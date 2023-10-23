package com.dashroshan.dltrainer;

public class DarkLegions {
    private void write(int offset, int value) {
        try {
            JTrainer jTrainer = new JTrainer(null, "The Dark Legions");
            int pointer = MemoryUtils.bytesToSignedInt(jTrainer.readProcessMemory(0x00A3DD0C, 4));
            jTrainer.writeProcessMemory(pointer + offset, MemoryUtils.intToHexIntArray(value));
        } catch (Exception e) {
        }
    }

    public int[] read() {
        try {
            JTrainer jTrainer = new JTrainer(null, "The Dark Legions");
            int pointer = MemoryUtils.bytesToSignedInt(jTrainer.readProcessMemory(0x00A3DD0C, 4));
            int wood = MemoryUtils.bytesToSignedInt(jTrainer.readProcessMemory(pointer + 32504, 4));
            int stone = MemoryUtils.bytesToSignedInt(jTrainer.readProcessMemory(pointer + 32512, 4));
            int gold = MemoryUtils.bytesToSignedInt(jTrainer.readProcessMemory(pointer + 32508, 4));
            int housing = MemoryUtils.bytesToSignedInt(jTrainer.readProcessMemory(pointer + 32728, 4));
            housing *= 5;
            housing = Math.min(256, housing);
            return new int[] { wood, stone, gold, housing };
        } catch (Exception e) {
        }
        return new int[] { 0, 0, 0, 0 };
    }

    public void wood(int value) {
        write(32504, value);
    }

    public void stone(int value) {
        write(32512, value);
    }

    public void gold(int value) {
        write(32508, value);
    }

    public void troops(int value) {
        write(32728, value);
    }
}
