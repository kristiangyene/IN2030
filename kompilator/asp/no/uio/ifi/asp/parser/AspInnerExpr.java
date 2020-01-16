package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspInnerExpr extends AspAtom{
    AspExpr expr;

    AspInnerExpr(int n){
        super(n);
    }

    static AspInnerExpr parse(Scanner s){
        Main.log.enterParser("inner expr");

        AspInnerExpr aie = new AspInnerExpr(s.curLineNum());
        skip(s, TokenKind.leftParToken);
        aie.expr = AspExpr.parse(s);
        skip(s, TokenKind.rightParToken);

        Main.log.leaveParser("inner expr");
        return aie;
    }
    
    @Override
    void prettyPrint(){
        Main.log.prettyWrite("(");
        expr.prettyPrint();
        Main.log.prettyWrite(")");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return expr.eval(curScope);
    }
}
