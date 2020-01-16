package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt{
    ArrayList<AspExpr> exprTests =new ArrayList<>();
    ArrayList<AspSuite> suiteTests = new ArrayList<>();
    //For prettyprint.
    boolean elseBool = false;
    boolean ElifBool = false;

    AspIfStmt(int n){
        super(n);
    }

    static AspIfStmt parse(Scanner s){
        Main.log.enterParser("if stmt");

        AspIfStmt ais = new AspIfStmt(s.curLineNum());
        skip(s, TokenKind.ifToken);
        while(true){
            //Parse suite Until there are no more elif tokens.
            ais.exprTests.add(AspExpr.parse(s));
            skip(s,TokenKind.colonToken);
            ais.suiteTests.add(AspSuite.parse(s));
            //If there is an else, break after parsing. 
            if(s.curToken().kind == TokenKind.elseToken){
                skip(s, TokenKind.elseToken);
                skip(s, TokenKind.colonToken);
                ais.suiteTests.add(AspSuite.parse(s));
                ais.elseBool = true;
                break;
            //Keep looping if elifToken.
            }else if(s.curToken().kind == TokenKind.elifToken){
                skip(s, TokenKind.elifToken);
                ais.ElifBool = true;
            }else{
                break;
            }
        }

        Main.log.enterParser("if stmt");
        return ais;
    }

    @Override
    void prettyPrint(){
        int counter = 0;
        Main.log.prettyWrite("if ");
        while(counter < exprTests.size()){
            exprTests.get(counter).prettyPrint();
            Main.log.prettyWrite(":");
            suiteTests.get(counter).prettyPrint();
            //See if there is an else, and if its the last one.
            if(elseBool && (exprTests.size() - 1 == counter)){
                Main.log.prettyWrite("else:");
                suiteTests.get(counter+1).prettyPrint();
                ElifBool = false;
            }
            //Check if there is an elifToken.
            if(ElifBool && exprTests.size() > counter) Main.log.prettyWrite("else if");
            counter++;
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue v = null;
		for(int i = 0; i < exprTests.size(); i++){
			v = exprTests.get(i).eval(curScope);
			if(v.getBoolValue("if statement", this)){
				v = suiteTests.get(i).eval(curScope);
				return v;
			}
		}
		if(elseBool){
			trace("else:");
			v = suiteTests.get(suiteTests.size()-1).eval(curScope);
		}
		return v;
    }
}
