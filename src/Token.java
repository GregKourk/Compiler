
package parser;

/**
 *
 * @author Γρηγόρης
 */
public class Token {
    
    public TokenType type;
    public String data;
    public int line;

    public Token(TokenType type, String data, int line) {
        this.type = type;
        this.data = data;
        this.line = line;
    }

    @Override
    public String toString() {
        return String.format("(%s %s %d)", type.name(), data, line);
    }
    
    public String getTokenData()
    {
        return this.data;
    }
        
}
