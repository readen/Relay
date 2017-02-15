package cn.readen.relay.generator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Argument
 */
public final class JArg implements JPrintable {

    private final String name;
    private final String type;
    private final List<JAnnotation> annotations = new ArrayList<>();
    private  JImport jImport;
    public JArg(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public JArg(String name,Class type){
        this(name,type.getSimpleName());
        jImport=new JImport(type);
    }

    public JImport getImport() {
        return jImport;
    }

    public JArg add(JAnnotation annotation) {
        this.annotations.add(annotation);
        return this;
    }

    @Override
    public void print(PrintStream out, int indent) {
        for (int i = 0; i < indent; i++) out.print(' ');
        for (JAnnotation annotation : annotations) {
            annotation.print(out, indent);
            out.print(' ');
        }
        out.print(this.type);
        out.print(" ");
        out.print(this.name);
    }

}
