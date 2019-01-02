/* This file is part of VoltDB.
 * Copyright (C) 2008-2019 VoltDB Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package org.voltdb.export;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.voltdb.BackendTarget;
import org.voltdb.VoltDB.Configuration;
import org.voltdb.client.Client;
import org.voltdb.client.ClientImpl;
import org.voltdb.client.ClientResponse;
import org.voltdb.compiler.VoltProjectBuilder;
import org.voltdb.regressionsuites.LocalCluster;
import org.voltdb.regressionsuites.MultiConfigSuiteBuilder;
import org.voltdb.regressionsuites.TestSQLTypesSuite;
import org.voltdb.utils.MiscUtils;
import org.voltdb.utils.VoltFile;

public class TestExportV2SuitePro extends TestExportBaseSocketExport {
    private static final int k_factor = 1;

    @Override
    public void setUp() throws Exception
    {
        m_username = "default";
        m_password = "password";
        VoltFile.recursivelyDelete(new File("/tmp/" + System.getProperty("user.name")));
        File f = new File("/tmp/" + System.getProperty("user.name"));
        f.mkdirs();
        super.setUp();

        startListener();
        m_verifier = new ExportTestExpectedData(m_serverSockets, m_isExportReplicated, true, k_factor+1);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        System.out.println("Shutting down client and server");
        closeClientAndServer();
    }

    // Test Export of an ADDED table.
    //
    public void testExportAndAddedTable() throws Exception {
        System.out.println("testExportAndAddedTable");
        final Client client = getClient();
        while (!((ClientImpl) client).isHashinatorInitialized()) {
            Thread.sleep(1000);
            System.out.println("Waiting for hashinator to be initialized...");
        }

        // add a new table
        final String newCatalogURL = Configuration.getPathToCatalogForTest("export-ddl-addedtable.jar");
        final String deploymentURL = Configuration.getPathToCatalogForTest("export-ddl-addedtable.xml");
        final ClientResponse callProcedure = client.updateApplicationCatalog(new File(newCatalogURL),
                                                                             new File(deploymentURL));
        assertTrue(callProcedure.getStatus() == ClientResponse.SUCCESS);

        // verify that it exports
        for (int i=0; i < 10; i++) {
            final Object[] rowdata = TestSQLTypesSuite.m_midValues;
            m_verifier.addRow(client, "ADDED_TABLE", i, convertValsToRow(i, 'I', rowdata));
            // Grp tables added to verifier because they are needed by ExportToFileVerifier
            m_verifier.addRow(client, "ADDED_TABLE_GRP", i, convertValsToRow(i, 'I', rowdata));
            final Object[]  params = convertValsToParams("ADDED_TABLE", i, rowdata);
            final Object[]  paramsGrp = convertValsToParams("ADDED_TABLE_GRP", i, rowdata);
            client.callProcedure("InsertAddedTable", params);
            client.callProcedure("InsertAddedTable", paramsGrp);
        }

        quiesceAndVerifyTarget(client, m_verifier);
    }

    //  Test Export of a DROPPED table.  Queues some data to a table.
    //  Then drops the table and verifies that Export can successfully
    //  drain the dropped table. IE, drop table doesn't lose Export data.
    //
    public void testExportAndDroppedTable() throws Exception {
        System.out.println("testExportAndDroppedTable");
        Client client = getClient();
        for (int i=0; i < 10; i++) {
            final Object[] rowdata = TestSQLTypesSuite.m_midValues;
            m_verifier.addRow(client, "NO_NULLS", i, convertValsToRow(i, 'I', rowdata));
            // Grp tables added to verifier because they are needed by ExportToFileVerifier
            m_verifier.addRow(client, "NO_NULLS_GRP", i, convertValsToRow(i, 'I', rowdata));
            final Object[] params = convertValsToParams("NO_NULLS", i, rowdata);
            final Object[] paramsGrp = convertValsToParams("NO_NULLS_GRP", i, rowdata);
            client.callProcedure("Insert", params);
            client.callProcedure("Insert", paramsGrp);
        }

        // now drop the no-nulls table
        final String newCatalogURL = Configuration.getPathToCatalogForTest("export-ddl-sans-nonulls.jar");
        final String deploymentURL = Configuration.getPathToCatalogForTest("export-ddl-sans-nonulls.xml");
        quiesce(client);
        final ClientResponse callProcedure = client.updateApplicationCatalog(new File(newCatalogURL),
                                                                             new File(deploymentURL));
        assertTrue(callProcedure.getStatus() == ClientResponse.SUCCESS);

        client = getClient();

        // must still be able to verify the export data.
        quiesceAndVerifyTarget(client, m_verifier);
    }

    public TestExportV2SuitePro(final String name) {
        super(name);
    }

    static public junit.framework.Test suite() throws Exception
    {
        LocalCluster config;

        System.setProperty(ExportDataProcessor.EXPORT_TO_TYPE, "org.voltdb.exportclient.SocketExporter");
        Map<String, String> additionalEnv = new HashMap<>();
        additionalEnv.put(ExportDataProcessor.EXPORT_TO_TYPE, "org.voltdb.exportclient.SocketExporter");

        final MultiConfigSuiteBuilder builder =
            new MultiConfigSuiteBuilder(TestExportV2SuitePro.class);

        project = new VoltProjectBuilder();
        project.setSecurityEnabled(true, true);
        project.addRoles(GROUPS);
        project.addUsers(USERS);
        project.addSchema(TestSQLTypesSuite.class.getResource("sqltypessuite-export-ddl-with-target.sql"));
        project.addSchema(TestSQLTypesSuite.class.getResource("sqltypessuite-nonulls-export-ddl-with-target.sql"));

        wireupExportTableToSocketExport("ALLOW_NULLS");
        wireupExportTableToSocketExport("NO_NULLS");
        wireupExportTableToSocketExport("ALLOW_NULLS_GRP");
        wireupExportTableToSocketExport("NO_NULLS_GRP");
        project.addProcedures(PROCEDURES);


        // JNI, single server
        // Use the cluster only config. Multiple topologies with the extra catalog for the
        // Add drop tests is harder. Restrict to the single (complex) topology.
        //
        //        config = new LocalSingleProcessServer("export-ddl.jar", 2,
        //                                              BackendTarget.NATIVE_EE_JNI);
        //        config.compile(project);
        //        builder.addServerConfig(config);


        /*
         * compile the catalog all tests start with
         */
        config = new LocalCluster("export-ddl-cluster-rep.jar", 2, 3, k_factor,
                BackendTarget.NATIVE_EE_JNI, LocalCluster.FailureState.ALL_RUNNING, true, false, additionalEnv);
        config.setHasLocalServer(false);
        config.setMaxHeap(1024);
        boolean compile = config.compile(project);
        assertTrue(compile);
        builder.addServerConfig(config, false);


        /*
         * compile a catalog without the NO_NULLS table for add/drop tests
         */
        config = new LocalCluster("export-ddl-sans-nonulls.jar", 2, 3, k_factor,
                BackendTarget.NATIVE_EE_JNI,  LocalCluster.FailureState.ALL_RUNNING, true, false, additionalEnv);
        config.setHasLocalServer(false);
        config.setMaxHeap(1024);
        project = new VoltProjectBuilder();
        project.addRoles(GROUPS);
        project.addUsers(USERS);
        project.addSchema(TestSQLTypesSuite.class.getResource("sqltypessuite-export-ddl-with-target.sql"));
        wireupExportTableToSocketExport("ALLOW_NULLS");
        wireupExportTableToSocketExport("ALLOW_NULLS_GRP");
        project.addProcedures(PROCEDURES2);
        compile = config.compile(project);
        MiscUtils.copyFile(project.getPathToDeployment(),
                Configuration.getPathToCatalogForTest("export-ddl-sans-nonulls.xml"));
        assertTrue(compile);

        /*
         * compile a catalog with an added table for add/drop tests
         */
        config = new LocalCluster("export-ddl-addedtable.jar", 2, 3, k_factor,
                BackendTarget.NATIVE_EE_JNI,  LocalCluster.FailureState.ALL_RUNNING, true,false,additionalEnv);
        config.setHasLocalServer(false);
        config.setMaxHeap(1024);
        project = new VoltProjectBuilder();
        project.addRoles(GROUPS);
        project.addUsers(USERS);
        project.addSchema(TestSQLTypesSuite.class.getResource("sqltypessuite-export-ddl-with-target.sql"));
        project.addSchema(TestSQLTypesSuite.class.getResource("sqltypessuite-nonulls-export-ddl-with-target.sql"));
        project.addSchema(TestSQLTypesSuite.class.getResource("sqltypessuite-addedtable-export-ddl-with-target.sql"));

        wireupExportTableToSocketExport("ALLOW_NULLS");
        wireupExportTableToSocketExport("ADDED_TABLE");
        wireupExportTableToSocketExport("NO_NULLS");  // streamed table
        wireupExportTableToSocketExport("ALLOW_NULLS_GRP");
        wireupExportTableToSocketExport("ADDED_TABLE_GRP");
        wireupExportTableToSocketExport("NO_NULLS_GRP"); // streamed table

        project.addProcedures(PROCEDURES);
        project.addProcedures(PROCEDURES3);
        compile = config.compile(project);
        MiscUtils.copyFile(project.getPathToDeployment(),
                Configuration.getPathToCatalogForTest("export-ddl-addedtable.xml"));
        assertTrue(compile);


        return builder;
    }
}
