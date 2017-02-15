package cn.readen.relay.generator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Doc
 */
public final class JDoc implements JPrintable {

    class kv {

        public String key;
        public String value;

        kv(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }


    private List<String> list = new ArrayList<String>();
    private List<kv> named = new ArrayList<kv>();

    public JDoc(String str) {
        this.list.add(str);
    }

    /**
     * Append line to body
     *
     * @param str Line
     */
    public JDoc append(String str) {
        this.list.add(str);
        return this;
    }

    public JDoc append(String key, String value) {
        this.named.add(new kv(key, value));
        return this;
    }

    public JDoc addParam(String name, String comment) {
        return append("@param", name + " " + comment);
    }

    public JDoc addReturn(String comment) {
        return append("@return", comment);
    }

    public JDoc addSee(String ref) {
        return append("@see", ref);
    }

    /**
     * Append line to body
     *
     * @param args Line
     */
    public JDoc append(String ... args) {
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
    public JDoc append(Object ... args) {
        for (Object arg : args) {
            this.list.add(arg.toString());
        }
        return this;
    }

    @Override
    public void print(PrintStream out, int indent) {
        for (int i = 0; i < indent; i++) out.print(' ');
        out.println("/**");
        for (String str : this.list) {
            for (int i = 0; i < indent; i++) out.print(' ');
            out.print(" * ");
            out.println(str);
        }
        if (this.named.size() > 0) {
            for (int i = 0; i < indent; i++) out.print(' ');
            out.println(" *");
            for (kv kv : this.named) {
                for (int i = 0; i < indent; i++) out.print(' ');
                out.print(" * ");
                out.print(kv.key);
                out.print(" ");
                out.println(kv.value);
            }
        }
        for (int i = 0; i < indent; i++) out.print(' ');
        out.println(" */");
    }
}
