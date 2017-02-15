package cn.readen.relay.generator;

import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

/**
 * Annotation
 */
public final class JAnnotation implements JPrintable {

    private final String name;

    private final List<String> names = new ArrayList<String>();
    private final List<Object> values = new ArrayList<Object>();

    /**
     * Create annotation
     *
     * @param name Name
     */
    public JAnnotation(String name) {
        this.name = name;
    }

    /**
     * Add item
     *
     * @param name  Name
     * @param value Value
     * @return This
     */
    public JAnnotation add(String name, Object value) {
        if (value != null) {
            this.names.add(name);
            this.values.add(value);
        }
        return this;
    }

    @Override
    public void print(PrintStream out, int indent) {
        for (int i = 0; i < indent; i++) out.print(' ');
        out.print("@");
        out.print(this.name);
        // (
        if (this.names.size() > 0) {
            out.print("(");
            for (int i = 0; i < this.names.size(); i++) {
                String name = this.names.get(i);
                Object value = this.values.get(i);
                // ,
                if (i != 0) out.print(", ");
                // name
                if (!"".equals(name)) {
                    out.print(name);
                    out.print(" = ");
                }
                // value
                if (value instanceof String) {
                    // string
                    out.print('"');
                    out.print(value);
                    out.print('"');
                } else if (value instanceof String[]) {
                    // string[]
                    out.print("{");
                    int cnt = 0;
                    for (String str : (String[]) value) {
                        if (cnt != 0) out.print(", ");
                        out.print('"');
                        out.print(str);
                        out.print('"');
                        cnt++;
                    }
                    out.print("}");
                } else if (value instanceof JAnnotation[]) {
                    // string[]
                    out.print("{");
                    int cnt = 0;
                    for (JAnnotation annotation : (JAnnotation[]) value) {
                        if (cnt != 0) out.print(", ");
                        annotation.print(out, 0);
                        cnt++;
                    }
                    out.print("}");
                } else if (value.getClass().isEnum()) {
                    // out enum value
                    Class c = value.getClass();
                    out.print(c.getName());
                    out.print('.');
                    out.print(value);
                } else {
                    // other
                    out.print(value);
                }
            }
            // )
            out.print(")");
        }
    }
}
