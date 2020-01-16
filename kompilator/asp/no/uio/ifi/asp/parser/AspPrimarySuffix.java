package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public abstract class AspPrimarySuffix extends AspSyntax{
    String token;

    AspPrimarySuffix(int n) {
        super(n);
    }

    static AspPrimarySuffix parse(Scanner s){
        Main.log.enterParser("primary suffix");

        AspPrimarySuffix aps = null;
        //Send to either AspArguments or AspSubscription.
        if(s.curToken().kind == TokenKind.leftParToken) aps = AspArguments.parse(s);
        else aps = AspSubscription.parse(s);

        Main.log.leaveParser("primary suffix");
        return aps;
    }
}
