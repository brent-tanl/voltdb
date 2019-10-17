// File generated by hadoop record compiler. Do not edit.
/**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.apache.zookeeper_voltpatches.data;


import org.apache.jute_voltpatches.*;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;

public class Id implements Record, Comparable<Id> {
    private String scheme;
    private String id;
    public Id() {
    }
    public Id(String scheme, String id) {
        this.scheme = scheme;
        this.id = id;
    }
    public String getScheme() {
        return scheme;
    }
    public void setScheme(String m_) {
        scheme = m_;
    }
    public String getId() {
        return id;
    }
    public void setId(String m_) {
        id = m_;
    }
    public void serialize(OutputArchive a_, String tag) throws IOException {
        a_.startRecord(this, tag);
        a_.writeString(scheme, "scheme");
        a_.writeString(id, "id");
        a_.endRecord(this, tag);
    }
    public void deserialize(InputArchive a_, String tag) throws IOException {
        a_.startRecord(tag);
        scheme = a_.readString("scheme");
        id = a_.readString("id");
        a_.endRecord(tag);
    }

    @Override
    public void writeCSV(CsvOutputArchive a) throws IOException {
        a.startRecord(this,"");
        a.writeString(scheme,"scheme");
        a.writeString(id,"id");
        a.endRecord(this,"");
    }

    @Override
    public String toString() {
        return toStringHelper();
    }
    public void write(DataOutput out) throws IOException {
        serialize(new BinaryOutputArchive(out), "");
    }
    public void readFields(DataInput in) throws IOException {
        deserialize(new BinaryInputArchive(in), "");
    }
    @Override
    public int compareTo(Id peer_) {
        return Comparator.comparing(Id::getScheme)
                .thenComparing(Id::getId)
                .compare(this, peer_);
    }
    @Override
    public boolean equals(Object peer_) {
        if (!(peer_ instanceof Id)) {
            return false;
        } else {
            return peer_ == this || compareTo((Id) peer_) == 0;
        }
    }
    @Override
    public int hashCode() {
        int result = 17;
        int ret;
        ret = scheme.hashCode();
        result = 37*result + ret;
        ret = id.hashCode();
        return 37*result + ret;
    }
    public static String signature() {
        return "LId(ss)";
    }
}
