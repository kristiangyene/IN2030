package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue{
    double floatValue;

    public RuntimeFloatValue(double v){
        floatValue = v;
    }

    @Override
    protected String typeName(){
    return "float";
    }
 
    @Override
    public String toString(){
        return Double.toString(floatValue);
    }

    @Override
    public String showInfo(){
        return Double.toString(floatValue);
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        if(floatValue == 0.0) return false;
        return true;
    }

    @Override
    public long getIntValue(String what, AspSyntax where){
        return (long)floatValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where){
        return floatValue;
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        return String.valueOf(floatValue);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where){
        return new RuntimeFloatValue(floatValue);
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where){
        return new RuntimeFloatValue((-1.0 * floatValue));
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        if(floatValue == 0.0) return new RuntimeBoolValue(true);
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("+ operand", where);
            rtValue = new RuntimeFloatValue(floatValue + l);
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("+ operand", where);
            rtValue = new RuntimeFloatValue(floatValue + d);
        } 
        else runtimeError("Type error for +.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("< operand", where);
            rtValue = new RuntimeBoolValue((floatValue < l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("< operand", where);
            rtValue = new RuntimeBoolValue((floatValue < d));
        } 
        else runtimeError("Type error for <.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("<= operand", where);
            rtValue = new RuntimeBoolValue((floatValue <= l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("<= operand", where);
            rtValue = new RuntimeBoolValue((floatValue <= d));
        } 
        else runtimeError("Type error for <=.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("> operand", where);
            rtValue = new RuntimeBoolValue((floatValue > l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("> operand", where);
            rtValue = new RuntimeBoolValue((floatValue > d));
        } 
        else runtimeError("Type error for >.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue(">= operand", where);
            rtValue = new RuntimeBoolValue((floatValue >= l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue(">= operand", where);
            rtValue = new RuntimeBoolValue((floatValue >= d));
        } 
        else runtimeError("Type error for >=.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("- operand", where);
            rtValue = new RuntimeFloatValue(floatValue - l);
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("- operand", where);
            rtValue = new RuntimeFloatValue(floatValue - d);
        } 
        else runtimeError("Type error for -.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("* oprenad", where);
            rtValue = new RuntimeFloatValue(floatValue * l);
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("* operand", where);
            rtValue = new RuntimeFloatValue(floatValue * d);
        } 
        else runtimeError("Type error for *.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("== operand", where);
            rtValue = new RuntimeBoolValue((floatValue == l));
        }
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("== operand", where);
            rtValue = new RuntimeBoolValue((floatValue == d));
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
            rtValue = new RuntimeBoolValue((floatValue != l));
        }
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("!= operand", where);
            rtValue = new RuntimeBoolValue((floatValue != d));
        } 
        else if (v instanceof RuntimeNoneValue) return new RuntimeBoolValue(true);
        else runtimeError("Type error for !=.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("/ operand", where);
            rtValue = new RuntimeFloatValue(floatValue / l);
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("/ operand", where);
            rtValue = new RuntimeFloatValue(floatValue / d);
        } 
        else runtimeError("Type error for /.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("% operand", where);
            rtValue = new RuntimeFloatValue(floatValue - l * Math.floor(floatValue / l));
            } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("% operand", where);
            rtValue = new RuntimeFloatValue(floatValue - d * Math.floor(floatValue/d));
        } 
        else runtimeError("Type error for %.", where);
        return rtValue;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if (v instanceof RuntimeIntValue){
            long l = v.getIntValue("// opernad", where);
            rtValue = new RuntimeFloatValue(Math.floor(floatValue / l));
        } 
        else if (v instanceof RuntimeFloatValue){
            double d = v.getFloatValue("// operand", where);
            rtValue = new RuntimeFloatValue(Math.floor(floatValue / d));
        } 
        else runtimeError("Type error for //.", where);
        return rtValue;
    }
}