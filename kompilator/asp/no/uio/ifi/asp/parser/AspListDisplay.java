package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspListDisplay extends AspAtom{
    ArrayList<AspExpr> exprTests = new ArrayList<>();

    AspListDisplay(int n){
        super(n);
    }

    static AspListDisplay parse(Scanner s){
        Main.log.enterParser("list display");

        AspListDisplay ald = new AspListDisplay(s.curLineNum());
        skip(s, TokenKind.leftBracketToken);
        while(true){
            //looping until scanner finds right bracket.
            if(s.curToken().kind != TokenKind.rightBracketToken){
                ald.exprTests.add(AspExpr.parse(s));
                //Skips comma.
                if(s.curToken().kind == TokenKind.commaToken){
                    skip(s, TokenKind.commaToken);
                }else break;
            }else break;
        }
        skip(s, TokenKind.rightBracketToken);

        Main.log.leaveParser("list display");
        return ald;
    }

    @Override
    void prettyPrint(){
        Main.log.prettyWrite("[");
        int counter = 0;
        for(AspExpr expr: exprTests){
            expr.prettyPrint();
            if(counter<exprTests.size()-1){
                Main.log.prettyWrite(", ");
                counter++;
            }
        }
        Main.log.prettyWrite("]");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        ArrayList<RuntimeValue> rtValues = new ArrayList<>();
		RuntimeListValue rtListValues = new RuntimeListValue();
		for(int i = 0; i < exprTests.size(); i++) rtValues.add(exprTests.get(i).eval(curScope));
		for(RuntimeValue value : rtValues) rtListValues.addElem(value);
		return rtListValues;
    }
}
