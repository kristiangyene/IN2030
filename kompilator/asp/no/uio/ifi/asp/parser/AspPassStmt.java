package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspPassStmt extends AspSmallStmt{

    AspPassStmt(int n){
        super(n);
    }

    static AspPassStmt parse(Scanner s){
        Main.log.enterParser("pass stmt");

        AspPassStmt aps = new AspPassStmt(s.curLineNum());
        skip(s, TokenKind.passToken);

        Main.log.leaveParser("pass stmt");
        return aps;
    }

    @Override
    void prettyPrint(){
        Main.log.prettyWrite("pass");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        trace("pass");
        return null;
    }
}
