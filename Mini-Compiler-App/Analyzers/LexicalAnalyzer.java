package Analyzers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    public List<String> tokens;
    public List<String> dtv = new ArrayList<>(); // data types and values
    // Will be used for type checking later at semantic analysis phase if syntax analysis phase passed
    private final String[] lexemes;

    public LexicalAnalyzer(String input){

        if(input.contains(";")) {
            input = input.replace(";"," ; ");
        }
        if(input.contains("=")) {
            input = input.replace("="," = ");
        }

        Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\S+");
        Matcher matcher = pattern.matcher(input);

        List<String> inputSpecific = new ArrayList<>();
        while (matcher.find()) {
            inputSpecific.add(matcher.group());
        }

        lexemes = inputSpecific.toArray(new String[inputSpecific.size()]);//store potential lexemes into the list
        System.out.println("Lexemes found: " + Arrays.toString(lexemes));
    }

    public boolean tokenizer() {

        tokens = new ArrayList<>(lexemes.length);

        for(String lexeme : lexemes){

            switch(lexeme){

                case ";":
                    tokens.add("<delimiter>");
                    break;
                case "=":
                    tokens.add("<assignment_operator>");
                    break;
                case "String", "int", "double", "char", "boolean":
                    tokens.add("<data_type>");
                    dtv.add(lexeme);
                    break;
                case "true", "false": //valid value for Boolean
                    tokens.add("<value>");
                    dtv.add(lexeme);
                    break;
                default:
                    if(lexeme.matches("^([a-zA-Z_$][a-zA-Z\\d_$]*)$")) { //for valid identifier
                        tokens.add("<identifier>");
                    }
                    else if(lexeme.matches("\"[^\"]*\"")){ //valid value for String
                        tokens.add("<value>");
                        dtv.add(lexeme);
                    }
                    else if(lexeme.matches("[+-]?[0-9]+")) { // valid value for integer
                        tokens.add("<value>");
                        dtv.add(lexeme);
                    }
                    else if(lexeme.matches("[+-]?[0-9]+(\\.[0-9]+)")) { //valid value for double
                        tokens.add("<value>");
                        dtv.add(lexeme);
                    }
                    else if(lexeme.matches("'.'")) { //valid value for single character
                        tokens.add("<value>");
                        dtv.add(lexeme);
                    }
                    else {
                        System.out.println("Lexical Analysis: ERROR! -> Unknown/Invalid lexeme found.");
                        tokens.clear();
                        return false;
                    }
                    break;
            }
        }
        System.out.println("Lexical Analysis: Valid lexemes found.");
        return true;
    }
}
