package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeIntValue extends RuntimeValue{
    long intValue;

    public RuntimeIntValue(long v){
        intValue = v;
    }

    @Override
    protected String typeName(){
    return "integer";
    }
 
    @Override
    public String toString(){
        return Long.toString(intValue);
    }

    @Override
    public String showInfo(){
        return Long.toString(intValue);
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        if(intValue == 0) return false;
        return true;
    }

    @Override
    public long getIntValue(String what, AspSyntax where){
        return intValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where){
        return (double) intValue;
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        return String.valueOf(intValue);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where){
        return new RuntimeFloatValue(intValue);
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where){
        return new RuntimeFloatValue((-1 * intValue));
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        if(intValue == 0) return new RuntimeBoolValue(true);
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("+ operand", where);
            rtValue = new RuntimeIntValue(intValue + l);
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("+ operand", where);
            rtValue = new RuntimeFloatValue(intValue + d);
        } 
        else runtimeError("Type error for +.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("- operand", where);
            rtValue = new RuntimeIntValue(intValue - l);
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("- operand", where);
            rtValue = new RuntimeFloatValue(intValue - d);
        } 
        else runtimeError("Type error for -.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("* operand", where);
            rtValue = new RuntimeIntValue(intValue * l);
        } 
        else if(v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("* operand", where);
            rtValue = new RuntimeFloatValue(intValue * d);
        } 
        else runtimeError("Type error for *.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("// operand", where);
            rtValue = new RuntimeIntValue(Math.floorDiv(intValue , l));
        } 
        else if(v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("// operand", where);
            rtValue = new RuntimeFloatValue(Math.floor(intValue / d));
        } 
        else runtimeError("Type error for //.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("== operand", where);
            rtValue = new RuntimeBoolValue((intValue == l));
        }
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("== operand", where);
            rtValue = new RuntimeBoolValue((intValue == d));
        } 
        else if (v instanceof RuntimeNoneValue) return new RuntimeBoolValue(false);
        else runtimeError("Type error for ==.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("!= operand", where);
            rtValue = new RuntimeBoolValue((intValue != l));
        }
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("!= operand", where);
            rtValue = new RuntimeBoolValue((intValue != d));
        } 
        else if (v instanceof RuntimeNoneValue) return new RuntimeBoolValue(true);
        else runtimeError("Type error for !=.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("< operand", where);
            rtValue = new RuntimeBoolValue((intValue < l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("< operand", where);
            rtValue = new RuntimeBoolValue((intValue < d));
        } 
        else runtimeError("Type error for <.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("<= operand", where);
            rtValue = new RuntimeBoolValue((intValue <= l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("<= operand", where);
            rtValue = new RuntimeBoolValue((intValue <= d));
        } 
        else runtimeError("Type error for <=.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue(">= operand", where);
            rtValue = new RuntimeBoolValue((intValue >= l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue(">= operand", where);
            rtValue = new RuntimeBoolValue((intValue >= d));
        } 
        else runtimeError("Type error for >=.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("> operand", where);
            rtValue = new RuntimeBoolValue((intValue > l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("> operand", where);
            rtValue = new RuntimeBoolValue((intValue > d));
        } 
        else runtimeError("Type error for >.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("/ operand", where);
            rtValue = new RuntimeFloatValue(intValue / l);
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("/ operand", where);
            rtValue = new RuntimeFloatValue(intValue / d);
        } 
        else runtimeError("Type error for /.", where);
        return rtValue;
    }


    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("% operand", where);
            rtValue = new RuntimeIntValue(Math.floorMod(intValue, l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("% operand", where);
            rtValue = new RuntimeFloatValue(intValue - d * Math.floor(intValue/d));
        } 
        else runtimeError("Type error for %.", where);
        return rtValue;
    }
}