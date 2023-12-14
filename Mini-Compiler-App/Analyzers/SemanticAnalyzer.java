package Analyzers;

import java.util.List;

public class SemanticAnalyzer {
    public boolean noError = true;

    public SemanticAnalyzer(List<String> dtv) {

        for(int i = 0; i < dtv.size(); i++){
            if(!isValueValid(dtv.get(i),dtv.get(i+1))){ //analyze each data type if it has valid values

                System.out.println("Semantic Analysis: ERROR -> Unknown/Invalid value/s found.");
                noError = false;
                break;
            }
            i++;
        }

        if(noError){

            System.out.println("Semantic Analysis: Values are valid. Congrats! No Error at all.");
        }
    }
    public boolean isValueValid(String dataType, String value) {

        switch(dataType){
            case "String":
                if(value.matches("\"[^\"]*\"")){ //valid value for String
                    return true;
                }
                break;
            case "int":
                if(value.matches("[+-]?[0-9]+")) { // valid value for integer
                    return true;
                }
                break;
            case "double":
                if(value.matches("[+-]?[0-9]+")||value.matches("[+-]?[0-9]+(\\.[0-9]+)")) { //valid value for double
                    return true;
                }
                break;
            case "char":
                if(value.matches("'.'")) { //valid value for single character
                    return true;
                }
                break;
            case "boolean":
                if(value.equals("true") || value.equals("false")){
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }
}

