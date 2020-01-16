package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.parser.*;
import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeFunc extends RuntimeValue{
    AspFuncDef funcDef;
    RuntimeScope defScope;
    String name;
    ArrayList <RuntimeValue> list = new ArrayList<>();


    public RuntimeFunc(RuntimeValue rv, ArrayList<RuntimeValue> list, RuntimeScope rp, AspFuncDef def){
        name = rv.showInfo();
        this.list = list;
        defScope = rp;
        this.funcDef = def;
    }

    public  RuntimeFunc(String s ){
        name = s;
    }


    @Override
    public String toString(){
        return name;
    }

    @Override
    protected String typeName() {
        return "function";
    }

    RuntimeScope returnScope(){
        return defScope;
    }

    boolean checkPara(RuntimeValue v){
        RuntimeListValue rtList = (RuntimeListValue)v;
        return (rtList.getList().size() == list.size());
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> argList, AspSyntax where){
        RuntimeValue hi = null;
        if(argList.size() == list.size()){
            RuntimeScope scope = new RuntimeScope(defScope);

            for(int i = 0; i < list.size(); i ++){
                RuntimeValue recValue = null;
                recValue = defScope.find(argList.get(i).toString(), funcDef);
                if(recValue != null) scope.assign(list.get(i).toString(), recValue);
                else scope.assign(list.get(i).toString(), argList.get(i));
            } 
            try{
                hi = funcDef.runFunction(scope);
            } 
            catch(RuntimeReturnValue v){
                return v.value;
            }
        }
        else Main.error("Actual and formal parameters are not the same");
        return hi;
    }
}
