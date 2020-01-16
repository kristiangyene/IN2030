package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspFuncDef extends AspCompoundStmt{
    AspSuite suite;
    ArrayList<AspName> nameTests = new ArrayList<>();

    AspFuncDef(int n){
        super(n);
    }

    static AspFuncDef parse(Scanner s){
        Main.log.enterParser("func def");

        AspFuncDef afd = new AspFuncDef(s.curLineNum());
        skip(s, TokenKind.defToken);
        afd.nameTests.add(AspName.parse(s));
        skip(s,TokenKind.leftParToken);
        if(s.curToken().kind != TokenKind.rightParToken){
            while(true){
                //Parse name until there is no more commas.
                afd.nameTests.add(AspName.parse(s));
                if(s.curToken().kind == TokenKind.commaToken){
                    //Another name.
                    skip(s, TokenKind.commaToken);
                }else{
                    break;
                }
            }
        }
        skip(s,TokenKind.rightParToken);
        skip(s, TokenKind.colonToken);
        //Send to suite.
        afd.suite = AspSuite.parse(s);

        Main.log.leaveParser("func def");
        return afd;
    }

    @Override
    void prettyPrint(){
        Main.log.prettyWrite("def ");
		nameTests.get(0).prettyPrint();
		Main.log.prettyWrite(" (");

		int nPrinted = 0;

		for (int i = 1;i<nameTests.size();i++) {
			if(nPrinted > 0){
				Main.log.prettyWrite(", ");
			}
			nameTests.get(i).prettyPrint();
			++nPrinted;
		}

		Main.log.prettyWrite(")");
		Main.log.prettyWrite(":");
		suite.prettyPrint();
		Main.log.prettyWriteLn();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue v = nameTests.get(0).eval(curScope);
		ArrayList<RuntimeValue> rtvalues = new ArrayList<>();
		if(nameTests.size() > 1){
			for(int i = 1; i < nameTests.size() ; i++){
				RuntimeValue v2 = nameTests.get(i).eval(curScope);
                rtvalues.add(v2);
			}
		}
		RuntimeFunc rtf = new RuntimeFunc(v, rtvalues, curScope, this);
		curScope.assign(nameTests.get(0).name, rtf);
		trace("def " + v.showInfo());
		return v;
	}

	public RuntimeValue runFunction(RuntimeScope s)throws RuntimeReturnValue{
		return suite.eval(s);
	}
}
