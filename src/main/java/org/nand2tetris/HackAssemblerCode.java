package org.nand2tetris;

import java.util.Arrays;
import java.util.HashMap;

public class HackAssemblerCode implements AssemblerCode {
    HashMap<String, String> symbolBinaryTable;

    public HackAssemblerCode() {
        symbolBinaryTable = new HashMap<>();
        symbolBinaryTable.put("null", "000"); // for dest and jump

//        Computation bits
        //  a = 0 Translations
        symbolBinaryTable.put("0", "101010");
        symbolBinaryTable.put("1", "111111");
        symbolBinaryTable.put("-1", "111010");

//       D is omitted but found in the comp method
        symbolBinaryTable.put("!D", "001101");
        symbolBinaryTable.put("-D", "001111");
//        A is omitted but found in the comp method
        symbolBinaryTable.put("!A", "110001");
        symbolBinaryTable.put("-A", "110011");

        symbolBinaryTable.put("D+1", "011111");
        symbolBinaryTable.put("D-1", "001110");
        symbolBinaryTable.put("A+1", "110111");
        symbolBinaryTable.put("A-1", "110010");

        symbolBinaryTable.put("D+A", "000010");
        symbolBinaryTable.put("D-A", "010011");
        symbolBinaryTable.put("A-D", "000111");

        symbolBinaryTable.put("D&A", "000000");
        symbolBinaryTable.put("D|A", "010101");

        //  a = 1 translations
//      M is omitted but found in the comp method
        symbolBinaryTable.put("!M", "110001");
        symbolBinaryTable.put("-M", "110011");

        symbolBinaryTable.put("M+1", "110111");
        symbolBinaryTable.put("M-1", "110010");

        symbolBinaryTable.put("D+M", "000010");
        symbolBinaryTable.put("D-M", "010011");
        symbolBinaryTable.put("M-D", "000111");

        symbolBinaryTable.put("D&M", "000000");
        symbolBinaryTable.put("D|M", "010101");

//        Dest bits
        // null,M,D is omitted and only returned in the dest method
        symbolBinaryTable.put("DM", "011"); // 2nd edition
        symbolBinaryTable.put("MD", "011"); // 1st edition

        symbolBinaryTable.put("AM", "101");
//        A is omitted but found in the dest method
        symbolBinaryTable.put("AD", "110");

        symbolBinaryTable.put("AMD", "111");// 1st edition
        symbolBinaryTable.put("ADM", "111"); // 2nd edition

//      Jump bits
        // null is ommited and is handled in the jump method
        symbolBinaryTable.put("JGT", "001");
        symbolBinaryTable.put("JEQ", "010");
        symbolBinaryTable.put("JGE", "011");
        symbolBinaryTable.put("JLT", "100");
        symbolBinaryTable.put("JNE", "101");
        symbolBinaryTable.put("JLE", "110");
        symbolBinaryTable.put("JMP", "111");
    }


    public String getCInstructionPrefix(String comp) {
        if (comp.contains("M"))
            return "1111";
        return "1110";
    }

    @Override
    public String dest(String mnemonic) {
        if (mnemonic.equals("D"))
            return "010";
        else if (mnemonic.equals("A")) {
            return "100";
        } else if (mnemonic.equals("M")) {
            return "001";
        }
        raiseExceptionWhenSymbolIsNotFound(mnemonic, "dest");

        return symbolBinaryTable.get(mnemonic);
    }

    @Override
    public String comp(String mnemonic) {
        if (mnemonic.equals("D"))
            return "001100";
        else if (mnemonic.equals("A")) {
            return "110000";
        } else if (mnemonic.equals("M")) {
            return "110000";
        }
        raiseExceptionWhenSymbolIsNotFound(mnemonic, "comp");

        return symbolBinaryTable.get(mnemonic);
    }

    @Override
    public String jump(String mnemonic) {
        raiseExceptionWhenSymbolIsNotFound(mnemonic, "jump");
        return symbolBinaryTable.get(mnemonic);
    }

    /**
     * An exception is raised, since a symbolTable returns a null if not present
     * */
    private void raiseExceptionWhenSymbolIsNotFound(String mnemonic, String instructionComponent) {
        if (!symbolBinaryTable.containsKey(mnemonic))
            throw new UnsupportedOperationException(instructionComponent + " component with mnemonic: (" + mnemonic + ") is not found");
    }


    public String addressInstruction(int num) {
        if (num > 32767) // Would violate op code.
            throw new UnsupportedOperationException("Cant convert Numbers greater than " + 32767);

        char[] instruction = new char[16];
        Arrays.fill(instruction, '0');
        int currentBit = 0;

        while (num != 0) {
            instruction[15 - currentBit] = ("" + (num % 2)).charAt(0);
            num /= 2;
            currentBit++;
        }

        return new String(instruction);
    }
}
