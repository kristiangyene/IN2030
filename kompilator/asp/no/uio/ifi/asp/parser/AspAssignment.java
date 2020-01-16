package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAssignment extends AspSmallStmt{
    ArrayList<AspSubscription> subTests = new ArrayList<>();
    AspName name;
    AspExpr expr;

    AspAssignment(int n) {
        super(n);
    }

    public static AspAssignment parse(Scanner s){
        Main.log.enterParser("assignment");

        AspAssignment aa = new AspAssignment(s.curLineNum());
        aa.name = AspName.parse(s);
        while(true){
            //check if its a subscription.
            if (s.curToken().kind == TokenKind.leftBracketToken) aa.subTests.add(AspSubscription.parse(s));
            else break;
        }
        skip(s,TokenKind.equalToken);
        aa.expr = AspExpr.parse(s);

        Main.log.leaveParser("assignment");
       return aa;
    }

    @Override
    void prettyPrint(){
        name.prettyPrint();
        for(AspSubscription sub: subTests){
            sub.prettyPrint();
        }
        Main.log.prettyWrite(" = ");
        expr.prettyPrint();
        Main.log.prettyWriteLn();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue vName = name.eval(curScope);
        RuntimeValue helper = null;
        boolean severalSub = false;
        
		for(int i = 0; i < subTests.size()-1; i++){
			if(i == 0){
                RuntimeValue temp = curScope.find(name.name, this);
				helper = temp.evalSubscription(subTests.get(i).eval(curScope), this);
				severalSub = true;
			}else{
				helper = helper.evalSubscription(subTests.get(i).eval(curScope), this);
			}
        }
        //Check for subscriptions:
		if(!subTests.isEmpty()){
			RuntimeValue orig = subTests.get(subTests.size()-1).eval(curScope);
            RuntimeValue temp = curScope.find(orig.toString(), this);
            
            
            if(severalSub == false){
                helper = curScope.find(name.name, this);
			}

            if(temp != null){
                RuntimeValue p = expr.eval(curScope);
                RuntimeValue potentialValue = curScope.find(p.toString(), this);
                if(potentialValue == null) helper.evalAssignElem(temp, p, this);
                else helper.evalAssignElem(temp, potentialValue, this);
            }
			 else{
                RuntimeValue p = expr.eval(curScope);
                RuntimeValue potentialValue = curScope.find(p.toString(), this);
                if(potentialValue == null) helper.evalAssignElem(orig, p, this);
                else helper.evalAssignElem(orig, potentialValue, this);
             }
             	
		}else{
            //Find out if element in curscope already exist
            RuntimeValue p = expr.eval(curScope);
            RuntimeValue potentialValue = curScope.find(p.toString(), this);

          

            //Assigning elements
			if(potentialValue == null){
                curScope.assign(name.name, p);
				trace(name.name + " : " + p.showInfo());
            }
            else{
                curScope.assign(name.name, potentialValue);
				trace(name.name + " : " + potentialValue.showInfo());
            }
        }
		return vName;
    }
}