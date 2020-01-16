package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspArguments extends AspPrimarySuffix{
    ArrayList<AspExpr> exprTests = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    static AspArguments parse(Scanner s){
        Main.log.enterParser("arguments");

        AspArguments aa = new AspArguments(s.curLineNum());
        skip(s,leftParToken);
        while(true){
            //Parse all the arguments until s hits a rightpar.
            if(s.curToken().kind != rightParToken){
                aa.exprTests.add(AspExpr.parse(s));
                //If it hits a comma, break and check again for expr.
                if(s.curToken().kind == commaToken){
                    skip(s, s.curToken().kind);
                }else break;
                //Rightpar hit.
            }else break;
        }
        skip(s, rightParToken);

        Main.log.leaveParser("arguments");
        return aa;
    }
    @Override
    void prettyPrint(){
        Main.log.prettyWrite("(");
        int counter = 0;
        for(AspExpr expr: exprTests){
            if(counter > 0){
                Main.log.prettyWrite(", ");
            }
            expr.prettyPrint();
            counter++;
        }
        Main.log.prettyWrite(")");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeListValue v = new RuntimeListValue();
		for(int i = 0 ; i < exprTests.size(); ++i){
            RuntimeValue v2 = exprTests.get(i).eval(curScope);
			RuntimeValue v3 = curScope.find(v2.toString(), this);
			if(v3 == null) v.addElem(v2);
			else v.addElem(v3);
		}
		return v;
    }
}