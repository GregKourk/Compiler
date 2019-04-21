
package parser;

/**
 *
 * @author Γρηγόρης
 */
public enum TokenType {
        whitespaceTK("[ \t\f\r]+"),
        newlineTK("\n"),
        realconstTK("-?\\d+\\.\\d+"),
        integerconstTK("-?\\d+"),
        programTK("program"),
        declareTK("declare"),
        enddeclareTK("enddeclare"),
        endprogramTK("endprogram"),
        realTK("real"),
        integerTK("integer"),
        inputTK("input"),       
        printTK("print"),
        variableTK("[a-zA-Z]\\w*"),
        plusTK("\\+"),
        minusTK("\\-"),
        multTK("\\*"),
        divTK("\\/"),
        leftpTK("\\("),
        rightpTK("\\)"),
        assignTK(":="),     
        semicolTK(";"),
        commaTK(","),
        unknownTK("."),
        eofTK("\\Z"),
        ;

        public final String pattern;

        private TokenType(String pattern) {
            this.pattern = pattern;
        }    
}
