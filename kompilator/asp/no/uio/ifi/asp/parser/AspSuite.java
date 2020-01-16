package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspSuite extends AspSyntax{
    ArrayList<AspStmt> as = new ArrayList<>();
    boolean checkDedent = false;
    boolean checkNewline = false;
    AspSmallStmtList stmt;
    AspSuite(int n) {
        super(n);
    }

    static AspSuite parse(Scanner s){
        Main.log.enterParser("suite");

        AspSuite aSuite = new AspSuite(s.curLineNum());
        if(s.curToken().kind == TokenKind.newLineToken){
            //Skip newline and indent.
            while(s.curToken().kind == TokenKind.newLineToken) skip(s,TokenKind.newLineToken); 
            while(s.curToken().kind == TokenKind.indentToken) skip(s, TokenKind.indentToken);
            while(true){
                //Skip newlines.
                if(s.curToken().kind == TokenKind.newLineToken) skip(s, TokenKind.newLineToken);
                //Token is a stmt.
                else if(s.curToken().kind != TokenKind.dedentToken
                        && s.curToken().kind != TokenKind.eofToken){
                    aSuite.as.add(AspStmt.parse(s));
                    aSuite.checkNewline = true;
                }else break;
            }
            //Skip dedent.
            if(s.curToken().kind == TokenKind.dedentToken){
                skip(s,TokenKind.dedentToken);
                aSuite.checkDedent = true;
            }
        }
        //Send to AspSmallStmtList if its not a newline to start with.
        else{
            
            aSuite.stmt = AspSmallStmtList.parse(s);
        }
        
        Main.log.leaveParser("suite");
        return aSuite;
    }

    @Override
    void prettyPrint(){
        Main.log.prettyWriteLn();
        Main.log.prettyIndent();
        for(AspStmt ast: as) {
            ast.prettyPrint();
        }
        if(checkDedent)Main.log.prettyDedent();
        if(!checkDedent)Main.log.prettyWriteLn();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue v = null;
        if(checkNewline){
            for(int i = 0; i < as.size() ; i++){
                v = as.get(i).eval(curScope);
            }
            return v;
            }
            v = stmt.eval(curScope);
            return v;
        }
}
