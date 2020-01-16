package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt{
    ArrayList<AspSmallStmt> smallStmts = new ArrayList<>();

    AspSmallStmtList(int n){
        super(n);
    }

    static AspSmallStmtList parse(Scanner s){
        Main.log.enterParser("small stmt list");

        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());
        assl.smallStmts.add(AspSmallStmt.parse(s));
        skip(s, s.curToken().kind);

        Main.log.leaveParser("small stmt list");
        return assl;
    }
    
    @Override
    public void prettyPrint(){
        for(AspSmallStmt stmt: smallStmts){
            stmt.prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue v = null;
        for(int i = 0; i < smallStmts.size(); i++){ 
            v = smallStmts.get(i).eval(curScope);
        }
        return v;
    }
}
