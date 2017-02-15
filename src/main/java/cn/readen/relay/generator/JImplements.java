package cn.readen.relay.generator;

import java.io.PrintStream;

/**
 * Implements
 */
public final class JImplements implements JPrintable {

    private String name;

    public JImplements(String name) {
        this.name = name;
    }

    @Override
    public void print(PrintStream out, int indent) {
        out.print(this.name);
    }
}
