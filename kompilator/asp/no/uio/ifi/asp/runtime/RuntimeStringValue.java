package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue{
    String stringValue;


    public RuntimeStringValue(String v){
        stringValue = v;
    }


    @Override
    protected String typeName(){
        return "string";
    }

    @Override
    public String showInfo(){
        return stringValue;

    }
    @Override
    public String toString(){
        return stringValue;
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where){
        return new RuntimeIntValue(stringValue.length());
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            int i = (int) v.getIntValue("[...] operand", where);
            rtValue = new RuntimeStringValue(Character.toString(stringValue.charAt(i)));
        }
        else runtimeError("Type error for [...].", where);
        return rtValue;
    }


    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeStringValue){
            String s = v.getStringValue("+ operand", where);
            rtValue = new RuntimeStringValue(stringValue.concat(s));
        }
        else runtimeError("Type error for +.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("* operand", where);
            rtValue = new RuntimeStringValue(multiplyString(stringValue, l));
        }
        else runtimeError("Type error for *.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeStringValue){
            String s = v.getStringValue("== operand", where);
            rtValue = new RuntimeBoolValue((stringValue.equals(s)));
        } 
        else if (v instanceof RuntimeNoneValue) return new RuntimeBoolValue(false);
        else runtimeError("Type error for ==.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeStringValue){
            String s = v.getStringValue("!= operand", where);
            rtValue = new RuntimeBoolValue((!stringValue.equals(s)));
        } 
        else if (v instanceof RuntimeNoneValue) return new RuntimeBoolValue(true);
        else runtimeError("Type error for !=.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeStringValue){
            String s = v.getStringValue("< operand", where);
            if(stringValue.compareTo(s) < 0) rtValue = new RuntimeBoolValue(true);
            else rtValue = new RuntimeBoolValue(false); 
        } 
        else runtimeError("Type error for <.", where);
        return rtValue;
    }

    
    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeStringValue){
            String s = v.getStringValue("> operand", where);
            if(stringValue.compareTo(s) > 0) rtValue = new RuntimeBoolValue(true);
            else rtValue = new RuntimeBoolValue(false);
        } 
        else runtimeError("Type error for >.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeStringValue){
            String s = v.getStringValue(">= operand", where);
            if(stringValue.compareTo(s) >= 0) rtValue = new RuntimeBoolValue(true);
            else rtValue = new RuntimeBoolValue(false);
        } 
        else runtimeError("Type error for >=.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeStringValue){
            String s = v.getStringValue("<= operand", where);
            if(stringValue.compareTo(s) <= 0) rtValue = new RuntimeBoolValue(true);
            else rtValue = new RuntimeBoolValue(false);
        } 
        else runtimeError("Type error for <.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        if(stringValue == "") return new RuntimeBoolValue(true);
        return new RuntimeBoolValue(false);
    }


    @Override
    public long getIntValue(String what, AspSyntax where){
        return Long.parseLong(stringValue);
    }

    @Override
    public double getFloatValue(String what, AspSyntax where){
        return Double.parseDouble(stringValue);
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        if(stringValue == "") return false;
        return true;
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        return stringValue;
    }

    public String multiplyString(String s, long l){
        String rtValueult = "";
        for(int i = 0; i < l; i++) rtValueult += s;
        return rtValueult;
    }
}