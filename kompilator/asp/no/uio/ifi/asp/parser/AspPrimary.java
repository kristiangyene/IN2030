package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;


class AspPrimary extends AspSyntax{

	AspAtom a;
	ArrayList<AspPrimarySuffix> pSuffix = new ArrayList<>();

	AspPrimary(int n){
		super(n);
	}

	static AspPrimary parse(Scanner s){
		Main.log.enterParser("primary");
		AspPrimary ap = new AspPrimary(s.curLineNum());

		ap.a = AspAtom.parse(s);
		while((s.curToken().kind == leftParToken) || (s.curToken().kind == leftBracketToken)){
			if(s.curToken().kind == leftBracketToken){
				ap.pSuffix.add(AspSubscription.parse(s));
			}else{
				ap.pSuffix.add(AspArguments.parse(s));
			}
		}

		Main.log.leaveParser("primary");
		return ap;
	}


	@Override
	void prettyPrint() {
		a.prettyPrint();

		for(AspPrimarySuffix a : pSuffix ){
			a.prettyPrint();
		}
	}

	@Override
	RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
		RuntimeValue v = null;
		if(pSuffix.size() != 0){
			v = a.eval(curScope);
			for(int i = 0; i < pSuffix.size(); i++){

				//Handling the subscription and argument separate
				//If its a subscrition
				if(pSuffix.get(i) instanceof AspSubscription){
					RuntimeValue vSuffix = curScope.find(pSuffix.get(i).eval(curScope).toString(), this);
					RuntimeValue vAtom = curScope.find(v.toString(), this);
	
					if(vAtom != null){
						if(vSuffix != null){
							v = vAtom.evalSubscription(vSuffix, this);
						} 
						else{
							v = vAtom.evalSubscription(pSuffix.get(i).eval(curScope), this);
						}
					}else{
						if(vSuffix != null){
							v = v.evalSubscription(vSuffix, this);
						}
						else{
							v = v.evalSubscription(pSuffix.get(i).eval(curScope), this);
						}
					}
				} 
				//Then its an argument
				else{
					RuntimeValue x = pSuffix.get(i).eval(curScope); //Her kraesjer det
					//String of the library function called
					RuntimeValue t = curScope.find(v.toString(), this);
					//String for lib function
					String func = t.toString();
					

					//LIBRARY FUNCTION "LEN" IS CALLED
					if(func.equals("len")){
						ArrayList<RuntimeValue>values = new ArrayList<>();
						RuntimeValue convert = null;
						RuntimeListValue rtList = (RuntimeListValue)x;
						RuntimeValue value = curScope.find(rtList.getElem(0).toString(), this);

						if(value == null){
							convert = rtList.getElem(0).evalLen(this);
							return convert;
						}

						if(value instanceof RuntimeListValue){
							convert = (RuntimeListValue)value;
						}else if(value instanceof RuntimeDictValue){
							convert = (RuntimeDictValue)value;
						}else if(value instanceof RuntimeStringValue){
							convert = (RuntimeStringValue)value;
						}
						else Main.error("Type is not allowed for function 'len'");


						values.add(convert);
						RuntimeIntValue len = (RuntimeIntValue)t.evalFuncCall(values, this);
						trace("Call function "  + t.toString() + " with params: " + values.toString());
						return len;
					}


					//LIBRARY FUNCTION "INT" IS CALLED
					else if(func.equals("int")){
						ArrayList<RuntimeValue>values = new ArrayList<>();
						RuntimeValue convert = null;
						RuntimeListValue rtList = (RuntimeListValue)x;
						trace("Call function "  + t.toString() + " with params: " + rtList.toString());
						RuntimeValue value = curScope.find(rtList.getElem(0).toString(), this);

						if(value == null){
							Long temp;
							temp = rtList.getElem(0).getIntValue("integer", this);
							convert = new RuntimeIntValue(temp);
							return convert;
						}


						if(value instanceof RuntimeIntValue){
							convert = (RuntimeIntValue)value;
						}else if(value instanceof RuntimeFloatValue){
							convert = (RuntimeFloatValue)value;
						}else if(value instanceof RuntimeStringValue){
							convert = (RuntimeStringValue)value;
						}
						else Main.error("Type is not allowed for function 'int'");

						values.add(convert);
						RuntimeIntValue len = (RuntimeIntValue)t.evalFuncCall(values, this);

						return len;
					}

					//LIBRARY FUNCTION "FLOAT" IS CALLED
					else if(func.equals("float")){
						ArrayList<RuntimeValue>values = new ArrayList<>();
						RuntimeValue convert = null;
                        RuntimeListValue rtList = (RuntimeListValue)x;
                        RuntimeValue value = curScope.find(rtList.getElem(0).toString(), this);

                        if(value == null){
                            double temp;
                            temp = rtList.getElem(0).getFloatValue("float", this);
                            convert = new RuntimeFloatValue(temp);
                            return convert;
                        }


                        if(value instanceof RuntimeIntValue){
							convert = (RuntimeIntValue)value;
						}else if(value instanceof RuntimeFloatValue){
                            convert = (RuntimeFloatValue)value;
                        }else if(value instanceof RuntimeStringValue){
                            convert = (RuntimeStringValue)value;
						}
						else Main.error("Type is not allowed for function 'float'");
                        

                        values.add(convert);
                        RuntimeIntValue len = (RuntimeIntValue)t.evalFuncCall(values, this);
                        trace("Called function "  + t.toString() + " with parameters: " + values.toString());
                        return len;
					}
					//LIBRARY FUNCTION "INPUT" IS CALLED

					else if (func.equals("input")){
                        RuntimeListValue rtList = (RuntimeListValue)x;
                        String output = rtList.getList().toString();
                        System.out.print(output.substring(1, output.length()-1));
						RuntimeStringValue len = (RuntimeStringValue)t.evalFuncCall(null, this);

						if(rtList.getList().size() == 0) trace("Call function "  + t.toString());
						 else trace("Called function "  + t.toString() + " with parameters: " + rtList.getList().toString());
						return len;
					}

                    //LIBRARY FUNCTION "STR" IS CALLED
					else if (func.equals("str")){
						ArrayList<RuntimeValue>values = new ArrayList<>();
						RuntimeValue convert = null;

                        RuntimeListValue rtList = (RuntimeListValue)x;
                        trace("Called function "  + t.toString() + " with parameters: " + rtList.getList().toString());
                        RuntimeValue value = curScope.find(rtList.getElem(0).toString(), this);

                        if(value == null){
                            String temp;
                            temp = rtList.getElem(0).getStringValue("str", this);
                            convert = new RuntimeStringValue(temp);
							RuntimeStringValue len = (RuntimeStringValue)t.evalFuncCall(rtList.getList(), this);
							
                            return len;
                        }

                        values.add(value);

                        RuntimeStringValue len = (RuntimeStringValue)t.evalFuncCall(values, this);
                        return len;

                    }
                    
                    //LIBRARY FUNCTION "RANGE" IS CALLED

					else if(func.equals("range")){
                        ArrayList<RuntimeValue>values = new ArrayList<>();
						RuntimeValue convert = null;

                        RuntimeListValue rtList = (RuntimeListValue)x;
                        for (int j = 0; j<rtList.getList().size(); j++) {
                            RuntimeValue value = curScope.find(rtList.getElem(j).toString(), this);
                            if(value == null){
                                long temp;
                                temp = rtList.getElem(j).getIntValue("int", this);
                                convert = new RuntimeIntValue(temp);
                                values.add(convert);
							} 
							else values.add(value);
                        }

                        trace("Called function "  + t.toString() + " with parameters: " + values.toString());
                        return t.evalFuncCall(values, this);

                    }

					//LIBRARY FUNCTION "PRINT" IS CALLED
					else if (func.equals("print")){
						ArrayList<RuntimeValue>values = new ArrayList<>();
						RuntimeValue convert = null;

                        RuntimeListValue rtList = (RuntimeListValue)x;

                        for (int j = 0; j<rtList.getList().size(); j++) {
                            RuntimeValue value = curScope.find(rtList.getElem(j).toString(), this);
                            if(value == null){
                                String temp;
                                temp = rtList.getElem(j).getStringValue("str", this);
                                convert = new RuntimeStringValue(temp);
                                values.add(convert);
							} 
							else values.add(value);
                        }

                        t.evalFuncCall(values, this);
                        trace("Called function "  + t.toString() + " with parameters: " + values.toString());
                        return new RuntimeNoneValue();
					}

					//NO LIBRARY FUNCTION CALLED: HANDLE AS A FUNCTION
					else if(t != null){
						if((x instanceof RuntimeListValue) && (t instanceof RuntimeFunc)){
							RuntimeFunc newFunc = (RuntimeFunc)t;
							RuntimeListValue rtList = (RuntimeListValue)x;
							trace("Called function "  + newFunc.toString() + " with parameters: " + rtList.getList().toString());
							v = newFunc.evalFuncCall(rtList.getList(), this);
							return v;
						} else{
							trace(v.showInfo() + " : " + t.showInfo());
							Main.error("Function is not allowed");
						}
					}
					else Main.error("Error, function called not found ");
				}
			}
			return v;
		}
		v = a.eval(curScope);
		return v;
	}

}
