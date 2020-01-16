package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspExprStmt extends AspSmallStmt{
    AspExpr expr;
    
    AspExprStmt(int n){
        super(n);
    }

    static AspExprStmt parse(Scanner s){
        Main.log.enterParser("expr stmt");

        AspExprStmt aes = new AspExprStmt(s.curLineNum());
        aes.expr = AspExpr.parse(s);

        Main.log.leaveParser("expr stmt");
        return aes;
    }

    @Override
    void prettyPrint(){
        expr.prettyPrint();
        Main.log.prettyWriteLn();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return expr.eval(curScope);
    }
}
