package cn.readen.relay.generator;

import java.io.PrintStream;

/**
 * Created by readen on 2017/2/14.
 */
public  enum Modifier {
    PUBLIC,
    PRIVATE,
    PROTECTED;

    public void print(PrintStream out ){
        if(this==Modifier.PUBLIC){
            out.print("public ");
        }else if(this==Modifier.PRIVATE){
            out.print("private ");
        }else {
            out.print("protected ");
        }
    }
}
