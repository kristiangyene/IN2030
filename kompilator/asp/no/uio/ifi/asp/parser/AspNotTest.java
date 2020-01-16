package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;


public class AspNotTest extends AspSyntax{
    AspComparison comp;
    boolean notBool = false;

    AspNotTest(int n) {
        super(n);
    }

    static AspNotTest parse(Scanner s){
        Main.log.enterParser("not test");

        AspNotTest ant = new AspNotTest(s.curLineNum());
        if(s.curToken().kind == TokenKind.notToken){
            skip(s, TokenKind.notToken);
            ant.notBool = true;
        }
        ant.comp = AspComparison.parse(s);

        Main.log.leaveParser("not test");
        return ant;
    }
    
    @Override
    void prettyPrint() {
        if(notBool) Main.log.prettyWrite(" not ");
        comp.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue v = comp.eval(curScope);
        if(notBool){
            v = v.evalNot(this);
        }
        return v;
    }
}
