package cn.readen.relay.generator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Constructor
 */
public final class JConstructor implements JPrintable {


    private final Modifier modifier;

    private String name;

    private JMethodBody methodBody;

    private final List<JComment> comments = new ArrayList<>();
    private final List<JAnnotation> annotations = new ArrayList<>();
    private final List<JArg> args = new ArrayList<>();
    private final List<JThrow> aThrows = new ArrayList<>();


    public JConstructor(Modifier modifier, String name) {
        this.modifier = modifier;
        this.name = name;
        this.methodBody = null;
    }

    public JConstructor(Modifier modifier, String name, JMethodBody body) {
        this.modifier = modifier;
        this.name = name;
        this.methodBody = body;
    }

    public JConstructor add(JAnnotation $) {
        this.annotations.add($);
        return this;
    }

    public JConstructor add(JComment $) {
        this.comments.add($);
        return this;
    }

    public JConstructor add(JArg $) {
        this.args.add($);
        return this;
    }

    public JConstructor add(JThrow $) {
        this.aThrows.add($);
        return this;
    }


    public JMethodBody getMethodBody() {
        return methodBody;
    }

    @Override
    public void print(PrintStream out, int indent) {
        // comments
        for (JComment comment : comments) {
            comment.print(out, indent);
        }

        // annotations
        for (JAnnotation annotation : annotations) {
            annotation.print(out, indent);
            out.println();
        }

        // indent
        for (int i = 0; i < indent; i++) out.print(' ');

        // name
        modifier.print(out);
        out.print(" ");
        out.print(this.name);

        // args
        out.print("(");
        for (int i = 0; i < this.args.size(); i++) {
            if (i != 0) out.print(", ");
            this.args.get(i).print(out, 0);
        }
        out.print(")");

        // throws
        if (this.aThrows.size() > 0) {
            out.print(" throws ");
            for (int i = 0; i < this.aThrows.size(); i++) {
                if (i != 0) out.print(", ");
                this.aThrows.get(i).print(out, 0);
            }
            out.print(" ");
        }

        // {
        out.println("{");

        // body
        if (this.methodBody != null)
            this.methodBody.print(out, indent + 2);

        // indent
        for (int i = 0; i < indent; i++) out.print(' ');
        // }
        out.println("}");
        out.println();
    }
}
