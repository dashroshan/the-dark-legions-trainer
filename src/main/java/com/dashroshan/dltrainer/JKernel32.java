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

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JKernel32 {
    public static boolean closeHandle(WinNT.HANDLE hObject) {
        return Kernel32.INSTANCE.CloseHandle(hObject);
    }

    public static String getLastError() {
        return Kernel32Util.formatMessageFromLastErrorCode(Native.getLastError());
    }

    public static boolean hookKeyboard(final KeyboardHookInfo keyboardHookInfo) {
        keyboardHookInfo.start();
        return keyboardHookInfo.thread.isAlive();
    }

    public static WinNT.HANDLE openProcess(int dwProcessId) {
        return Kernel32.INSTANCE.OpenProcess(
                Kernel32.PROCESS_VM_OPERATION | Kernel32.PROCESS_VM_WRITE | Kernel32.PROCESS_VM_READ, false,
                dwProcessId);
    }

    public static byte[] readProcessMemory(WinNT.HANDLE hProcess, int lpBaseAddress, int nSize) {
        IntByReference baseAddress = new IntByReference();
        baseAddress.setValue(lpBaseAddress);
        Memory lpBuffer = new Memory(nSize);
        boolean success = Kernel32.INSTANCE.ReadProcessMemory(hProcess, new Pointer(lpBaseAddress), lpBuffer, nSize,
                null);
        return success ? lpBuffer.getByteArray(0, nSize) : null;
    }

    public static boolean unHookKeyboard(KeyboardHookInfo info) {
        info.quit = true;
        return User32.INSTANCE.UnhookWindowsHookEx(info.hHook);
    }

    public static boolean writeProcessMemory(WinNT.HANDLE hProcess, int lpBaseAddress, int lpBuffer[]) {
        int length = lpBuffer.length;
        Memory memory = new Memory(length);
        for (int i = 0; i < length; i++) {
            memory.setByte(i, (byte) lpBuffer[i]);
        }
        IntByReference lpNumberOfBytesWritten = new IntByReference();
        return Kernel32.INSTANCE.WriteProcessMemory(hProcess, new Pointer(lpBaseAddress), memory,
                length, lpNumberOfBytesWritten);
    }

    public abstract static class KeyboardHookInfo implements Runnable, KeyListener {
        private static final int LLKHF_ALTDOWN = 32;
        private static final int LLKHF_EXTENDED = 1;
        private static final int LLKHF_UP = 128;
        private final Component source = new KeyHookComponent();
        WinUser.HHOOK hHook;
        volatile boolean quit;
        private Thread thread;

        final void start() {
            thread = new Thread(this);
            thread.start();
        }

        public final void stop() {
            quit = true;
        }

        KeyEvent toKeyEvent(WinUser.KBDLLHOOKSTRUCT lParam) {
            int modifiers = (lParam.flags & LLKHF_ALTDOWN) == 0 ? 0 : KeyEvent.ALT_DOWN_MASK;
            int location = (lParam.flags & LLKHF_EXTENDED) == 0 ? KeyEvent.KEY_LOCATION_STANDARD
                    : KeyEvent.KEY_LOCATION_NUMPAD;
            int vkCode = lParam.vkCode;
            char keyChar = (char) vkCode;
            return new KeyEvent(source, 0, lParam.time, modifiers, vkCode, keyChar, location);
        }

        private static class KeyHookComponent extends Component {
            private static final long serialVersionUID = -3521589097108693427L;
        }

        @Override
        public final void run() {
            WinDef.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
            WinUser.HOOKPROC proc = new WinUser.LowLevelKeyboardProc() {
                public WinDef.LRESULT callback(int nCode, WinDef.WPARAM wParam,
                        WinUser.KBDLLHOOKSTRUCT lParam) {
                    final KeyEvent keyEvent = toKeyEvent(lParam);
                    if ((lParam.flags & LLKHF_UP) == 0) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                keyReleased(keyEvent);
                                keyTyped(keyEvent);
                            }
                        });
                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                keyPressed(keyEvent);
                            }
                        });
                    }
                    return User32.INSTANCE.CallNextHookEx(hHook, nCode, wParam,
                            new WinDef.LPARAM(Pointer.nativeValue(lParam.getPointer())));
                }
            };
            hHook = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL, proc, hMod,
                    0);
            if (hHook == null) {
                quit = true;
                return;
            }
            WinUser.MSG msg = new WinUser.MSG();
            while (!quit) {
                User32.INSTANCE.PeekMessage(msg, null, 0, 0, 0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    quit = true;
                }
            }
            unHookKeyboard(this);
            thread = null;
        }

    }
}
