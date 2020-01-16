package no.uio.ifi.asp.runtime;


import java.util.ArrayList;
import java.util.Scanner;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.parser.AspSyntax;
import java.util.Collections;
import java.util.stream.LongStream;


public class RuntimeLibrary extends RuntimeScope{
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary(){
      assign("float", new RuntimeFunc("float"){
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
            checkNumParams(actualParams, 1, "float", where);
            RuntimeFloatValue i = new RuntimeFloatValue(actualParams.get(0).getFloatValue("double",where));
            return i;
        }
      });

      assign("input", new RuntimeFunc("input"){
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
            return new RuntimeStringValue(keyboard.nextLine());
        }
      });

      assign("int", new RuntimeFunc("int"){
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
            checkNumParams(actualParams, 1, "int", where);
            return new RuntimeIntValue(actualParams.get(0).getIntValue("long",where));
        }
      });

      assign("len", new RuntimeFunc("len"){
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
            checkNumParams(actualParams, 1, "len", where);
            return actualParams.get(0).evalLen(where);
        }
      });

      assign("print", new RuntimeFunc("print"){
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
            for(RuntimeValue rv : actualParams){
              System.out.print(rv.toString() + " ");
            }
            System.out.println();
            return new RuntimeNoneValue();
          }
      });

        assign("range", new RuntimeFunc("range"){
          @Override
          public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
              checkNumParams(actualParams, 2, "int", where);
              RuntimeValue convertedValue = null;
              ArrayList<RuntimeValue> rangedList = new ArrayList<>();
                        long[] range = LongStream.rangeClosed(actualParams.get(0).getIntValue("int", where), actualParams.get(1).getIntValue("int", where)).toArray();
                        for (int k = 0; k < range.length-1; k++) { 
                            convertedValue = new RuntimeIntValue(range[k]);
                            rangedList.add(convertedValue);
                        }
              return new RuntimeListValue(rangedList);
        }
      });

        assign("str", new RuntimeFunc("str"){
          @Override
          public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
              checkNumParams(actualParams, 1, "str", where);
              return new RuntimeStringValue(actualParams.get(0).getStringValue("str",where));
        }
      });
    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs, int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect) RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}
