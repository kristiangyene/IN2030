
package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

import no.uio.ifi.asp.scanner.*;

class AspComparison extends AspSyntax{
	ArrayList<AspTerm> termTests = new ArrayList<>();
	ArrayList<AspCompOpr> compOprTests = new ArrayList<>();

	AspComparison(int n){
		super(n);
	}

	static AspComparison parse(Scanner s){
		AspComparison acp = new AspComparison(s.curLineNum());
		Main.log.enterParser("comparison");
		while (true) {
			acp.termTests.add(AspTerm.parse(s));

			TokenKind kind = s.curToken().kind;
			//Check for comparison tokens.
			if(kind == lessToken || kind == greaterToken ||
			kind == doubleEqualToken || kind == greaterEqualToken ||
			kind == lessEqualToken || kind == notEqualToken){
				acp.compOprTests.add(AspCompOpr.parse(s));
			}else break;
		}

		Main.log.leaveParser("comparison");
		return acp;
	}


	@Override
	void prettyPrint(){
        int counter = 0;
        for(AspTerm term: termTests){
            term.prettyPrint();
            if(counter < compOprTests.size()) compOprTests.get(counter++).prettyPrint();
        }
    }

	@Override
	RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
		RuntimeValue v = termTests.get(0).eval(curScope);
		RuntimeValue next = null;

		for (int i = 1; i < termTests.size(); ++i) {
			v = termTests.get(i-1).eval(curScope);
			next = termTests.get(i).eval(curScope);

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

			//Evaluate term
			switch (compOprTests.get(i-1).kind){
				case greaterEqualToken:
					v = v.evalGreaterEqual(next, this); 
					break;
				case lessEqualToken:
					v = v.evalLessEqual(next, this); 
					break;
				case notEqualToken:
					v = v.evalNotEqual(next, this); 
					break;
				case lessToken:
					v = v.evalLess(next, this); 
					break;
				case greaterToken:
					v = v.evalGreater(next, this); 
					break;
				case doubleEqualToken:
					v = v.evalEqual(next, this); 
					break;
				default:
				Main.panic("Term operator: " + compOprTests.get(i-1).kind + " is not valid!");
			}
			if(!v.getBoolValue("and operator", this)) return v;
		}
		return v;
	}
}