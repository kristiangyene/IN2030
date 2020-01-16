package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;


//Deler tatt fra forelesning (19.09)
public class AspWhileStmt extends AspCompoundStmt{
    AspExpr expr;
    AspSuite suite;
    AspWhileStmt(int n){
        super(n);
    }

    static AspWhileStmt parse(Scanner s){
        Main.log.enterParser("while stmt");

        AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
        skip(s, whileToken);
        aws.expr = AspExpr.parse(s);
        skip(s, colonToken);
        aws.suite = AspSuite.parse(s);

        Main.log.leaveParser("while stmt");
        return aws;
    }

    @Override
    void prettyPrint(){
        Main.log.prettyWrite("while ");
        expr.prettyPrint();
        Main.log.prettyWrite(":");
        suite.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        while (true){
            RuntimeValue v = expr.eval(curScope);
            if (! v.getBoolValue("while loop test", this)) break;
            trace("while True:");
            suite.eval(curScope);
        }
        trace("while False:");
        return null;
    }
}
