package cn.readen.relay.generator;

import java.io.PrintStream;

/**
 * Throw
 */
public final class JThrow implements JPrintable {

    private String name;

    public JThrow(String name) {
        this.name = name;
    }

    @Override
    public void print(PrintStream out, int indent) {
        out.print(this.name);
    }
}
