public class Memory {
    private int[] memory = new int[256000];
    public Memory() {
        //default
    }

    //Only restriction on memory is that it be greater than -1
    public Memory(int size) {
        size = Math.abs(size);
        memory = new int[size];
    }

    public void reset() {
        memory = new int[256000];
    }

    public void storeWord(int index, int value) {
        memory[index] = value;
    }

    public int retrieveWord(int index) {
        return memory[index];
    }

    public void writeChanged() {
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] != 0) {
                System.out.println("Memory@index " + i + " : " + memory[i]);
            }
        }
    }
}
