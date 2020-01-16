package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspSubscription extends AspPrimarySuffix{
    AspExpr expr;

    AspSubscription(int n){
        super(n);
    }

    static AspSubscription parse(Scanner s){
        Main.log.enterParser("subscription");

        AspSubscription as = new AspSubscription(s.curLineNum());
        skip(s, TokenKind.leftBracketToken);
        as.expr = AspExpr.parse(s);
        skip(s, TokenKind.rightBracketToken);

        Main.log.leaveParser("subscription");
        return as;
    }
    @Override
    void prettyPrint(){
        Main.log.prettyWrite("[");
        expr.prettyPrint();
        Main.log.prettyWrite("]");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return expr.eval(curScope);
    }
}
