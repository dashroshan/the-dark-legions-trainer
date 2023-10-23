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

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;

public class JUser32 {
    public static HWND findWindow(String lpClassName, String lpWindowName) {
        String className = lpClassName == null || lpClassName.isEmpty() ? null : lpClassName;
        String windowName = lpWindowName == null || lpWindowName.isEmpty() ? null : lpWindowName;
        return User32.INSTANCE.FindWindow(className, windowName);
    }

    public static HWND findWindowByTitle(final String partialTitle) {
        final HWND[] found = new HWND[1];
        User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(HWND hWnd, Pointer arg1) {
                String wText = getWindowText(hWnd);
                System.out.println("Found window with text " + hWnd + ", Text: " + wText);
                boolean contains = wText.contains(partialTitle);
                if (contains) {
                    found[0] = hWnd;
                }
                return !contains;
            }
        }, null);
        return found[0];
    }

    public static String getWindowText(HWND hWnd) {
        int len = User32.INSTANCE.GetWindowTextLength(hWnd) * 4;
        char[] windowText = new char[len];
        User32.INSTANCE.GetWindowText(hWnd, windowText, len);
        String wText = Native.toString(windowText);
        return wText;
    }

    public static WinUser.WINDOWINFO getWindowInfo(HWND hWnd) {
        WinUser.WINDOWINFO pwi = new WinUser.WINDOWINFO();
        User32.INSTANCE.GetWindowInfo(hWnd, pwi);
        return pwi;
    }

    public static int getWindowThreadProcessId(HWND hWnd) {
        IntByReference lpdwProcessId = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(hWnd, lpdwProcessId);
        return lpdwProcessId.getValue();
    }
}
