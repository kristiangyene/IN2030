package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.runtime.RuntimeValue;

import java.util.ArrayList;

  
public class RuntimeListValue extends RuntimeValue{

    ArrayList<RuntimeValue> rtValueList = new ArrayList<RuntimeValue>();

    public RuntimeListValue(){}

    public RuntimeListValue (ArrayList<RuntimeValue> v){    
        rtValueList = v;
    }

    public void addElem(RuntimeValue v){
        rtValueList.add(v);
    }

    @Override
    protected String typeName(){
        return "list";
    }

    public ArrayList<RuntimeValue> getList(){
        return rtValueList;
    }

    public RuntimeValue getElem(int pos){
        return rtValueList.get(pos);
    }


    @Override
    public RuntimeValue evalLen(AspSyntax where){
        RuntimeIntValue v = new RuntimeIntValue(rtValueList.size());
        return v;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if(v instanceof RuntimeIntValue){
            int i = (int) v.getIntValue("[...] operand", where);
            if(i > rtValueList.size()-1) Main.error("List size: " + rtValueList.size() + ". Position of the requested element: " + i + ".");
            else rtValue = rtValueList.get(i);
        }
        else runtimeError("Type error for [...].", where);
        return rtValue;
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        String liststr = "";
        for(RuntimeValue r : rtValueList) liststr += r.showInfo() + ", ";
        if(rtValueList.isEmpty()) liststr = "[" + liststr + "]";
        liststr = "[" + liststr.substring(0,(liststr.length()-2)) + "]";
        return liststr;
    }

    @Override
    public String toString(){
        String liststr = "";
        for(RuntimeValue r : rtValueList) liststr += r.showInfo() + ", ";
        if(rtValueList.size() == 0) liststr = "[" + liststr + "]";
        liststr = "[" + liststr.substring(0,(liststr.length()-2)) + "]";
        return liststr;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if(v instanceof RuntimeIntValue){
            long l = v.getIntValue("* operand", where);
            rtValue = new RuntimeListValue(multiplyList(rtValueList, l));
        } 
        else runtimeError("Type error for *.", where);
        return rtValue;
    }

    public ArrayList<RuntimeValue> multiplyList(ArrayList<RuntimeValue> v, long l){
        ArrayList<RuntimeValue> temp = new ArrayList<RuntimeValue>();
        temp.addAll(v);
        for(int i = 0; i < l-1; i++) v.addAll(temp);
        return v;
    }
    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeNoneValue) return new RuntimeBoolValue(false);
        else runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeNoneValue) return new RuntimeBoolValue(true);
        else runtimeError("Type error for !=.", where);
        return null;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        if(rtValueList.isEmpty()) return false;
        return true;
    
    }

    @Override
    public void evalAssignElem(RuntimeValue v, RuntimeValue val, AspSyntax where){
        int i = (int) v.getIntValue("[...] operand", where);
        rtValueList.remove(i);
        rtValueList.add(i, val);
    }
}