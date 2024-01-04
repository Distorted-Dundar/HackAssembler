package org.nand2tetris;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

import static org.nand2tetris.Command.*;
import static org.nand2tetris.Command.A_COMMAND;

public class HackAssemblerParser implements AssemblerParser {

    HashMap<String, Integer> symbolTable = new HashMap<>();
    Scanner reader;
    int lastAvailableRamEntry = 16;
    int currentLine;
    boolean isFirstPass;

    String currentCommand = "NULL";
    Command currentCommandType;

    public HackAssemblerParser(String fileLocation) {
        try {
            File file = new File(fileLocation);
            this.reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new UnsupportedOperationException("Could not find or open the file" + fileLocation);
        }
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1); // The only registers are A, D and M
        symbolTable.put("R2", 2); // These  are really Aliases for Ram registers
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);

//        Are in a different part Not in RAM
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);

//        In low Memory
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);

//        We provide memory allocations after 15
        currentLine = 0;
        firstPass(fileLocation);
    }

    /**
     * The first pass checks for labels and adds them to the symbol table it registers it as the next line
     * of executable code.
     */
    private void firstPass(String fileLocation) {
        this.isFirstPass = true;
        while (hasMoreLines()) {
            advance();
        }
        this.isFirstPass = false;
        try {
            File file = new File(fileLocation);
            this.reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new UnsupportedOperationException("Could not find or open the file" + fileLocation);
        }
        this.currentLine = 0;
        this.currentCommand = "NULL";
        this.currentCommandType = null;
    }

    @Override
    public boolean hasMoreLines() {
        return reader.hasNextLine();
    }

    @Override
    public void advance() {
        closeFileWhenNoMoreLinesArePresent();

        String cmd = "";

        while (hasMoreLines()) {
            String[] lexemes = this.reader.nextLine().split("\\s+");

            for (String element : lexemes) {
                if (element.equals("//")) {
                    break;
                } else if (element.isEmpty()) {
                    continue;
                }
                cmd = element;
            }
            if (!cmd.isEmpty()) {
                break;
            }
        }
//      Decides what to do if no command is found
        if (cmd.isEmpty()) {
            return;
        }
        this.currentCommand = cmd;

        if (isFirstPass) {
            addLabelToSymbolTable();
        } else {
            addAddressableSymbolToSymbolTable();
        }
        if (instructionType() != L_COMMAND)
            this.currentLine += 1;
    }

    private void addAddressableSymbolToSymbolTable() {
        boolean AInstructionIsNumeric = instructionType() == A_COMMAND && !symbol().matches("^[0-9]*$");
        if (AInstructionIsNumeric && !symbolTable.containsKey(symbol())) {
            symbolTable.put(symbol(), lastAvailableRamEntry);
            lastAvailableRamEntry += 1;
        }
    }

    private void addLabelToSymbolTable() {
        boolean labeledCommandSymbolIsNotFound = instructionType() == L_COMMAND && !symbolTable.containsKey(symbol());
        if (labeledCommandSymbolIsNotFound) {
            symbolTable.put(symbol(), currentLine);
        }
    }

    private void closeFileWhenNoMoreLinesArePresent() {
        if (!hasMoreLines())
            this.reader.close();

    }

    public int getAddress(String symbol) {
        if (symbol.matches("^[0-9]*$")) {
            return Integer.parseInt(symbol);
        }
        return symbolTable.get(symbol);
    }

    @Override
    public Command instructionType() {
        if (this.currentCommand.contains("=") || this.currentCommand.contains(";")) {
            this.currentCommandType = C_COMMAND;
        } else if (this.currentCommand.contains("@")) {
            this.currentCommandType = A_COMMAND;
        }
//        Contains a (*) in the instruction
        else {
            this.currentCommandType = L_COMMAND;
        }

        return this.currentCommandType;
    }

    @Override
    public String symbol() {
        int AInstructionSymbolIndex = 1;
        Command type = instructionType();
        if (type == C_COMMAND) {
            throw new UnsupportedOperationException("Cant call symbol on a C command" + C_COMMAND);
        } else if (type == A_COMMAND) {
            return this.currentCommand.substring(AInstructionSymbolIndex);
        }
//        L String (*) strips the left and right
        int endIndex = this.currentCommand.length() - 1;
        return this.currentCommand.substring(1, endIndex);
    }

    @Override
    public String dest() {
        raiseExceptionWhenInstructionTypeIsNotControl(instructionType(), "dest()");

        if (!this.currentCommand.contains("=")) {
            return "null";
        }
        String regexPattern = "((?=;|=|@)|(?<=;|=|@))";
        String[] lexemes = this.currentCommand.split(regexPattern);

        return lexemes[0];
    }

    @Override
    public String comp() {
        raiseExceptionWhenInstructionTypeIsNotControl(instructionType(), "comp()");

        int compIndex;
        if (this.currentCommand.contains("="))
            compIndex = 2;
        else
            compIndex = 0;

        String regexPattern = "((?=;|=|@)|(?<=;|=|@))";
        String[] lexemes = this.currentCommand.split(regexPattern);

        return lexemes[compIndex];
    }

    @Override
    public String jump() {
        raiseExceptionWhenInstructionTypeIsNotControl(instructionType(), "jump()");

        if (!this.currentCommand.contains(";")) {
            return "null";
        }
        String regexPattern = "((?=;|=|@)|(?<=;|=|@))";
        String[] lexemes = this.currentCommand.split(regexPattern);

        int jumpInstructionIndex = lexemes.length - 1;
        return lexemes[jumpInstructionIndex];
    }

    private void raiseExceptionWhenInstructionTypeIsNotControl(Command currentCommandType, String instructionComponent) {
        if (currentCommandType != Command.C_COMMAND) {
            throw new UnsupportedOperationException("Cant call " + instructionComponent + " on" + currentCommandType);
        }
    }

}

