package org.nand2tetris;

import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;

public class HackAssembler {
    public static void main(String @NotNull [] args) {
        Command currentCommandType;
        String symbol;
        int address;
        String assembledInstruction;
        String prefix, comp, jump, dest;

        AssemblerParser hackAssemblerParser = new HackAssemblerParser(args[0]);
        AssemblerCode hackAssemblerCode = new HackAssemblerCode();

        String path = args[0];
        System.out.printf("Parsing file with path: %s%n", path);
        String pathTemplate = path.substring(0, path.length() - 4);
        String writePath = pathTemplate + ".hack";

        try (FileWriter hackWriter = new FileWriter(writePath)) {

            while (hackAssemblerParser.hasMoreLines()) {
                hackAssemblerParser.advance();
                currentCommandType = hackAssemblerParser.instructionType();

                if (currentCommandType == Command.A_COMMAND) {
                    symbol = hackAssemblerParser.symbol();
                    address = hackAssemblerParser.getAddress(symbol);
                    assembledInstruction = hackAssemblerCode.addressInstruction(address);

                    hackWriter.write(assembledInstruction + "\n");
                } else if (currentCommandType == Command.C_COMMAND) {
                    prefix = hackAssemblerCode.getCInstructionPrefix(hackAssemblerParser.comp());
                    comp = hackAssemblerCode.comp(hackAssemblerParser.comp());
                    dest = hackAssemblerCode.dest(hackAssemblerParser.dest());
                    jump = hackAssemblerCode.jump(hackAssemblerParser.jump());
                    assembledInstruction = prefix + comp + dest + jump;

                    hackWriter.write(assembledInstruction + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not write " + writePath);
        }
        System.out.printf("Finished translating %s%n", writePath);
    }

}
