package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;

public class AspIntegerLiteral extends AspAtom{
    long number;

    AspIntegerLiteral(int n){
        super(n);
    }
    static AspIntegerLiteral parse(Scanner s){
        Main.log.enterParser("integer literal");

        AspIntegerLiteral ail = new AspIntegerLiteral(s.curLineNum());
        ail.number = s.curToken().integerLit;
        skip(s, s.curToken().kind);

        Main.log.leaveParser("integer literal");
        return ail;
    }
    
    @Override
    void prettyPrint(){
        Main.log.prettyWrite("" + number);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return new RuntimeIntValue(number);
    }
}
