package org.nand2tetris;

/**
 * The Code interface that enables you to call methods that return
 * binary representation of certain mnemonics for example
 * calling jump("JGT"); // 001
 */
public interface AssemblerCode {
    /**
     * Returns the binary code of the dest mnemonic.
     *
     * @return 3 bits to signify dest
     */
     String dest(String mnemonic);

    /**
     * Returns the binary code of the comp mnemonic.
     *
     * @return 7 bits to signify dest
     */
    String comp(String mnemonic);

    /**
     * Returns the binary code of the jump mnemonic.
     *
     * @return 3 bits to signify dest
     */
    String jump(String mnemonic);

    /**
     * Translates a addressableInstruction to 16 binary format <br>
     * For example addressInstruction("12"); // [0]000000000001100
     */
     String addressInstruction(int num);

    /**
     * Is only called when you want to figure out a C instruction prefix.
     * If the instruction involves the M register IN THE COMP field.
     * Then it must mean the a bit
     * is asserted otherwise its 0. This is the last bit in the prefix.
     */
     String getCInstructionPrefix(String comp);
}
