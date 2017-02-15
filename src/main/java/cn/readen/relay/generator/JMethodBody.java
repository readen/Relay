package cn.readen.relay.generator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Method body
 */
public final class JMethodBody implements JPrintable {

    private List<String> list = new ArrayList<String>();

    /**
     * Append line to body
     *
     * @param str Line
     */
    public JMethodBody append(String str) {
        this.list.add(str);
        return this;
    }

    /**
     * Append line to body
     *
     * @param args Line
     */
    public JMethodBody append(String ... args) {
        for (String arg : args) {
            this.list.add(arg);
        }
        return this;
    }

    /**
     * Append line to body
     *
     * @param args Line
     */
    public JMethodBody append(Object ... args) {
        for (Object arg : args) {
            this.list.add(arg.toString());
        }
        return this;
    }

    @Override
    public void print(PrintStream out, int indent) {
        for (String str : this.list) {
            for (int i = 0; i < indent; i++) out.print(' ');
            out.println(str);
        }
    }
}
