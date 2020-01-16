package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public abstract class AspStmt extends AspSyntax {

    AspStmt(int n) {
        super(n);
    }
    
    static AspStmt parse(Scanner s){
        Main.log.enterParser("stmt");
        AspStmt as = null;

        //Check which tokenKind scanner is on.
        switch (s.curToken().kind) {
            case leftParToken:
            case leftBraceToken:
            case leftBracketToken:
            case trueToken:
            case falseToken:
            case notToken:
            case floatToken:
            case integerToken:
            case stringToken:
            case nameToken:
            case noneToken:
            case elseToken:
            case semicolonToken:
        
            //Send to AspSmallStmtList.
            if(s.anyEqualToken()){
                as = AspSmallStmtList.parse(s);
                break;
            }else{
                as = AspSmallStmtList.parse(s);
                break;
            }
            case returnToken:
                as = AspSmallStmtList.parse(s);
                break;
            case passToken:
                as = AspSmallStmtList.parse(s);
                break;
            
                case minusToken:
                as = AspSmallStmtList.parse(s);
                break;

            //Send to AspCompoundStmt.
            case ifToken:
                as = AspCompoundStmt.parse(s);
                break;
            case whileToken:
                as = AspCompoundStmt.parse(s);
                break;
            case defToken:
                as = AspCompoundStmt.parse(s);
                break;
            case forToken:
                as = AspCompoundStmt.parse(s);
                break;
            default:
                parserError("Expected a stmt but found a " + s.curToken().kind + "!", s.curLineNum());
        }

        Main.log.leaveParser("stmt");
        return as;
    }
}
