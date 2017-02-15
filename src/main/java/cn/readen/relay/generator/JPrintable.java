package cn.readen.relay.generator;

import java.io.PrintStream;

/**
 * Printable interface
 */
public interface JPrintable {

    /**
     * Print content
     *
     * @param out Stream
     * @param indent Indent (spaces)
     */
    void print(PrintStream out, int indent);
}
