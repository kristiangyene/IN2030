package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

class AspFactor extends AspSyntax{
	ArrayList<AspFactorOpr> factorOprTests = new ArrayList<>();
	ArrayList<AspPrimary> primaryTests = new ArrayList<>();
	ArrayList<AspFactorPrefix> prefixTests = new ArrayList<>();

	AspFactor(int n){
		super(n);
	}

	static AspFactor parse(Scanner s){
		AspFactor af = new AspFactor(s.curLineNum());
		Main.log.enterParser("factor");
			while(true){
				if(s.curToken().kind == plusToken || s.curToken().kind == minusToken) af.prefixTests.add(AspFactorPrefix.parse(s));
				af.primaryTests.add(AspPrimary.parse(s));
				if(s.curToken().kind == astToken || s.curToken().kind == slashToken
				|| s.curToken().kind == percentToken || s.curToken().kind == doubleSlashToken){
					af.factorOprTests.add(AspFactorOpr.parse(s));
				}else{
					break;
				}
			}
		Main.log.leaveParser("factor");
		return af;
	}


	@Override
	void prettyPrint(){
		if(!prefixTests.isEmpty()) prefixTests.get(0).prettyPrint();
		if(primaryTests.size() == 1) primaryTests.get(0).prettyPrint();
		else{
			primaryTests.get(0).prettyPrint();
			for(int i = 1; i< primaryTests.size(); i++){
				factorOprTests.get(i-1).prettyPrint();
				primaryTests.get(i).prettyPrint();
			}
		}
	}


	@Override
	RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
		RuntimeValue v = primaryTests.get(0).eval(curScope);
		RuntimeValue next = null;

		for (int i = 1; i < primaryTests.size(); ++i){
			next = primaryTests.get(i).eval(curScope);
			RuntimeValue possible = curScope.find(v.toString(), this);
			RuntimeValue nextPossible = curScope.find(next.toString(), this);

			//If they are not null, they get new value
			if((possible != null) && (nextPossible != null)){
				v = possible;
				next = nextPossible;
			}
			else if((possible == null) && (nextPossible != null)){
				next = nextPossible;
			}else if((possible != null) && (nextPossible == null)){
				v = possible;
			}

			switch (factorOprTests.get(i-1).opr){
				case astToken:
					v = v.evalMultiply(next, this);
					break;
				case slashToken:
					v = v.evalDivide(next, this); 
					break;
				case percentToken:
					v = v.evalModulo(next, this); 
					break;
				case doubleSlashToken:
					v = v.evalIntDivide(next, this); 
					break;
				default:
				Main.panic("Operator: " + factorOprTests.get(i-1).opr + " is not valid!");
			}
		}

		if(!prefixTests.isEmpty()){

			switch(prefixTests.get(0).prefix){
				case minusToken:
					v = v.evalNegate(this); 
					break;

				case plusToken:
					v = v.evalPositive(this); break;
					default:
				Main.panic("Prefix: " + prefixTests.get(0).prefix + " is not valid!");
			}
		}
		return v;
	}
}