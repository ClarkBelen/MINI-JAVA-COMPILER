package Analyzers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyntaxAnalyzer {
    private final List<String> tokens;
    private final List<String> dtv;

    public SyntaxAnalyzer(List<String> tokens, List<String> dtv) {
        this.tokens = tokens;
        this.dtv = dtv;
    }

    public boolean isSyntaxPassed (){
        final String[] DECLARATION = {"<data_type>", "<identifier>", "<delimiter>"};
        final String[] ASSIGNMENT = {"<data_type>", "<identifier>", "<assignment_operator>", "<value>", "<delimiter>"};

        List<List> allSyntax = new ArrayList<>();

        int counter = 0;
        for(int t = 0; t < tokens.size(); t++){
            if(tokens.get(t).equals("<delimiter>") || t==tokens.size()-1){
                allSyntax.add(tokens.subList(counter, t+1));
                counter = t+1;
            }
        }

        int i = 0;

        for(List group : allSyntax){ //analyze according to valid syntax, and filter data types with values per group
            if (group.equals(Arrays.asList(DECLARATION))) {

                dtv.remove(i);
            }
            else if (group.equals(Arrays.asList(ASSIGNMENT))) {

                i+=2;
            }
            else {
                System.out.println("Syntax Analysis: ERROR! ->  Unknown/Invalid sequence of tokens found.");
                return false;
            }
        }
        System.out.println("Syntax Analysis: Valid sequence of tokens per line");
        return true;
    }
}
