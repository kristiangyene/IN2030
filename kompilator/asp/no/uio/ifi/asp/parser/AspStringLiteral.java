package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspStringLiteral extends AspAtom{
    String literal;

    AspStringLiteral(int n){
        super(n);
    }

    static AspStringLiteral parse(Scanner s){
        Main.log.enterParser("string literal");

        AspStringLiteral asl = new AspStringLiteral(s.curLineNum());
        asl.literal = s.curToken().stringLit;
        skip(s, TokenKind.stringToken);

        Main.log.leaveParser("string literal");
        return asl;
    }

    @Override
    void prettyPrint(){
        Main.log.prettyWrite(literal);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        //Removes the string quotes.
        return new RuntimeStringValue(literal.substring(1, literal.length()-1));
    }
}
