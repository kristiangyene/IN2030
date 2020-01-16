package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspReturnStmt extends AspSmallStmt{
    AspExpr expr;
    int lineNr;

    AspReturnStmt(int n){
        super(n);
    }

    static AspReturnStmt parse(Scanner s){
        Main.log.enterParser("return stmt");

        AspReturnStmt ars = new AspReturnStmt(s.curLineNum());
        skip(s, TokenKind.returnToken);
        ars.lineNr = s.curLineNum();
        ars.expr = AspExpr.parse(s);

        Main.log.leaveParser("return stmt");
        return ars;
    }
    
    @Override
    void prettyPrint(){
        Main.log.prettyWrite("return ");
        expr.prettyPrint();
        Main.log.prettyWriteLn();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue v = expr.eval(curScope);
        RuntimeValue possible = curScope.find(v.toString(), this);
        //Check if the value already exist
        if(possible != null){
            trace("return " + possible.showInfo());
            throw new RuntimeReturnValue(possible, lineNr);
        }else{
            trace("return " + v.showInfo());
            throw new RuntimeReturnValue(v, lineNr);
        }
    }
}
