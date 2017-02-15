package cn.readen.relay.generator;

import java.io.PrintStream;

/**
 * Comment
 */
public final class JComment implements JPrintable {

    private final String text;

    /**
     * Create comment
     *
     * @param text Text
     */
    public JComment(String text) {
        this.text = text;
    }

    @Override
    public void print(PrintStream out, int indent) {
        for (int i = 0; i < indent; i++) out.print(' ');
        out.print("// ");
        out.println(this.text);
    }

}
