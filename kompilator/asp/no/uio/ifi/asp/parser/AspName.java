package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspName extends AspAtom{
    public String name;

    AspName(int n) {
        super(n);
    }

    public static AspName parse(Scanner s){
        Main.log.enterParser("name");

        AspName an = new AspName(s.curLineNum());
        an.name = s.curToken().name;
        skip(s, TokenKind.nameToken);

        Main.log.leaveParser("name");
        return an;
    }
    @Override
    void prettyPrint(){
        Main.log.prettyWrite("" + name);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return new RuntimeStringValue(name);
    }
}
