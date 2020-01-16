package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspCompOpr extends AspSyntax{
    TokenKind kind;
    
    AspCompOpr(int n) {
        super(n);
    }

    static AspCompOpr parse(Scanner s){
        Main.log.enterParser("comp opr");

        AspCompOpr aco = new AspCompOpr(s.curLineNum());
        aco.kind = s.curToken().kind;
        skip(s, s.curToken().kind);

        Main.log.leaveParser("comp opr");
        return aco;
    }
    
    @Override
    void prettyPrint(){
        Main.log.prettyWrite(" " + kind.toString() + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}