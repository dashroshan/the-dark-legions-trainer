package com.dashroshan.dltrainer;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {
    private boolean shift = false;
    DarkLegions darkLegions = new DarkLegions();

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT)
            shift = true;
        else if (e.getKeyCode() == NativeKeyEvent.VC_W && shift == true) {
            darkLegions.wood(100000);
        } else if (e.getKeyCode() == NativeKeyEvent.VC_S && shift == true) {
            darkLegions.stone(100000);
        } else if (e.getKeyCode() == NativeKeyEvent.VC_G && shift == true) {
            darkLegions.gold(100000);
        } else if (e.getKeyCode() == NativeKeyEvent.VC_T && shift == true) {
            darkLegions.troops(100);
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT)
            shift = false;
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}