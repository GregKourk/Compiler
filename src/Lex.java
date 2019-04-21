
package parser;

/**
 *
 * @author Γρηγόρης
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Lex {
    
    private ArrayList<Token> tokens;

    public Lex(String filename){

        String input=null;
        try {
            input = new Scanner(new File(filename)).useDelimiter("\\A").next();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tokens = new ArrayList<>();
        int lineCount=1;

        StringBuilder tokenPatternsBuffer = new StringBuilder();
        for (TokenType tokenType : TokenType.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        Pattern tokenPatterns = Pattern.compile(tokenPatternsBuffer.substring(1));
        
        Matcher matcher = tokenPatterns.matcher(input);
       
        while (matcher.find())            
            for(TokenType token : TokenType.values())
                if(matcher.group(token.name()) != null){
                    if (null != token.name())
                        switch (token.name()) {
                            case "unknownTK":
                                System.out.format("ERROR - Unknown Symbol at line %d: %s\n",lineCount,matcher.group(token.name()));
                                System.exit(0);
                                break;
                            case "digitwordTK":
                                System.out.format("ERROR - Unknown Word at line %d: %s\n",lineCount,matcher.group(token.name()));
                                System.exit(0);
                                break;
                            case "newlineTK":
                                lineCount++;
                                break;
                            case "whitespaceTK":
                                ;
                                break;
                            default:
                                tokens.add(new Token(token , matcher.group(token.name()), lineCount));
                                break;
                        }
                }
        
    }


    public ArrayList<Token> getTokens() {
        return tokens;
    }
    
    public Token nextToken(){
        return tokens.remove(0);
    }
     
}
