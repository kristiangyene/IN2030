package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspFactorOpr extends AspSyntax{
    TokenKind opr;
    AspFactorOpr(int n) {
        super(n);
    }
    public static AspFactorOpr parse(Scanner s){
        Main.log.enterParser("factor opr");

        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());
        afo.opr = s.curToken().kind;
        skip(s,s.curToken().kind);

        Main.log.leaveParser("factor opr");
        return afo;
    }
    @Override
    void prettyPrint(){
        Main.log.prettyWrite(" " + opr + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return null;
    }


}
