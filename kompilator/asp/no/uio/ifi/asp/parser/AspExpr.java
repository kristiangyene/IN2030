package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExpr extends AspSyntax{
    ArrayList<AspAndTest> andTests = new ArrayList<>();

    AspExpr(int n){
        super(n);
    }

    public static AspExpr parse(Scanner s){
	Main.log.enterParser("expr");
    
	AspExpr ae = new AspExpr(s.curLineNum());
	while(true){
        //Parse and until or.
        ae.andTests.add(AspAndTest.parse(s));
        if(s.curToken().kind != orToken) break;
        skip(s, orToken);
    }

	Main.log.leaveParser("expr");
	return ae;
    }


    @Override
    public void prettyPrint(){
        int counter = 0;
        for (AspAndTest aat: andTests){
            if (counter > 0){
                Main.log.prettyWrite(" or ");
            }
            aat.prettyPrint();
            ++counter;
        }
      }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    RuntimeValue v = andTests.get(0).eval(curScope);
        for(int i = 1 ; i < andTests.size(); ++i){
            if(v.getBoolValue("or operand", this)) return v;
            v = andTests.get(i).eval(curScope);
        }
        return v;
    }
}
