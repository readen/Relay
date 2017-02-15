package cn.readen.relay.generator;

import java.io.PrintStream;

/**
 * Field
 */
public final class JField implements JPrintable {

    Modifier modifier;
    private final boolean aStatic;
    private final String name;
    private final String type;
    private Object value;
    private  JImport jImport;

    private JDoc doc;
    private JComment comment;

    public JField(Modifier modifier, boolean aStatic, String name, String type, Object value) {
        this.modifier = modifier;
        this.aStatic = aStatic;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public JField(Modifier modifier, boolean aStatic, String name, Class type, Object value){
        this(modifier,aStatic,name,type.getSimpleName(),value);
        jImport=new JImport(type);
    }

    public JField(Modifier modifier, boolean aStatic, String name, String type) {
        this(modifier,aStatic,name,type,null);
    }

    public JField(Modifier modifier, boolean aStatic, String name, Class type){
        this(modifier,aStatic,name,type.getSimpleName(),null);
        jImport=new JImport(type);
    }
    /**
     * Add comment
     *
     * @param comment Comment
     * @return This
     */
    public JField add(JComment comment) {
        this.comment = comment;
        return this;
    }

    public JField add(JDoc doc) {
        this.doc = doc;
        return this;
    }

    public JImport getImport() {
        return jImport;
    }


    @Override
    public void print(PrintStream out, int indent) {

        // doc
        if (this.doc != null) this.doc.print(out, indent);

        // comment + indent
        if (this.comment != null) this.comment.print(out, indent);

        // indent
        for (int i = 0; i < indent; i++) out.print(' ');

        // self
        modifier.print(out);
        if (this.aStatic) out.print("static ");
        // type
        out.print(this.type);
        out.print(" ");
        // name
        out.print(this.name);
        if (this.value != null) {
            out.print(" = ");
            if (this.type.equals("String")) {
                out.print('"');
                out.print(this.value);
                out.print('"');
//            } if (this.type.equals("boolean")) {
//                out.print(this.value);
            } else {
                out.print(this.value);
            }
        }
        out.println(";");

    }

}
