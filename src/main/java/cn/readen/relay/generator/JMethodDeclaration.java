package cn.readen.relay.generator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Method Declaration
 */
public final class JMethodDeclaration implements JPrintable {

    private final String name;
    private final String type;

    private JDoc doc = null;
    private final List<JComment> comments = new ArrayList<>();
    private final List<JAnnotation> annotations = new ArrayList<>();
    private final List<JArg> args = new ArrayList<>();
    private final List<JThrow> aThrows = new ArrayList<>();


    public JMethodDeclaration(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public JMethodDeclaration add(JDoc doc) {
        this.doc = doc;
        return this;
    }

    public JMethodDeclaration add(JAnnotation annotation) {
        this.annotations.add(annotation);
        return this;
    }

    public JMethodDeclaration add(JComment comment) {
        this.comments.add(comment);
        return this;
    }

    public JMethodDeclaration add(JArg argument) {
        this.args.add(argument);
        return this;
    }

    public JMethodDeclaration add(JThrow name) {
        this.aThrows.add(name);
        return this;
    }

    @Override
    public void print(PrintStream out, int indent) {
        // comments
        for (JComment comment : comments) {
            comment.print(out, indent);
        }

        // doc
        if (doc != null) doc.print(out, indent);

        // annotations
        for (JAnnotation annotation : annotations) {
            annotation.print(out, indent);
            out.println();
        }

        // indent
        for (int i = 0; i < indent; i++) out.print(' ');

        // name
        out.print(this.type);
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
        }

        // ;
        out.println(";");
        out.println();
    }
}
