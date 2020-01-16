package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;


    public Scanner(String fileName) {
	curFileName = fileName;
	indents.push(0);

	try {
	    sourceFile = new LineNumberReader(
			    new InputStreamReader(
				new FileInputStream(fileName),
				"UTF-8"));
	} catch (IOException e) {
	    scannerError("Cannot read " + fileName + "!");
	}
    }


    private void scannerError(String message) {
	String m = "Asp scanner error";
	if (curLineNum() > 0)
	    m += " on line " + curLineNum();
	m += ": " + message;

	Main.error(m);
    }


    public Token curToken() {
	while (curLineTokens.isEmpty()) {
	    readNextLine();
	}
	return curLineTokens.get(0);
    }


    public void readNextToken() {
	if (! curLineTokens.isEmpty())
	    curLineTokens.remove(0);
    }


    private void readNextLine() {
	curLineTokens.clear();

	// Read the next line:
	String line = null;
	try {
	    line = sourceFile.readLine();
	    if (line == null) {
			//Create dedent tokens at the end
			while(!indents.empty()){
				if(indents.pop() > 0){
					curLineTokens.add(new Token(dedentToken, curLineNum()));
				}
			  }
			
			
		sourceFile.close();
		sourceFile = null;
	    } else {
		Main.log.noteSourceLine(curLineNum(), line);
	    }
	} catch (IOException e) {
	    sourceFile = null;
	    scannerError("Unspecified I/O error!");
	}

	//Base for checking tokenkinds
	boolean ignore = false;
	String lineString = "";

	if(line != null){
		//Read lines using array as a way to store all chars of a line
		String trimmedLine = line.trim();
		char[] charArray = trimmedLine.toCharArray();
		

		//Start iterating through chars
		for (int i = 0; i < charArray.length;){
			boolean isName = false;
			boolean isString = false;
			boolean isFloat = false;
			boolean operatorOrDelimiter = false;
			//Handle comments
			if (charArray[i] == '#') break;
			
			//If char is a name
			if (isLetterAZ(charArray[i])){
				lineString += charArray[i];
				isName = true;
				i++;
				while(charArray.length > i){
					//Check if all chars are allowed in nameToken
					if(isLetterAZ(charArray[i]) || isDigit(charArray[i])){
						lineString += charArray[i];
						i++;
					}else break;
				}
				//Find matching tokenKind keywords
				for(TokenKind kind : EnumSet.range(andToken, yieldToken)){
					if(kind.toString().equals(lineString)){
						curLineTokens.add(new Token(kind,curLineNum()));
						isName = false;
					}
				}
				if(isName){
					curLineTokens.add(new Token(nameToken,curLineNum()));
					curLineTokens.get(curLineTokens.size()-1).name = lineString;
				}
				lineString = "";
			}

			
			//If char is a string
			else if(charArray[i] == '\"' || charArray[i] == '\''){
				lineString += charArray[i];
				i++;
				//Adds the rest for the string
				while (charArray.length > i){
					if (charArray[i] == '\"' || charArray[i] == '\''){
						lineString += charArray[i];
						isString = true;
						i++;
						break;
					}
					lineString += charArray[i];
					i++;
				}
				//If scanner cant find the next double quote
				if (!isString){
					String errorLine = "\n";
					for (int j = 0; j < charArray.length; j++) errorLine += " ";			
					errorLine += "∧\n";
					scannerError("Error! Invalid string\n" + trimmedLine + errorLine);
				}
				Token strToken = new Token(stringToken, curLineNum());
				strToken.stringLit = lineString;
				curLineTokens.add(strToken);
				lineString = "";
			}

		
			//If char is an integer or a float
			else if(isDigit(charArray[i])){
				lineString += charArray[i];
				i++;
				while(charArray.length > i){
					//Check if there is a digit before "."
					if(isDigit(charArray[i])){
						lineString += charArray[i];
						i++;
					}else if (charArray[i] == '.'){
						lineString += charArray[i];
						i++;
						//Needed try/catch to not get indexOutOfBoundsException
						try{
							//Check if there is a digit after "."
							boolean digit = isDigit(charArray[i]);
						}catch(Exception ArrayIndexOutOfBoundsException){
							int j = 0;
							String errorLine = "\n";
							while(charArray[j] != '.'){
								errorLine += " ";
								j++;
							}
							errorLine += "∧\n";
							scannerError("Error! Ivalid float\n" + trimmedLine + errorLine);
						}
						isFloat = true;
					}
					//Break if integer
					else break;
				}
				if(isFloat){
					Token floToken = new Token(floatToken, curLineNum());
					floToken.floatLit = Double.parseDouble(lineString);
					curLineTokens.add(floToken);

				}else{
					Token intToken = new Token(integerToken, curLineNum());
					intToken.integerLit = Integer.parseInt(lineString);
					curLineTokens.add(intToken);
				}
				lineString = "";
			}
			else{
				//If char is a operator or delimiter
				if(charArray[i] != ' '){
					lineString += charArray[i];
					i++;
					while(charArray.length > i){
						if (!isLetterAZ(charArray[i]) && charArray[i] != ' ' 
										&& !isDigit(charArray[i]) 
								 		&& charArray[i] != '\"'){
							lineString += charArray[i];
							i++;
						}
						else break;
					}
					//See if it finds matching tokenKind
					for(TokenKind kind : EnumSet.range(astToken, semicolonToken)){
						if(kind.toString().equals(lineString)){
							curLineTokens.add(new Token(kind,curLineNum()));
							operatorOrDelimiter = true;
						}
					}
					char wrongSymbol = lineString.charAt(0);
					String errorLine = "";
					while(operatorOrDelimiter == false){
						try{
							lineString = lineString.substring(0, lineString.length() - 1);
							i--;
						}catch(Exception e){
							//If char is not in enum TokenKind
							int j = 0;
							errorLine = "\n";
							while(charArray[j] != wrongSymbol){
								errorLine += " ";
								j++;
							}
							errorLine += "∧\n";
							scannerError("Error! Invalid symbol\n" + trimmedLine + errorLine);
						}
						for(TokenKind kind : EnumSet.range(astToken, semicolonToken)){
							if(kind.toString().equals(lineString)){
								curLineTokens.add(new Token(kind,curLineNum()));
								operatorOrDelimiter = true;
							}
						}
					}
				}
				//If char is a whitespace
				else i++;
				lineString = "";
				}
			}
		}
		else{
			curLineTokens.add(new Token(eofToken, curLineNum()));
			ignore = true;
		}
			

	//Algorithm for indent/dedent handling according to the lecture
	if(!ignore && curLineTokens.size() != 0){
		line = expandLeadingTabs(line);
		int indent = findIndent(line);
		//Create indentToken if indent > indents.top
		if(indent > indents.peek()){
			indents.push(indent);
			curLineTokens.add(0, new Token(indentToken,curLineNum()));
		}
		//Create dedentToken if indent < indents.top
		while(indent < indents.peek()){
			indents.pop();
			curLineTokens.add(0, new Token(dedentToken,curLineNum()));
		}
		//Error if indent != indents
		if(indent != indents.peek()){
			String errorLine = "\n|";
			for(int j = 0; j < indent-2; j++){
				errorLine += "_";
			}
			errorLine += "|\n";
			scannerError("Error! Invalid use of indent/dedent\n" + line + errorLine);
		}
	}


	// Terminate line
	//Added a check in case the line is ignored
	if (!ignore) curLineTokens.add(new Token(newLineToken,curLineNum()));
	
	//Added a check in case the line is not ignored, just also if its only one token on the line
	if(ignore || curLineTokens.size() != 1) {
		for (Token t: curLineTokens) 
			Main.log.noteToken(t);
		}
	}



    public int curLineNum() {
	return sourceFile!=null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
	int indent = 0;

	while (indent<s.length() && s.charAt(indent)==' ') indent++;
	return indent;
    }

    private String expandLeadingTabs(String s) {
	String newS = "";
	for (int i = 0;  i < s.length();  i++) {
	    char c = s.charAt(i);
	    if (c == '\t') {
		do {
		    newS += " ";
		} while (newS.length()%TABDIST > 0);
	    } else if (c == ' ') {
		newS += " ";
	    } else {
		newS += s.substring(i);
		break;
	    }
	}
	return newS;
    }


    private boolean isLetterAZ(char c) {
	return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }


    private boolean isDigit(char c) {
	return '0'<=c && c<='9';
    }


    public boolean isCompOpr() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	return false;
    }


    public boolean isFactorPrefix() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	return false;
    }


    public boolean isFactorOpr() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	return false;
    }
	

    public boolean isTermOpr() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	return false;
    }


    public boolean anyEqualToken() {
	for (Token t: curLineTokens) {
	    if (t.kind == equalToken) return true;
	    if (t.kind == semicolonToken) return false;
	}
	return false;
    }
}
