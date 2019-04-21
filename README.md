# Compiler
Small program to test functionality of given compiler.


Grammar of program language that is testet throught this project is the one following:

<PROGRAM>		      ::= program <DECLARATIONS> <STATEMENTS> endprogram
<DECLARATIONS>	  ::= ( declare <VARLIST> enddeclare )*
<VARLIST>		      ::= integer variable ( , variable )* | 
			            real variable  ( , variable )*
<STATEMENTS	      ::= <STATEMENT> ( ; <STATEMENT> )*
<STATEMENT>	      ::= input <INPUT-TAIL> |
    		          print <PRINT-TAIL> |
			            variable <ASSIGNT-TAIL> 
<INPUT-TAIL>	    ::= ( variable )
<PRINT-TAIL>	    ::= ( <EXPRESSION> )
<ASSIGN-TAIL>	    ::= := <EXPRESSION>
<EXPRESSION>	    ::= < SIGN> <TERM> ( + <TERM> | - <TERM> )* 
<TERM> 		        ::= <FACTOR> ( * <FACTOR> | / <FACTOR> )*
<FACTOR>		      ::= numerical_constant | 
			            variable |
			            ( <EXPRESSION> ) 
<SIGN> 		        ::= ε | + | - 

