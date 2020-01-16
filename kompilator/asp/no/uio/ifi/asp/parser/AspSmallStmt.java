package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.parser.AspExprStmt;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public abstract class AspSmallStmt extends AspSyntax{

    AspSmallStmt(int n){
        super(n);
    }

    static AspSmallStmt parse(Scanner s){
        Main.log.enterParser("small stmt");

        AspSmallStmt as = null;
        //Check which tokenKind scanner is on.
        switch (s.curToken().kind){
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
            case forToken:
            case defToken:
            case whileToken:
            case ifToken:
            case semicolonToken:

            if(s.anyEqualToken()){
                as = AspAssignment.parse(s);
                break;
            }else{
                as = AspExprStmt.parse(s);
                break;
            }
            case minusToken:
                as = AspExprStmt.parse(s);
                break;
            case returnToken:
                as = AspReturnStmt.parse(s);
                break;
            case passToken:
                as = AspPassStmt.parse(s);
                break;
            default:
                parserError("Expected a small stmt but found a " + s.curToken().kind + "!", s.curLineNum());
        }

        Main.log.leaveParser("small stmt");
        return as;
    }
}
