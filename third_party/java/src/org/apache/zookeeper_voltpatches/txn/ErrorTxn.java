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

package org.apache.zookeeper_voltpatches.txn;

import org.apache.jute_voltpatches.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;

public class ErrorTxn implements Record, Comparable<ErrorTxn> {
    private int err;
    public ErrorTxn() {
    }
    public ErrorTxn(int err) {
        this.err = err;
    }
    public int getErr() {
        return err;
    }
    public void setErr(int m_) {
        err = m_;
    }
    @Override
    public void serialize(OutputArchive a_, String tag) throws IOException {
        a_.startRecord(this,tag);
        a_.writeInt(err,"err");
        a_.endRecord(this,tag);
    }
    @Override
    public void deserialize(InputArchive a_, String tag) throws IOException {
        a_.startRecord(tag);
        err = a_.readInt("err");
        a_.endRecord(tag);
    }

    @Override
    public void writeCSV(CsvOutputArchive a) throws IOException {
        a.startRecord(this,"");
        a.writeInt(err,"err");
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
    public int compareTo(ErrorTxn peer_) {
        return Comparator.comparingInt(ErrorTxn::getErr).compare(this, peer_);
    }
    @Override
    public boolean equals(Object peer_) {
        if (! (peer_ instanceof ErrorTxn)) {
            return false;
        } else {
            return peer_ == this || compareTo((ErrorTxn) peer_) == 0;
        }
    }
    @Override
    public int hashCode() {
        int result = 17;
        int ret = err;
        return 37*result + ret;
    }
    public static String signature() {
        return "LErrorTxn(i)";
    }
}
