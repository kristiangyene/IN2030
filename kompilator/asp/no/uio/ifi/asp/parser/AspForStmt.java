package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.parser.AspExpr;
import no.uio.ifi.asp.parser.AspSuite;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;
import java.util.Arrays;

public class AspForStmt extends AspCompoundStmt{
    AspSuite suite;
    AspName name;
    AspExpr expr;

    AspForStmt(int n) {
        super(n);
    }

    static AspForStmt parse(Scanner s){
        Main.log.enterParser("for stmt");

        AspForStmt afs = new AspForStmt(s.curLineNum());
        skip(s, forToken);
        afs.name = AspName.parse(s);
        skip(s, inToken);
        afs.expr = AspExpr.parse(s);
        skip(s, colonToken);
        afs.suite = AspSuite.parse(s);

        Main.log.leaveParser("for stmt");
        return afs;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyWrite("for ");
        name.prettyPrint();
        Main.log.prettyWrite(" in ");
        expr.prettyPrint();
        Main.log.prettyWrite(":");
        suite.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue n = name.eval(curScope);
        RuntimeValue v = expr.eval(curScope);
        
        String fixed = v.toString();
  
        //Removes the whitespaces and brackets from the list-string.
        fixed = fixed.replace("[", "");
        fixed = fixed.replace("]", "");
        fixed = fixed.replaceAll("\\s","");
        ArrayList<String> values = new ArrayList<String>(Arrays.asList(fixed.split(",")));
        for(String s: values){
            try{
                RuntimeIntValue riv = new RuntimeIntValue(Integer.parseInt(s));
                curScope.assign(name.name, riv);
                suite.eval(curScope);
            }catch (NumberFormatException nfe){
                RuntimeStringValue rsv = new RuntimeStringValue(s);
                curScope.assign(name.name, rsv);
                suite.eval(curScope);
            }
        }
		return n;
    }
}

