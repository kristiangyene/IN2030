package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.runtime.RuntimeValue;

import java.util.*;


public class RuntimeDictValue extends RuntimeValue{
    Map<RuntimeValue, RuntimeValue> rtValueDict = new HashMap<RuntimeValue, RuntimeValue>();


    public RuntimeDictValue ( Map<RuntimeValue, RuntimeValue> map){
        this.rtValueDict = map;
    }

    public RuntimeDictValue(){}

    @Override
    protected String typeName(){
        return "dict";
    }


    public void addElem(RuntimeValue key, RuntimeValue value){
        rtValueDict.put(key, value);
    }

    @Override
    public String toString(){
        String dictString = "";
        if(rtValueDict.isEmpty()) dictString = "{" + dictString + "}";
        else{
            dictString += "{";
            for (Map.Entry<RuntimeValue, RuntimeValue> entry : rtValueDict.entrySet()){
            dictString += entry.getKey().toString() + " : " + entry.getValue().toString() + " , ";
            }
            dictString = dictString.substring(0, dictString.length() - 2) + " }";
        }
        return dictString;
    }

    @Override
    public String showInfo(){
        String dictString = "";
        if(rtValueDict.isEmpty()) dictString = "{" + dictString + "}";
        else{
            dictString += "{";
            for (Map.Entry<RuntimeValue, RuntimeValue> entry : rtValueDict.entrySet()){
            dictString += entry.getKey().toString() + " : " + entry.getValue().toString() + " , ";
            }
            dictString = dictString.substring(0, dictString.length() - 2) + "}";
        }
        return dictString;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where){
        RuntimeValue rtValue = null;
        if(v instanceof RuntimeStringValue){
            String s = v.getStringValue("[...] operand", where);
            for (Map.Entry<RuntimeValue, RuntimeValue> entry : rtValueDict.entrySet()){
                if(entry.getKey().getStringValue("String", where).equals(s)){
                    rtValue = entry.getValue();
                    return rtValue;
                }
            }
            runtimeError("Key not found.", where);
        }
        else runtimeError("Type error for{...}.", where);
        return rtValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        if(rtValueDict.isEmpty()) return false;
        return true; 
    }


    @Override
    public void evalAssignElem(RuntimeValue v, RuntimeValue val, AspSyntax where){
        String s = v.toString();
        ArrayList<String> keystrings = new ArrayList<>();
        for(RuntimeValue key: rtValueDict.keySet()) keystrings.add(key.toString());
        if(keystrings.contains(s)){
        for (Map.Entry<RuntimeValue, RuntimeValue> entry : rtValueDict.entrySet()){
            if(entry.getKey().toString().equals(s)){
                rtValueDict.put(entry.getKey(), val);
                
                }
            }
        }else{
            rtValueDict.put(v, val);
        }
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        String dictString = "";
        if(rtValueDict.isEmpty()) dictString = "{" + dictString + "}";
        else{
            dictString += "{";
            for (Map.Entry<RuntimeValue, RuntimeValue> entry : rtValueDict.entrySet()){
            dictString += entry.getKey().toString() + " : " + entry.getValue().toString() + " , ";
            }
            dictString = dictString.substring(0, dictString.length() - 2) + "}";
        }
        return dictString;
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
    public RuntimeValue evalLen(AspSyntax where){
        return  new RuntimeIntValue(rtValueDict.size());
    }
}