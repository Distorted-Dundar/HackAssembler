package org.nand2tetris;

/**
 * The parser interface that enables you to call methods that interpret hack Assembly.
 */
public interface AssemblerParser {
    /**
     * Are there more commands in the input?
     */
    boolean hasMoreLines();

    /**
     * Reads the next command from
     * the input and makes it the current command. <br> Should be called only
     * if <code>hasMoreCommands()</code> is true. Initially there is no current command.
     */
    void advance();

    /**
     * Returns the type of the current command:
     * <ul>
     *     <li>
     *       A_COMMAND for @Xxx where Xxx is either a symbol or a decimal number
     *     </li>
     *     <li>
     *          C_COMMAND for dest=comp;jump
     *     </li>
     *     <li>
     *         L_COMMAND (actually, pseudo- command) for (Xxx) where Xxx is a symbol.
     *     </li>
     * </ul>
     */
    Command instructionType();

    /**
     * Returns the symbol or decimal Xxx of the current command @Xxx or (Xxx). <br>
     * Should be called only when commandType() is A_COMMAND or L_COMMAND.
     */
    String symbol();

    /**
     * Returns the dest mnemonic in
     * the current C-command (8 possibilities).  <br>
     * Should be called only
     * when commandType() is C_COMMAND.
     **/
    String dest();

    /**
     * Returns the comp mnemonic in the current C-command (28 possibilities).  <br>
     * Should be called only when commandType() is C_COMMAND.
     */
    String comp();


    /**
     * Returns the jump mnemonic in the current C-command (8 possibilities).  <br>
     * Should be called only when commandType() is C_COMMAND.
     */
    String jump();

    /**
     * Returns the memory address of a symbol according to what it has in the Symbol Table
     */
     int getAddress(String symbol);
}
