package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspAndTest extends AspSyntax{
    ArrayList<AspNotTest> notTests = new ArrayList<AspNotTest>();
    
    AspAndTest(int n){
        super(n);
    }

    static AspAndTest parse(Scanner s){
        Main.log.enterParser("and test");
        
        AspAndTest aat = new AspAndTest(s.curLineNum());
        while(true){
            aat.notTests.add(AspNotTest.parse(s));
            if(s.curToken().kind != andToken) break;
            skip(s, andToken);
        }

        Main.log.leaveParser("and test");
        return aat;
    }

    @Override
    void prettyPrint(){
        int counter = 0;
        for(AspNotTest test: notTests){
            if(counter > 0) Main.log.prettyWrite(" and ");
            test.prettyPrint();
            counter++;
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue v = notTests.get(0).eval(curScope);
        for (int i = 1;  i < notTests.size();  ++i) {
            if (! v.getBoolValue("and operand", this)) return v;
            v = notTests.get(i).eval(curScope);
        }
        return v; 
    }
}