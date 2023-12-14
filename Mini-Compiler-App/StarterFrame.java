import Analyzers.LexicalAnalyzer;
import Analyzers.SemanticAnalyzer;
import Analyzers.SyntaxAnalyzer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StarterFrame extends JFrame implements ActionListener {

    private JTextArea resultTextArea;
    private JTextArea codeTextArea;
    private JButton openFileB, lexicalAnalysisB, syntaxAnalysisB, semanticAnalysisB, clearB;
    private LexicalAnalyzer lexical;
    private File selectedFile;

    public StarterFrame(){

        this.setTitle("Mini Java Compiler Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,700);
        this.setLayout(new BorderLayout(3,3));
        this.setLocationRelativeTo(null);
//        this.setResizable(false);

        setUpComponents(); //Set up all components for the GUI

        this.setVisible(true);

        displayWelcomeMessage(); //Initial message when starts

    }

    private void displayWelcomeMessage() {
        String welcome = """
                Welcome to the Mini Java Compiler App!
                
                This compiler only accept codes from a text file containing :
                    1. Data types limited to: [String, int, double, char, boolean]
                    2. Valid Identifiers
                    3. Simple or basic declaration or assignment statements/expressions
                    4. Valid values for specific data types per assignment syntax
                    5. Statements/Expressions with ending delimiter (';')
                
                Enjoy using this Mini Compiler!
                """;
        JOptionPane.showMessageDialog(null, welcome,"Welcome Message",JOptionPane.PLAIN_MESSAGE);
    }

    //-----------------------Main Set up method-----------------------------------//
    private void setUpComponents(){

        Border defaultBorder = BorderFactory.createEtchedBorder();

        //Create Panels using createPanel method
        JPanel menuArea = createPanel(new FlowLayout(), Color.gray, new Dimension(150, 0), defaultBorder);
        JPanel phases = createPanel(new FlowLayout(), Color.gray, new Dimension(150, 350), defaultBorder);
        JPanel resultCodeArea = createPanel(new BorderLayout(), null, new Dimension(650, 0), null);

        //Create result text area
        resultTextArea = new JTextArea(3,0);
        resultTextArea.setBorder(BorderFactory.createLineBorder(Color.darkGray,1));
        resultTextArea.setEditable(false);
        resultTextArea.setFont(new Font(null, Font.BOLD,13));

        //Create code text pane
        codeTextArea = new JTextArea();//
        codeTextArea.setBorder(defaultBorder);
        codeTextArea.setBackground(Color.lightGray);
        codeTextArea.setFont(new Font(null, Font.ITALIC,14));

        //Add both result and codeTextArea to resultCodeArea panel
        resultCodeArea.add(resultTextArea, BorderLayout.NORTH);
        resultCodeArea.add(codeTextArea);
        resultCodeArea.add(new JScrollPane(codeTextArea), BorderLayout.CENTER); // Align in the center with a scroll pane

        //Setup buttons
        openFileB = createStyledButton("Open File");
        lexicalAnalysisB = createStyledButton("Lexical Analysis");
        syntaxAnalysisB = createStyledButton("Syntax Analysis");
        semanticAnalysisB = createStyledButton("Semantic Analysis");
        clearB = createStyledButton("Clear");

        //Add phases label to phases panel for the phases menu
        JLabel phasesLabel = new JLabel("Compiler Phases:");
        phasesLabel.setFont(new Font("Helvetica", Font.BOLD,12));
        phasesLabel.setForeground(Color.black);

        phases.add(phasesLabel, FlowLayout.LEFT);
        phases.add(lexicalAnalysisB);
        phases.add(syntaxAnalysisB);
        phases.add(semanticAnalysisB);

        //Add buttons to the menuArea panel
        menuArea.add(openFileB);
        menuArea.add(phases, FlowLayout.CENTER);
        menuArea.add(clearB);

        JLabel credits = new JLabel("Created by: Clark Belen");
        credits.setFont(new Font(null, Font.ITALIC | Font.BOLD, 12));

        this.add(menuArea, BorderLayout.WEST);
        this.add(resultCodeArea, BorderLayout.CENTER);
        this.add(credits, BorderLayout.PAGE_END);

        //Add action listener for buttons
        addButtonListeners(openFileB, lexicalAnalysisB, syntaxAnalysisB, semanticAnalysisB, clearB);

        //Set initial state for components (Result text area, Code text area, Buttons)
        setInitialState();
    }

    //-----------------------Creation & Initiation methods-----------------------------------//
    private JPanel createPanel(LayoutManager layout, Color color, Dimension dimension, Border border){
        JPanel panel = new JPanel(layout);
        panel.setBackground(color);
        panel.setPreferredSize(dimension);
        panel.setBorder(border);
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setPreferredSize(new Dimension(140, 100)); // Set preferred size
        button.setFont(new Font("Helvetica", Font.BOLD, 14)); // Set font
        button.setBackground(Color.darkGray);
        button.setForeground(new Color(255, 197, 105));
        button.setFocusable(false);

        return button;
    }

    private void addButtonListeners(JButton... buttons) {
        for (JButton button : buttons) {
            button.addActionListener(this);
        }
    }

    //-----------------------Response to each action performed methods (per button)-----------------------------------//
    private void setInitialState(){ // Initial state by default for clear button
        resultTextArea.setText("  Result: \n\n~Result goes here~");
        resultTextArea.setBackground(Color.lightGray);

        codeTextArea.setText("[Open a text file containing codes]\n\n ~Code goes here~ ");
        codeTextArea.setEditable(false);
        codeTextArea.setCaretColor(Color.black);

        openFileB.setEnabled(true);
        lexicalAnalysisB.setEnabled(false);
        syntaxAnalysisB.setEnabled(false);
        semanticAnalysisB.setEnabled(false);
        clearB.setEnabled(false);
    }

    private void performLexicalA(){ // for lexical analysis button
        lexical = new LexicalAnalyzer(codeTextArea.getText()); //proceed to lexical analyzer

        if(lexical.tokenizer()){ //1st phase  if successful
            resultTextArea.setText("  Result:   SUCCESSFUL \n\n  LEXICAL Analysis:   Valid lexemes found.");

            resultTextArea.setBackground(new Color(102,255,102));

            codeTextArea.setEditable(false);
            lexicalAnalysisB.setEnabled(false);
            syntaxAnalysisB.setEnabled(true);

        }
        else { //1st phase if failed
            resultTextArea.setText("  Result:   FAILED \n\n  LEXICAL Analysis:   ERROR -> Unknown/Invalid lexeme found.");

            resultTextArea.setBackground(new Color(255,102,102));
            codeTextArea.setEditable(true);

            JOptionPane.showMessageDialog(null,"Note: You can edit the codes, then restart the analysis again.",
                    "LEXICAL ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void performSyntaxA() { // for syntax analysis button
        SyntaxAnalyzer syntax = new SyntaxAnalyzer(lexical.tokens, lexical.dtv);
        if(syntax.isSyntaxPassed()){//2nd phase if successful
            resultTextArea.setText("  Result:   SUCCESSFUL \n\n  SYNTAX Analysis:   Valid sequence of tokens per line");

            semanticAnalysisB.setEnabled(true);
        }
        else { //2nd phase if failed
            resultTextArea.setText("  Result:   FAILED \n\n  SYNTAX Analysis:   ERROR ->  Unknown / Invalid sequence of tokens found.");

            resultTextArea.setBackground(new Color(255,102,102));
            codeTextArea.setEditable(true);

            JOptionPane.showMessageDialog(null,"Note: You can edit the codes, then restart the analysis again.", "SYNTAX ERROR", JOptionPane.ERROR_MESSAGE);

        }
    }
    private void performSemanticA() { // for semantic analysis button
        SemanticAnalyzer semantic = new SemanticAnalyzer(lexical.dtv);
        if(semantic.noError){ //3rd phase if successful
            resultTextArea.setText("  Result:   SUCCESSFUL \n\n  SEMANTIC Analysis:   Values are valid.\n\n  Congrats! No Error at all.");

            System.out.println("Congrats! It's all good.");
        }
        else { //3rd phase if failed
            resultTextArea.setText("  Result:   FAILED \n\n  SEMANTIC Analysis:   ERROR -> Unknown / Invalid values are found");

            resultTextArea.setBackground(new Color(255,102,102));
            codeTextArea.setEditable(true);

            JOptionPane.showMessageDialog(null,"Note: You can edit the codes, then restart the analysis again.", "SEMANTIC ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //-----------------------Action Performed method-----------------------------------//
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(openFileB)) {

            openFile(); //The method which selects a file from JFile chooser, read the contents, and set it to code text area
            if(selectedFile != null){

                openFileB.setEnabled(false);
                lexicalAnalysisB.setEnabled(true);
                clearB.setEnabled(true);
                resultTextArea.setText("  Result:   Successfully opened a text file.\n\n  Proceed to Compiler Phases.");
                resultTextArea.setBackground(new Color(102,255,102));
            }
        }

        if (e.getSource().equals(lexicalAnalysisB)) { //1st phase - Lexical Analysis

            if(!codeTextArea.getText().isBlank()) {
                performLexicalA(); //perform lexical analysis
            }
            else {
                resultTextArea.setText("  Result:   FAILED \n\n  Empty codes are not accepted.");
                resultTextArea.setBackground(new Color(255,102,102));
                codeTextArea.setEditable(true);

                JOptionPane.showMessageDialog(null,"Note: You can insert valid codes inside the code text area, then restart the analysis again.", "CONTENT ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        if(e.getSource().equals(syntaxAnalysisB)){ //2nd phase - Syntax Analysis

            performSyntaxA(); //perform syntax analysis
            syntaxAnalysisB.setEnabled(false);
        }

        if(e.getSource().equals(semanticAnalysisB)){ //3rd phase - Semantic Analysis

            performSemanticA(); //perform semantic analysis
            semanticAnalysisB.setEnabled(false);
        }

        if(e.getSource().equals(clearB)){

            int response = JOptionPane.showConfirmDialog(null,
                    "Are you sure?",
                    "Confirmation message",
                    JOptionPane.YES_NO_OPTION);

            if(response==JOptionPane.YES_OPTION){
                setInitialState(); //reset back to all initial states
            }
        }

        codeTextArea.setText(codeTextArea.getText());
        resetAnalysis(); //reset the analysis phases only if error occurred during analysis
    }

    private void resetAnalysis() {
        if(codeTextArea.isEditable()){
            lexicalAnalysisB.setEnabled(true);
            codeTextArea.setBackground(Color.white);
        }
        else{
            codeTextArea.setBackground(Color.lightGray);
        }
    }

    //-----------------------Opening and Reading file methods-----------------------------------//
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt", "text");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Get the root path of the class
        String rootPath = StarterFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File rootDirectory = new File(rootPath);

        // Set the current directory of the file chooser
        fileChooser.setCurrentDirectory(rootDirectory);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            String content;
            try {
                content = readFileContent(selectedFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            codeTextArea.setText(content);
        }
        else {
            selectedFile = null;
        }
    }

    private String readFileContent(File selectedFile) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
        String line;
        while((line = reader.readLine()) != null){
            content.append(line).append("\n");
        }
        reader.close();

        return content.toString();
    }
}
