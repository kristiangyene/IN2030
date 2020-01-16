package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspTerm extends AspSyntax{
	ArrayList<AspFactor> factorTests = new ArrayList<>();
	ArrayList<AspTermOpr> termOprTests = new ArrayList<>();

	AspTerm(int n){
		super(n);
	}

	static AspTerm parse(Scanner s){
		Main.log.enterParser("term");

		AspTerm at = new AspTerm(s.curLineNum());
		while (true){
			at.factorTests.add(AspFactor.parse(s));
			if(s.curToken().kind == plusToken || s.curToken().kind == minusToken) at.termOprTests.add(AspTermOpr.parse(s));
			else break;
		}
		
		Main.log.leaveParser("term");
		return at;
	}

	@Override
	void prettyPrint(){
		int i = 0;
		int counter = 0;
		for (AspFactor ant: factorTests){
			if(i > 0){
				if(!termOprTests.isEmpty()){
					AspTermOpr hi = termOprTests.get(counter);
					hi.prettyPrint();
					counter++;
				}
			}
			i++;
			ant.prettyPrint();
		}
	}

	@Override
	RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
		RuntimeValue v = factorTests.get(0).eval(curScope);
		RuntimeValue next = null;

		for (int i = 1; i < factorTests.size(); ++i){
			next = factorTests.get(i).eval(curScope);

			RuntimeValue orig = v;
			RuntimeValue origNext = next;

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

			//Trengte denne pga en feil der RuntimeStringValue blir brukt til oppslag i RuntimeScope.
			//Kunne muligens vaert en bedre losning her, men det fungerer.
			if((v instanceof RuntimeStringValue && next instanceof RuntimeIntValue)){
				next = origNext;
			}
			else if((v instanceof RuntimeIntValue && next instanceof RuntimeStringValue)){
				v = orig;
			}


			switch (termOprTests.get(i-1).termOpr){
				case minusToken:
					v = v.evalSubtract(next, this); 
					break;
				case plusToken:
				
					
					v = v.evalAdd(next, this); 
					break;
				default:
				Main.panic("Term operator: " + termOprTests.get(i-1).termOpr + "is not valid!");
			}
		}

		return v;
	}
}