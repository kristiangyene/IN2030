package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;


public class AspBooleanLiteral extends AspAtom{
    boolean bool;
    String stringBool; //For prettyprint
    
    AspBooleanLiteral(int n){
        super(n);
    }

    static AspBooleanLiteral parse(Scanner s){
        Main.log.enterParser("boolean literal");

        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());
        abl.stringBool = s.curToken().kind.toString();
        if(abl.stringBool == "True"){
            abl.bool = true;
        }else{
            abl.bool = false;
        }
        skip(s, s.curToken().kind);

        Main.log.leaveParser("boolean literal");
        return abl;
    }

    @Override
    void prettyPrint(){
        Main.log.prettyWrite(stringBool);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return new RuntimeBoolValue(bool);
    }
}