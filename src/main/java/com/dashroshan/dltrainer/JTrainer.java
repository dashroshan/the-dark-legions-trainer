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

import com.sun.jna.platform.win32.WinDef.HWND;
import com.dashroshan.dltrainer.exception.MemoryException;
import com.dashroshan.dltrainer.exception.WindowNotFoundException;
import com.sun.jna.platform.win32.WinNT;

public class JTrainer {
    private int pid = -1;
    private String windowClass = "";
    private String windowText = "";

    public JTrainer(int pid) {
        this.pid = pid;
    }

    public JTrainer(String partialWindowText) throws WindowNotFoundException {
        HWND hWnd = JUser32.findWindowByTitle(partialWindowText);
        if (hWnd == null) {
            throw new WindowNotFoundException(null, partialWindowText);
        }
        this.windowClass = null;
        this.windowText = JUser32.getWindowText(hWnd);
        this.pid = getProcessIdFromWindow();
    }

    private int getProcessIdFromWindow() throws WindowNotFoundException {
        return getWindowTheadProcessId(findWindow(windowClass, windowText));
    }

    /*
     * Internal Functions
     */
    private HWND findWindow(String lpClassName, String lpWindowName) throws WindowNotFoundException {
        HWND hWnd = JUser32.findWindow(lpClassName, lpWindowName);
        if (hWnd == null) {
            throw new WindowNotFoundException(lpClassName, lpWindowName);
        }
        return hWnd;
    }

    private int getWindowTheadProcessId(HWND hWnd) {
        return JUser32.getWindowThreadProcessId(hWnd);
    }

    public JTrainer(String windowClass, String windowText) throws WindowNotFoundException {
        this.windowClass = windowClass;
        this.windowText = windowText;
        this.pid = getProcessIdFromWindow();
    }

    public boolean hookKeyboard(final JKernel32.KeyboardHookInfo keyboardHookInfo) {
        return JKernel32.hookKeyboard(keyboardHookInfo);
    }

    public boolean isProcessAvailable() {
        WinNT.HANDLE hProcess = JKernel32.openProcess(pid);
        return hProcess != null;
    }

    public byte[] readProcessMemory(int lpBaseAddress, int nSize) throws MemoryException {
        WinNT.HANDLE hProcess = openProcess();
        byte[] result = JKernel32.readProcessMemory(hProcess, lpBaseAddress, nSize);
        if (result == null) {
            throw new MemoryException("ReadProcessMemory", getLastError());
        }
        JKernel32.closeHandle(hProcess);
        return result;
    }

    private WinNT.HANDLE openProcess() throws MemoryException {
        WinNT.HANDLE hProcess = JKernel32.openProcess(pid);
        if (hProcess == null) {
            throw new MemoryException("OpenProcess", getLastError());
        }
        return hProcess;
    }

    /*
     * External Functions
     */
    public String getLastError() {
        return JKernel32.getLastError();
    }

    public void setWindow(String windowClass, String windowText) throws WindowNotFoundException {
        this.windowClass = windowClass;
        this.windowText = windowText;
        reloadProcess();
    }

    public void reloadProcess() throws WindowNotFoundException {
        this.pid = getProcessIdFromWindow();
    }

    public boolean unHookKeyboard(JKernel32.KeyboardHookInfo info) {
        return JKernel32.unHookKeyboard(info);
    }

    public void writeProcessMemory(int lpBaseAddress, int lpBuffer[]) throws MemoryException {
        WinNT.HANDLE hProcess = openProcess();
        boolean success = JKernel32.writeProcessMemory(hProcess, lpBaseAddress, lpBuffer);
        if (!success) {
            throw new MemoryException("WriteProcessMemory", getLastError());
        }
        JKernel32.closeHandle(hProcess);
    }
}
