/**
 * Copyright (c) 2012 sprogcoder <sprogcoder@gmail.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dashroshan.dltrainer;

import java.util.Arrays;
import java.util.Collections;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;

public class MemoryUtils {
    private static final int FF = 0xFF;
    private static final int _16 = 16;
    private static final int _24 = 24;
    private static final int _8 = 8;

    /*
     * Byte Array Conversion Functions
     */
    public static int[] bytesToUnsignedInts(byte[] bytes) {
        int length = bytes.length;
        int[] realValues = new int[length];
        for (int i = 0; i < length; i++) {
            realValues[i] = bytes[i] & FF;
        }
        return realValues;
    }

    public static String[] bytesToUnsignedHexes(byte[] bytes) {
        int length = bytes.length;
        String[] hexValues = new String[length];
        for (int i = 0; i < length; i++) {
            hexValues[i] = Integer.toHexString(bytes[i] & FF);
        }
        return hexValues;
    }

    public static int bytesToSignedInt(byte[] bytes) {
        byte[] byteCopy = Arrays.copyOf(bytes, bytes.length);
        Collections.reverse(Bytes.asList(byteCopy));
        return Ints.fromByteArray(byteCopy);
    }

    public static int[] intToHexIntArray(int number) {
        int[] intArray = new int[4];

        for (int i = 0; i < 4; i++) {
            intArray[i] = (int) (number & 0xFF);
            number >>= 8;
        }

        return intArray;
    }

    /*
     * Unsigned Functions
     */
    public static int unsignedShortToInt(byte[] bytes) {
        int low = bytes[0] & FF;
        int high = bytes[1] & FF;
        return high << 8 | low;
    }

    public static long unsignedIntToLong(byte[] bytes) {
        long b1 = bytes[0] & FF;
        long b2 = bytes[1] & FF;
        long b3 = bytes[2] & FF;
        long b4 = bytes[3] & FF;
        return b1 << _24 | b2 << _16 | b3 << _8 | b4;
    }
}
