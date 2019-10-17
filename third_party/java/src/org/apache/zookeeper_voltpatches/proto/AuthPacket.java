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
import java.util.Arrays;
import java.util.Comparator;

public class AuthPacket implements Record<AuthPacket> {
    private int type;
    private String scheme;
    private byte[] auth;
    public AuthPacket() {
    }
    public AuthPacket(int type, String scheme, byte[] auth) {
        this.type = type;
        this.scheme = scheme;
        this.auth = auth;
    }
    public int getType() {
        return type;
    }
    public void setType(int m_) {
        type = m_;
    }
    public String getScheme() {
        return scheme;
    }
    public void setScheme(String m_) {
        scheme = m_;
    }
    public byte[] getAuth() {
        return auth;
    }
    public void setAuth(byte[] m_) {
        auth = m_;
    }
    @Override
    public void serialize(OutputArchive a_, String tag) throws IOException {
        a_.startRecord(this,tag);
        a_.writeInt(type,"type");
        a_.writeString(scheme,"scheme");
        a_.writeBuffer(auth,"auth");
        a_.endRecord(this,tag);
    }
    @Override
    public void deserialize(InputArchive a_, String tag) throws IOException {
        a_.startRecord(tag);
        type = a_.readInt("type");
        scheme = a_.readString("scheme");
        auth = a_.readBuffer("auth");
        a_.endRecord(tag);
    }

    @Override
    public String toString() {
        return toStringHelper();
    }
    @Override
    public int compareTo(AuthPacket peer_) {
        return Comparator.comparingInt(AuthPacket::getType)
                .thenComparing(AuthPacket::getScheme)
                .thenComparing(AuthPacket::getAuth, Utils::compareBytes)
                .compare(this, peer_);
    }
    @Override
    public boolean equals(Object peer_) {
        if (!(peer_ instanceof AuthPacket)) {
            return false;
        } else {
            return peer_ == this || compareTo((AuthPacket) peer_) == 0;
        }
    }
    @Override
    public int hashCode() {
        int result = 17;
        int ret;
        ret = type;
        result = 37*result + ret;
        ret = scheme.hashCode();
        result = 37*result + ret;
        ret = Arrays.toString(auth).hashCode();
        return 37*result + ret;
    }
    public static String signature() {
        return "LAuthPacket(isB)";
    }
}
