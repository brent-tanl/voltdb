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

package org.apache.zookeeper_voltpatches.proto;

import org.apache.jute_voltpatches.*;

import java.io.IOException;
import java.util.Comparator;

public class SetMaxChildrenRequest implements Record<SetMaxChildrenRequest> {
    private String path;
    private int max;
    public SetMaxChildrenRequest() {
    }
    public SetMaxChildrenRequest(String path, int max) {
        this.path = path;
        this.max = max;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String m_) {
        path = m_;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int m_) {
        max = m_;
    }
    @Override
    public void serialize(OutputArchive a_, String tag) throws IOException {
        a_.startRecord(this,tag);
        a_.writeString(path,"path");
        a_.writeInt(max,"max");
        a_.endRecord(this,tag);
    }
    @Override
    public void deserialize(InputArchive a_, String tag) throws IOException {
        a_.startRecord(tag);
        path = a_.readString("path");
        max = a_.readInt("max");
        a_.endRecord(tag);
    }

    @Override
    public String toString() {
        return toStringHelper();
    }
    @Override
    public int compareTo(SetMaxChildrenRequest peer_) {
        return Comparator.comparing(SetMaxChildrenRequest::getPath)
                .thenComparingLong(SetMaxChildrenRequest::getMax)
                .compare(this, peer_);
    }
    @Override
    public boolean equals(Object peer_) {
        if (!(peer_ instanceof SetMaxChildrenRequest)) {
            return false;
        } else {
            return  peer_ == this || compareTo((SetMaxChildrenRequest) peer_) == 0;
        }
    }
    @Override
    public int hashCode() {
        int result = 17;
        int ret;
        ret = path.hashCode();
        result = 37*result + ret;
        ret = max;
        return 37*result + ret;
    }
    public static String signature() {
        return "LSetMaxChildrenRequest(si)";
    }
}
