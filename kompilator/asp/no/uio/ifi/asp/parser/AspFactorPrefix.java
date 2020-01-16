package no.uio.ifi.asp.parser;

import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;

public class AspFactorPrefix extends AspSyntax{
    no.uio.ifi.asp.scanner.TokenKind prefix;

    AspFactorPrefix(int n) {
        super(n);
    }

    static AspFactorPrefix parse(Scanner s){
        Main.log.enterParser("factor prefix");

        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());
        afp.prefix = s.curToken().kind;
        skip(s, s.curToken().kind);

        Main.log.leaveParser("factor prefix");
        return afp;
    }

    @Override
    void prettyPrint(){
        Main.log.prettyWrite(" " + prefix.toString() + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return null;
    }


}
