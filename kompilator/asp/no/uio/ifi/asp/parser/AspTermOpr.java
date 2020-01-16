package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspTermOpr extends AspSyntax{
    TokenKind termOpr;

    AspTermOpr(int n){
        super(n);
    }

    static AspTermOpr parse(Scanner s){
        Main.log.enterParser("term opr");
        
        AspTermOpr ato = new AspTermOpr(s.curLineNum());
        ato.termOpr = s.curToken().kind;
        skip(s, ato.termOpr);

        Main.log.leaveParser("term opr");
        return ato;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyWrite(" " + termOpr + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return null;
    }


}
