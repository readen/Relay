package cn.readen.relay.generator;

import java.io.PrintStream;

/**
 * Created by readen on 2017/2/14.
 */
public class JImport implements JPrintable{

    private  final String name;

    public JImport(String name){
        this.name=name;
    }

    public JImport(Class type){
        name=type.getName();
    }

    @Override
    public void print(PrintStream out, int indent) {
        out.println("import "+name+";");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof JImport){
            return  ((JImport) obj).name.equals(this.name);
        }else {
            return false;
        }
    }
}
