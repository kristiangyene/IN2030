package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;


public class AspFloatLiteral extends AspAtom{
    double literal;

    AspFloatLiteral(int n){
        super(n);
    }

    static AspFloatLiteral parse(Scanner s){
        Main.log.enterParser("float literal");

        AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());
        afl.literal = s.curToken().floatLit;
        skip(s, TokenKind.floatToken);

        Main.log.leaveParser("float literal");
        return afl;
    }
    @Override
    void prettyPrint(){
        Main.log.prettyWrite("" + literal);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return new RuntimeFloatValue(literal);
    }
}
