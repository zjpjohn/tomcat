/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tomcat.util.http.parser;

import java.util.BitSet;

import org.junit.Test;

public class TesterParserPerformance {

    @Test
    public void testBitSetVsBooleanArray() {
        Lookup bitsetLookup = new BitSetLookup();
        Lookup booleanArrayLookup = new BooleanArrayLookup();

        int count = 10000;
        int loops = 5;

        // Warm up
        doLookupTest(bitsetLookup, count);
        doLookupTest(booleanArrayLookup, count);

        for (int i = 0; i < loops; i++) {
            System.out.println("Bitset   : " +  doLookupTest(bitsetLookup, count) + "ns");
            System.out.println("Boolean[]: " +  doLookupTest(booleanArrayLookup, count) + "ns");
        }
    }


    private long doLookupTest(Lookup lookup, int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < 128; j++) {
                lookup.doLookup(j);
            }
        }
        return System.nanoTime() - start;
    }

    private interface Lookup {
        boolean doLookup(int i);
    }


    private static class BitSetLookup implements Lookup {

        private BitSet values = new BitSet(128);

        @Override
        public boolean doLookup(int i) {
            return values.get(i);
        }
    }


    private static class BooleanArrayLookup implements Lookup {

        private boolean[] values = new boolean[128];

        @Override
        public boolean doLookup(int i) {
            return values[i];
        }
    }
}
