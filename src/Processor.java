public class Processor {
    //registers
    private int[] registers = new int[32]; //index 0 points to register 0, etc
    //r27 = stack ptr
    private int pc = 0; //program counter

    private Memory processorMemory = new Memory();

    public Processor() {
        //Default
    }

    /** Allows a variable number of registers greater than 32, and custom memory size */
    public Processor(short registersCount, int memorySize) {
        registersCount = (short)Math.abs((int)registersCount); //ensure positive numbers
        memorySize = Math.abs(memorySize);
        if (registersCount >= 32) {
            registers = new int[registersCount];
        }

        processorMemory = new Memory(memorySize);
    }

    /** Input: the number of the register to print, 0 to 31
     *  Output: "r" + [registerNumber] + " : " + [registerValue] + "\n"*/
    public void printRegister(int register) {
        System.out.println("r" + register + " : " + registers[register]);
    }
    private void setR0() { registers[0] = 0; } //r0 should always be 0, execute after every instruction completes

    //INSTRUCTIONSET - Instructions that begin with T have 2 inputs, I have one input, X have zero inputs, otherwise 3 inputs
    /** Input: destination register, register to add, value to add */
    private void addi(int rA, int rB, short IMM16) {
        try {
            registers[rA] = registers[rB] + (int)IMM16;
        } catch (IndexOutOfBoundsException noSuchRegister) {
            //do nothing
        } finally {
            setR0();
        }
    }

    /** Input: destination register, register to add, register to add */
    private void add(int rA, int rB, int rC) {
        try {
            registers[rA] = registers[rB] + registers[rC];
        } catch (IndexOutOfBoundsException noSuchRegister) {
            //do nothing
        } finally {
            setR0();
        }
    }

    /** Input: destination register, register to subtract from, register w/ amount to subtract */
    private void sub(int rA, int rB, int rC) {
        try {
            registers[rA] = registers[rB] - registers[rC];
        } catch (IndexOutOfBoundsException noSuchRegister) {
            //do nothing
        } finally {
            setR0();
        }
    }

    /** Input - register to store, amount to add to location, register with location to begin from */
    private void stw(int register, int offset, int ptr) {
        try {
            processorMemory.storeWord(registers[ptr] + offset, registers[register]);
        } catch (IndexOutOfBoundsException noSuchRegister) {
            //do nothing
        } finally {
            setR0();
        }
    }

    /** Input - register to store to, amount to add to location, register with location to begin from */
    private void ldw(int register, int offset, int ptr) {
        try {
            registers[register] = processorMemory.retrieveWord(registers[ptr] + offset);
        } catch (IndexOutOfBoundsException noSuchRegister) {
            //do nothing
        } finally {
            setR0();
        }
    }

    /** Input - new value for the program counter */
    private void I_jump(int newPC) {
        if (newPC >= 0) {
            pc = newPC;
        }
        setR0();
    }

    /** Inputs - r1, the greater register, r2, the lesser register, newPC, the program counter
     *  Outcome - if r1 > r2, pc = newPC */
    private void jumpG(int r1, int r2, int newPC) {
        //System.out.println("ENTERED - Old-" + pc + " :: New-" + newPC);
        if (newPC >= 0 && r1 < registers.length && r2 < registers.length) {
            //System.out.println("r1@" + r1 + ": " + registers[r1]);
            //System.out.println("r2@" + r2 + ": " + registers[r2]);
            if (registers[r1] > registers[r2]) {
                //System.out.println("Old - " + pc + " :: New - " + newPC);
                pc = newPC;
            } else {
                pc++;
            }
        }
        setR0();
    }

    /** Inputs - r1, the greater register, r2, the lesser register, newPC, the program counter
     *  Outcome - if r1 >= r2, pc = newPC */
    private void jumpGE(int r1, int r2, int newPC) {
        if (newPC >= 0 && r1 < registers.length && r2 < registers.length) {
            if (registers[r1] >= registers[r2]) {
                pc = newPC;
            } else {
                pc++;
            }
        }
        setR0();
    }

    private void I_branch(int newPC) {
        if (newPC >= 0) {
            registers[26] = pc + 1; //set r26 to the next value
            I_jump(newPC);
        } else {
            pc++;
        }
        setR0();
    }

    private void X_endBranch() {
        if (registers[26] >= 0) {
            I_jump(registers[26]); //If r26 is clean, jump to it
        } else {
            pc++;
        }
        setR0();
    }

    private void X_halt() {
        System.out.println("Execution Halted");
        this.expose();
        System.exit(11111); //This is the halt status
    }

    //END INSTRUCTIONSET

    public void execute(Parser input) {
        while (pc < input.instructions.size()) {
            this.interpret(input.instructions.get(pc));
            //Do not advance the pc - that is done by the interpret function
        }
    }

    public void interpret(String instruction) {
        System.out.println("Fetched instruction: " + instruction);
        String operation = instruction.substring(0, instruction.indexOf(" "));
        if (operation.charAt(0) != 'T' && operation.charAt(0) != 'I' && operation.charAt(0) != 'X') {
            //all these instructions should have three inputs
            String portion1 = instruction.substring(instruction.indexOf("(") + 1); //cuts to the next char
            String portion2 = portion1.substring(portion1.indexOf(" ") + 1);
            String portion3 = portion2.substring(portion2.indexOf(" ") + 1);
            String getFirst = portion1.substring(0, portion1.indexOf(" "));
            String getSecond = portion2.substring(0, portion2.indexOf(" "));
            String getThird = portion3.substring(0, portion3.indexOf(")"));

            int i1 = Integer.parseInt(getFirst);
            int i2 = Integer.parseInt(getSecond);
            int i3 = Integer.parseInt(getThird);
            //System.out.println("Executing with: " + operation + "-" + i1 + "-" + i2 + "-" + i3);
            try {
                switch (operation) {
                    case "ADDI":
                        addi(i1, i2, (short)i3);
                        pc++;
                        break;
                    case "ADD":
                        add(i1, i2, i3);
                        pc++;
                        break;
                    case "SUB":
                        sub(i1, i2, i3);
                        pc++;
                        break;
                    case "STW":
                        stw(i1, i2, i3);
                        pc++;
                        break;
                    case "LDW":
                        ldw(i1, i2, i3);
                        pc++;
                        break;
                    case "JUMPG":
                        jumpG(i1, i2, i3); //jumps should not increment the program counter
                        break;
                    case "JUMPGE":
                        jumpGE(i1, i2, i3);
                        break;
                    case "XHALT":
                    default:
                        System.out.println("Reached unexpected halt null");
                        X_halt();
                }
                //System.out.println("Executed instruction: " + instruction);
            } catch (Exception badInput) {
                System.err.println("Bad input - " + instruction);
                System.exit(-503);
            }
        } else {
            if (operation.equals("XHALT")) {
                //System.out.println("Reached correct halt statement");
                X_halt();
            } else if (operation.charAt(0) == 'T') {
                String portion1 = instruction.substring(instruction.indexOf("(") + 1); //cuts to the next char
                String portion2 = portion1.substring(portion1.indexOf(" ") + 1);
                String getFirst = portion1.substring(0, portion1.indexOf(" "));
                String getSecond = portion2.substring(0, portion2.indexOf(")"));
                int i1 = Integer.parseInt(getFirst);
                int i2 = Integer.parseInt(getSecond);
                //System.out.println("Executing with: " + operation + "-" + i1 + "-" + i2);
                switch (operation) {
                    default:
                        System.out.println("Reached unexpected halt T");
                        X_halt();
                }
            } else if (operation.charAt(0) == 'I') {
                String portion1 = instruction.substring(instruction.indexOf("(") + 1);
                String getFirst = portion1.substring(0, portion1.indexOf(")"));
                int i1 = Integer.parseInt(getFirst);
                //System.out.println("Executing with: " + operation + "-" + i1);
                switch (operation) {
                    case "IJUMP":
                        I_jump(i1);
                        //pc++;
                        break;
                    case "IBRANCH":
                        I_branch(i1);
                        break;
                    default:
                        System.out.println("Reached unexpected halt I");
                        X_halt();
                }
            } else if (operation.charAt(0) == 'X') {
                //this one needs no parsing, only handle the operation
                switch (operation) {
                    case "XENDBRANCH":
                        X_endBranch();
                        break;
                    case "XHALT":
                    default:
                        System.out.println("Reached unexpected halt X");
                        X_halt();
                }
            }
            //System.out.println("Reached skipped reading halt");
            //X_halt();
        }
    }

    public void expose() {
        System.out.println("Processor values:");
        for (int i = 0; i < registers.length; i++) {
            if (registers[i] != 0) {
                System.out.println("r" + i + " : " + registers[i]);
            }
        }

        System.out.println("Memory values:");
        processorMemory.writeChanged();

        System.out.println("Program Counter : " + pc);
    }
}
