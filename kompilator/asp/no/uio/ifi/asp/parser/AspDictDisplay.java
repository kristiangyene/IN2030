package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AspDictDisplay extends AspAtom{
    ArrayList<AspStringLiteral> sLiteralTests = new ArrayList<>();
    ArrayList<AspExpr> exprTests = new ArrayList<>();

    AspDictDisplay(int n){
        super(n);
    }

    static AspDictDisplay parse(Scanner s){
        Main.log.enterParser("dict display");
        AspDictDisplay add = new AspDictDisplay(s.curLineNum());
        skip(s, TokenKind.leftBraceToken);
        while(true){
            //Parse string until it hits a rightbrace, skip colon and comma.
            if(s.curToken().kind == TokenKind.stringToken){
                add.sLiteralTests.add(AspStringLiteral.parse(s));
                skip(s, TokenKind.colonToken);
                add.exprTests.add(AspExpr.parse(s));
                if(s.curToken().kind == TokenKind.commaToken){
                    skip(s, TokenKind.commaToken);
                }else break;
            }else break;
        }
        skip(s, TokenKind.rightBraceToken);
        Main.log.leaveParser("dict display");
        return add;
    }

    @Override
    void prettyPrint(){
        Main.log.prettyWrite("{");
        for(int i = 0; i < sLiteralTests.size(); i++){
            sLiteralTests.get(i).prettyPrint();
            Main.log.prettyWrite(": ");
            exprTests.get(i).prettyPrint();
            if(i < sLiteralTests.size()-1){
                Main.log.prettyWrite(", ");
            }
        }
        Main.log.prettyWrite("}");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        Map<RuntimeValue, RuntimeValue> rtValues = new HashMap<RuntimeValue, RuntimeValue>();
		RuntimeDictValue rtDictValues = new RuntimeDictValue();
        for(int i = 0; i < sLiteralTests.size(); i++){
            RuntimeValue key = sLiteralTests.get(i).eval(curScope);
            RuntimeValue value = exprTests.get(i).eval(curScope);
            rtValues.put(key, value);
        }
        for(Map.Entry<RuntimeValue, RuntimeValue> entry : rtValues.entrySet()){	
            rtDictValues.addElem(entry.getKey(), entry.getValue());
        }		
		return rtDictValues;
    }
}
