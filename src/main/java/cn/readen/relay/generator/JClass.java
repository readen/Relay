package cn.readen.relay.generator;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class
 */
public final class JClass implements JPrintable {

    private final String aPackage;
    private final String name;
    private boolean Final;

    private String extend = null;
    private final List<String> aImplements = new ArrayList<>();

    public  final List<JImport> imports=new ArrayList<>();
    private final List<JAnnotation> annotations = new ArrayList<>();
    private final List<JConstructor> constructors = new ArrayList<>();
    private final List<JField> fields = new ArrayList<>();
    private final List<JMethod> methods = new ArrayList<>();
    private final List<JComment> comments = new ArrayList<>();
    private final List<JDoc> docs = new ArrayList<>();

    // props
    private final List<JProperty> properties = new ArrayList<>();
    private final Map<String, JProperty> name2property = new HashMap<>();

    private JMethodBody staticMethod = null;

    /**
     * Create class
     *
     * @param name Name
     * @param aPackage Package
     */
    public JClass(String name, String aPackage) {
        this.name = name;
        this.aPackage = aPackage;
    }

    /**
     * Get package name
     *
     * @return Package name
     */
    public String getPackageName() {
        return aPackage;
    }

    /**
     * Get class name (without package)
     *
     * @return Class name
     */
    public String getClassName() {
        return name;
    }


    public JClass add(JProperty item) {
        this.properties.add(item);
        this.name2property.put(item.getName(), item);
        return this;
    }

    public JClass add(JImport jImport){
        if(jImport!=null&&!imports.contains(jImport)){
            imports.add(jImport);
        }
        return this;
    }

    public JProperty getProperty(String name) {
        return this.name2property.get(name);
    }

    public JClass add(JDoc doc) {
        this.docs.add(doc);
        return this;
    }

    public JClass add(JField field) {
        this.fields.add(field);
        add(field.getImport());
        return this;
    }

    public JClass add(JAnnotation annotation) {
        this.annotations.add(annotation);
        return this;
    }

    public JClass add(JConstructor constructor) {
        this.constructors.add(constructor);
        return this;
    }

    public JClass add(JMethod method) {
        this.methods.add(method);
        for(JImport jImport:method.getImports()){
            add(jImport);
        }
        return this;
    }

    public JClass setExtends(String str) {
        this.extend = str;
        return this;
    }

    public JClass setExtends(Class type) {
        this.extend = type.getSimpleName();
        add(new JImport(type));
        return this;
    }

    public JConstructor getFirstConstructor() {
        if (this.constructors.size() == 0) return null;
        return this.constructors.get(0);
    }


    /**
     * Add implementation
     *
     * @param str Implementation interface
     * @return This
     */
    public JClass add(String str) {
        this.aImplements.add(str);
        return this;
    }

    /**
     * Add comment
     *
     * @param comment Comment
     * @return This
     */
    public JClass add(JComment comment) {
        this.comments.add(comment);
        return this;
    }

    public List<JProperty> getProperties() {
        return properties;
    }

    /**
     * Set static method
     *
     * @param method Method
     * @return This
     */
    public JClass setStatic(JMethodBody method) {
        this.staticMethod = method;
        return this;
    }

    public JMethodBody getStatic() {
        return staticMethod;
    }

    /**
     * Set FINAL flag
     *
     * @param value FINAL flag value
     */
    public void setFinal(boolean value) {
        this.Final = value;
    }

    @Override
    public void print(PrintStream out, int indent) {
        // header
        out.print("package ");
        out.print(this.aPackage);
        out.println(";");
        out.println();

        for(JImport jImport:imports){
            jImport.print(out,0);
        }

        // comments
        for (JComment comment : comments) {
            comment.print(out, 0);
        }

        // docs
        for (JDoc doc : docs) {
            doc.print(out, 0);
        }

        // annotations
        if (this.annotations.size() > 0) {
            for (JAnnotation annotation : annotations) {
                annotation.print(out, 0);
                out.println();
            }
        }

        // class
        out.print("public ");
        if (this.Final) {
            out.print("final ");
        }
        out.print("class ");
        out.print(this.name);
        out.print(" ");

        if (this.extend != null) {
            out.print("extends ");
            out.print(this.extend);
            out.print(" ");
        }

        // implements
        if (this.aImplements.size() > 0) {
            out.print("implements ");
            for (int i = 0; i < this.aImplements.size(); i++) {
                if (i != 0) out.print(", ");
                out.print(this.aImplements.get(i));
            }
            out.print(" ");
        }

        // {
        out.println("{");

        // constructors
        out.println();
        for (JConstructor constructor : constructors) {
            constructor.print(out, 2);
        }

        // fields
        for (JField field : fields) {
            field.print(out, 2);
            out.println();
        }
        for (JProperty property : this.properties) {
            property.getField().print(out, 2);
            out.println();
        }

        // methods
        out.println();
        for (JMethod method : this.methods) {
            method.print(out, 2);
        }
        for (JProperty property : this.properties) {
            property.getGet().print(out, 2);
            property.getSet().print(out, 2);
        }

        // static ?
        if (this.staticMethod != null) {
            out.println("  static {");
            this.staticMethod.print(out, 4);
            out.println("  }");
        }

        // end class
        out.println("}");

    }

}
