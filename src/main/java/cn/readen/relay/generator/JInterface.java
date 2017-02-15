package cn.readen.relay.generator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface
 */
public final class JInterface implements JPrintable {

    private final String aPackage;
    private final String name;
    private boolean Final;

    private final List<JImplements> aImplements = new ArrayList<>();
    private final List<JAnnotation> annotations = new ArrayList<>();
    private final List<JMethodDeclaration> methods = new ArrayList<>();
    private final List<JComment> comments = new ArrayList<>();

    /**
     * Create interface
     *
     * @param name Name
     * @param aPackage Package
     */
    public JInterface(String name, String aPackage) {
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

    public JInterface add(JAnnotation $) {
        this.annotations.add($);
        return this;
    }

    public JInterface add(JMethodDeclaration $) {
        this.methods.add($);
        return this;
    }

    public JInterface add(JImplements $) {
        this.aImplements.add($);
        return this;
    }

    public JInterface add(JComment $) {
        this.comments.add($);
        return this;
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

        // comments
        for (JComment comment : comments) {
            comment.print(out, 0);
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
        out.print("interface ");
        out.print(this.name);
        out.print(" ");

        // implements
        if (this.aImplements.size() > 0) {
            out.print("implements ");
            for (int i = 0; i < this.aImplements.size(); i++) {
                if (i != 0) out.print(", ");
                this.aImplements.get(i).print(out, 0);
            }
            out.print(" ");
        }

        // {
        out.println("{");

        // methods
        out.println();
        for (JMethodDeclaration method : this.methods) {
            method.print(out, 2);
        }

        // end
        out.println("}");

    }

}
