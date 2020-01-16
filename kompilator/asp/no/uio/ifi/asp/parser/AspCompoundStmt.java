package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspWhileStmt;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;


public abstract class AspCompoundStmt extends AspStmt{

    AspCompoundStmt(int n){
        super(n);
    }

    static AspCompoundStmt parse(Scanner s){
        Main.log.enterParser("compound stmt");

        AspCompoundStmt acs = null;
        switch (s.curToken().kind) {
            case leftParToken:
            case leftBraceToken:
            case leftBracketToken:
            case trueToken:
            case falseToken:
            case notToken:
            case floatToken:
            case minusToken:
            case integerToken:
            case stringToken:
            case nameToken:
            case noneToken:
            case passToken:
            case returnToken:
            case forToken:
                acs = AspForStmt.parse(s);
                break;
            case whileToken:
                acs = AspWhileStmt.parse(s); 
                break;
            case ifToken:
                acs = AspIfStmt.parse(s); 
                break;
            case defToken:
                acs = AspFuncDef.parse(s); 
                break;
            default:
                parserError("Expected an compound stmt but found a " + s.curToken().kind + "!", s.curLineNum());
        }

        Main.log.leaveParser("compound stmt");
        return acs;
    }
}