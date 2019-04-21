
package parser;

/**
 *
 * @author Γρηγόρης
 */

import java.util.ArrayList;
import java.util.HashMap;
import static parser.TokenType.integerTK;


public class Parser {
    private Lex lex;
    private Token token;
    private Token tempToken;                                                    //Αντικείμενο τύπου Token για την προσωρινη αποθηκευση στοιχείων Token (υποερώτημα 5)
    
    private static boolean variableDeclaredMoreThanOnce=false;                  //μεταβλητη για ελεγχο διπλης δηλωσης καποιας μεταβλητης
    private static boolean variableDeclaredMoreThanOnceToBePrinted=false;       //μεταβλητή για σωστή εκτύπωση μηνύματος διπλής δήλωσης μεταβλητής
    private static boolean variableUsedAndDeclared=false;                       //μεταβλητή σχετικά με την ύπαρξη μεταβλητής που έχει χρησιμοποιηθεί και έχει δηλωθεί
    private static boolean variableUsedAfterHadGivenValue=false;                //μεταβλητή σχετικά με την ύπαρξη μεταβλητής που έχει χρησιμοποιηθεί ενώ δεν έχει γίνει εκχώρηση τιμής πρώτα
    private static boolean variableDeclaredInteger=false;                       //μεταβλητη σχετικα με αν μια συγκεκριμενη μεταβλητή εχει δηλωθει ως ακέραια
    private static boolean expressionWithDivine=false;                          //μεταβλητή σχετικά με το αν μια έκφραση περιέχει τουλάχιστον μία διαίρεση
    
    private static int intVariableCounter=0;                                    //μετρητης για τις μεταβλητες που δηλωνονται και ειναι ακεραιες     
    private static int realVariableCounter=0;                                   //μεταβλητη για τις μεταβλητες που δηλωνονται και ειναι πραγματικες
    private static int counterForMultipleDeclarations=0;                        //μετρητης για τις εμφανισεις μια μεταβλητης
    
    static ArrayList <String> variablesUsedAndDeclared;                         //ArrayList οπου κραταμε ολες τις μεταβλητες που εχουν δηλωθει
    static ArrayList<String> variablesForUseAfterGivenValue;                    //ArrayList που κρατάμε όλες τις μεταβλητές στις οποίες έχει γίνει εκχώρηση τιμής πριν την χρήση τους
    static ArrayList<String> variablesDeclaredInteger;                          //ArrayList που κραταμε όλες τις μεταβλητες που είναι δηλωμενες ως integer
    
    static HashMap <String, Integer> variablesWithMultipleDeclarations;         //δομη τυπου HashMap που κραταει ποιες μεταβλητες εχουν δηλωθει πολλες φορες και ποσες ειναι αυτες οι φορες (υποερωτημα 2)
    static HashMap <String, Integer> variablesUsedButNoDeclared;                //δομη τυπου HashMap που κραταει ποιες μεταβλητες εχουν χρησιμοποιηθει στο προγραμμα χωρις να εχουν δηλωθει προηγουμενως στο τμημα δηλωσεων (υποερωτημα 3)
    static HashMap <String, Integer> variablesUsedBeforeGiveValue;              //δομη τυπου HashMap που κραταει ποιες μεταβλητες χρησιμοποιουνται πριν γινει σε αυτες εκχωρηση τιμης (υποερωτημα 4)   
    static HashMap <String, Integer> variablesDeclaredIntegerThatHasDivine;     //δομη τυπου HashMap που κραταει τις μεταβλητες που ενω ειναι δηλωμενες ως ακεραιες εχουν μεσα στην εκφραση τους πραξη διαιρεσης (υποερωτημα 5)  
    
    public Parser(String filename)
    {
        this.variablesUsedAndDeclared = new ArrayList<>();
        this.variablesForUseAfterGivenValue = new ArrayList<>();
        this.variablesDeclaredInteger = new ArrayList<>();
        
        this.variablesWithMultipleDeclarations = new HashMap<>();
        this.variablesUsedButNoDeclared = new HashMap<>();
        this.variablesUsedBeforeGiveValue = new HashMap<>();
        this.variablesDeclaredIntegerThatHasDivine = new HashMap<>();
        
        lex = new Lex(filename);
        System.out.println(lex.getTokens());
        
        token = lex.nextToken();
        program();  
    }
    
    
       
    private void error(String s){
        System.out.format("error in line %d: %s\n",token.line,s);
        System.exit(0);
    }
    
    private void program() {
        if (token.type.name()=="programTK"){
            token = lex.nextToken();
            declarations();
            statements();
            if (token.type.name()=="endprogramTK"){
                token = lex.nextToken();
            }
            else error("endprogram keyword expected");       
        }
        else error("programs start with the keyword program");
        if (token.type.name()!="eofTK")
            error("end of file expected");
    }
    
    //****************My code**********************//
    public void declarations(){
        while (token.type.name()=="declareTK"){             //Όσο ξεκινάει με την λέξη declare εκτελούμε τα παρακάτω βήματα
            token = lex.nextToken();                        //Πάμε στο επόμενο 
            varlist();                                      //Εκτέλεση ρουτίνας varlist()
            if (token.type.name()=="enddeclareTK"){         //Αν τελειώνει με την λέξη enddeclare
                token = lex.nextToken();                    //Πάμε στο επόμενο token
            }
            else error("enddeclare keyword expected");      //Εμφάνιση μηνύματος λάθους αν δεν έχουμε τη λέξη enddeclare στο τέλος
        }
    }
    //*************End of my code******************//
    
    
    //****************My code**********************//
    public void varlist(){
       if (token.type.name()=="integerTK"){                 //Αν έχουμε τη λέξη integer
           token = lex.nextToken();                         //Πάμε στο επόμενο token
           if (token.type.name()=="variableTK"){            //Αν έχουμε μεταβλητή
               
               variableDeclaredMoreThanOnce=false;          //Θέτω την λογική μεταβλητή σε false για να μπορώ να ελέγξω την διπλή ύπαρξη κάποιας μεταβλητής
               
               counterForMultipleDeclarations = 1;                                                              //Αρχικοποίηση μετρητή σχετικά με τις πολλαπλές εμφανίσεις μεταβλητής
               if (!variablesWithMultipleDeclarations.containsKey(token.getTokenData())){                       //Αν δεν περιέχεται η μεταβλητή στο HashMap, τότε εμφανίζεται πρώτη φορά
               variablesWithMultipleDeclarations.put(token.getTokenData(), counterForMultipleDeclarations);     //Τοποθετώ την μεταβλητή στο HashMap με τον μετρητή να έχει τιμή 0
               }else{                                                                                           //Αν περιέχεται η μεταβλητή στο HashMap, αυξάνω τις ήδη εμφανίσεις κατά 1
               counterForMultipleDeclarations = variablesWithMultipleDeclarations.get(token.getTokenData());    //Βρίσκω πόσες εμφανίσεις έχει ήδη η μεταβλητή
               counterForMultipleDeclarations++;                                                                //Αυξάνω τις εμφανίσεις κατά 1
               variablesWithMultipleDeclarations.put(token.getTokenData(), counterForMultipleDeclarations);     //Μεταβάλλω τις εμφανίσεις της μεταβλητής με την νέα τιμή
               }
                      
               for (int i=0;i<variablesUsedAndDeclared.size();i++){                        //Ελέγχω όλη την ArrayList για να δω αν υπάρχει ήδη κάποια μεταβλητή με αυτό όνομα. Αν υπάρχει δεν την προσθέτω εκ νέου
                    if (variablesUsedAndDeclared.get(i).equals(token.getTokenData())){
                        variableDeclaredMoreThanOnce=true;                                 //Αν βρω ίδια μεταβλητή κάνω την λογική μεταβλητή σε true
                    } 
               }
               if (variableDeclaredMoreThanOnce!=true){                                    //Αν σε όλη την ArrayList δεν έχω βρει 2 ίδιες μεταβλητές τότε την προσθέτω στην ArrayList και αυξάνω τον μετρητή
                   variablesUsedAndDeclared.add(token.getTokenData());
                   intVariableCounter++;
               }
               
               variablesDeclaredInteger.add(token.getTokenData());      //Προσθηκη της μεταβλητη στην ArrayList
               
               token = lex.nextToken();                     //Πάμε στο επόμενο token
               while (token.type.name()=="commaTK"){        //Όσο έχουμε κόμμα 
                   token = lex.nextToken();                 //Πάμε στο επόμενο token
                   if (token.type.name()=="variableTK"){    //Αν έχουμε μεταβλητή
                       
                       variableDeclaredMoreThanOnce=false;                          //Θέτω την λογική μεταβλητή σε false για να μπορώ να ελέγξω την διπλή ύπαρξη κάποιας μεταβλητής
                       
                       counterForMultipleDeclarations = 1;                                                                  //Αρχικοποίηση μετρητή σχετικά με τις πολλαπλές εμφανίσεις μεταβλητής
                       if (!variablesWithMultipleDeclarations.containsKey(token.getTokenData())){                           //Αν δεν περιέχεται η μεταβλητή στο HashMap, τότε εμφανίζεται πρώτη φορά
                          variablesWithMultipleDeclarations.put(token.getTokenData(), counterForMultipleDeclarations);      //Τοποθετώ την μεταβλητή στο HashMap με τον μετρητή να έχει τιμή 0
                       }else{                                                                                               //Αν περιέχεται η μεταβλητή στο HashMap, αυξάνω τις ήδη εμφανίσεις κατά 1
                          counterForMultipleDeclarations = variablesWithMultipleDeclarations.get(token.getTokenData());     //Βρίσκω πόσες εμφανίσεις έχει ήδη η μεταβλητή
                          counterForMultipleDeclarations++;                                                                 //Αυξάνω τις εμφανίσεις κατά 1
                          variablesWithMultipleDeclarations.put(token.getTokenData(), counterForMultipleDeclarations);      //Μεταβάλλω τις εμφανίσεις της μεταβλητής με την νέα τιμή
                       }
                      
                       for (int i=0;i<variablesUsedAndDeclared.size();i++){        //Ελέγχω όλη την ArrayList για να δω αν υπάρχει ήδη κάποια μεταβλητή με αυτό όνομα. Αν υπάρχει δεν την προσθέτω εκ νέου
                            if (variablesUsedAndDeclared.get(i).equals(token.getTokenData())){                                
                                variableDeclaredMoreThanOnce=true;                 //Αν βρω ίδια μεταβλητή κάνω την λογική μεταβλητή σε true
                            } 
                       }
                       if (variableDeclaredMoreThanOnce!=true){                    //Αν σε όλη την ArrayList δεν έχω βρει 2 ίδιες μεταβλητές τότε την προσθέτω στην ArrayList και αυξάνω τον μετρητή
                           variablesUsedAndDeclared.add(token.getTokenData());
                           intVariableCounter++;
                       }
                       
                       variablesDeclaredInteger.add(token.getTokenData());      //Προσθηκη της μεταβλητη στην ArrayList
                       
                       token = lex.nextToken();                                 //Πάμε στο επόμενο token
                    }
                   else error("variable expected");                             //Εμφάνιση μηνύματος λάθους αν δεν έχουμε μεταβλητή
               }
           }
           else error("variable expected");                                     //Εμφάνιση μηνύματος λάθους αν δεν έχουμε μεταβλητή
       }
       else if (token.type.name()=="realTK"){                                   //Αν έχουμε τη λέξη real
           token = lex.nextToken();                                             //Πάμε στο επόμενο token
           if (token.type.name()=="variableTK"){                                //Αν έχουμε μεταβλητή
               
               variableDeclaredMoreThanOnce=false;                                                                  //Θέτω την λογική μεταβλητή σε false για να μπορώ να ελέγξω την διπλή ύπαρξη κάποιας μεταβλητής
               
               counterForMultipleDeclarations = 1;                                                                  //Αρχικοποίηση μετρητή σχετικά με τις πολλαπλές εμφανίσεις μεταβλητής
               if (!variablesWithMultipleDeclarations.containsKey(token.getTokenData())){                           //Αν δεν περιέχεται η μεταβλητή στο HashMap, τότε εμφανίζεται πρώτη φορά
                   variablesWithMultipleDeclarations.put(token.getTokenData(), counterForMultipleDeclarations);     //Τοποθετώ την μεταβλητή στο HashMap με τον μετρητή να έχει τιμή 0
               }else{                                                                                               //Αν περιέχεται η μεταβλητή στο HashMap, αυξάνω τις ήδη εμφανίσεις κατά 1
                   counterForMultipleDeclarations = variablesWithMultipleDeclarations.get(token.getTokenData());    //Βρίσκω πόσες εμφανίσεις έχει ήδη η μεταβλητή
                   counterForMultipleDeclarations++;                                                                //Αυξάνω τις εμφανίσεις κατά 1
                   variablesWithMultipleDeclarations.put(token.getTokenData(), counterForMultipleDeclarations);     //Μεταβάλλω τις εμφανίσεις της μεταβλητής με την νέα τιμή
               }
                      
               for (int i=0;i<variablesUsedAndDeclared.size();i++){                     //Ελέγχω όλη την ArrayList για να δω αν υπάρχει ήδη κάποια μεταβλητή με αυτό όνομα. Αν υπάρχει δεν την προσθέτω εκ νέου
                   if (variablesUsedAndDeclared.get(i).equals(token.getTokenData())){
                       variableDeclaredMoreThanOnce=true;                               //Αν βρω ίδια μεταβλητή κάνω την λογική μεταβλητή σε true
                   } 
               }
               if (variableDeclaredMoreThanOnce!=true){                                 //Αν σε όλη την ArrayList δεν έχω βρει 2 ίδιες μεταβλητές τότε την προσθέτω στην ArrayList και αυξάνω τον μετρητή
                    variablesUsedAndDeclared.add(token.getTokenData());
                    realVariableCounter++;
               }
               
               token = lex.nextToken();                                         //Πάμε στο επόμενο token
               while (token.type.name()=="commaTK"){                            //Όσο εχουμε κόμμα 
                   token = lex.nextToken();                                     //Πάμε στο επόμενο token
                   if (token.type.name()=="variableTK"){                        //Αν έχουμε μεταβλητή
                      variableDeclaredMoreThanOnce=false;                                              //Θέτω την λογική μεταβλητή σε false για να μπορώ να ελέγξω την διπλή ύπαρξη κάποιας μεταβλητής
                      
                      counterForMultipleDeclarations = 1;                                                                   //Αρχικοποίηση μετρητή σχετικά με τις πολλαπλές εμφανίσεις μεταβλητής
                      if (!variablesWithMultipleDeclarations.containsKey(token.getTokenData())){                            //Αν δεν περιέχεται η μεταβλητή στο HashMap, τότε εμφανίζεται πρώτη φορά
                          variablesWithMultipleDeclarations.put(token.getTokenData(), counterForMultipleDeclarations);      //Τοποθετώ την μεταβλητή στο HashMap με τον μετρητή να έχει τιμή 0
                      }else{                                                                                                //Αν περιέχεται η μεταβλητή στο HashMap, αυξάνω τις ήδη εμφανίσεις κατά 1
                          counterForMultipleDeclarations = variablesWithMultipleDeclarations.get(token.getTokenData());     //Βρίσκω πόσες εμφανίσεις έχει ήδη η μεταβλητή
                          counterForMultipleDeclarations++;                                                                 //Αυξάνω τις εμφανίσεις κατά 1
                          variablesWithMultipleDeclarations.put(token.getTokenData(), counterForMultipleDeclarations);      //Μεταβάλλω τις εμφανίσεις της μεταβλητής με την νέα τιμή
                      }
                      
                       for (int i=0;i<variablesUsedAndDeclared.size();i++){                     //Ελέγχω όλη την ArrayList για να δω αν υπάρχει ήδη κάποια μεταβλητή με αυτό όνομα. Αν υπάρχει δεν την προσθέτω εκ νέου
                            if (variablesUsedAndDeclared.get(i).equals(token.getTokenData())){
                                variableDeclaredMoreThanOnce=true;                              //Αν βρω ίδια μεταβλητή κάνω την λογική μεταβλητή σε true
                             
                            } 
                       }
                       if (variableDeclaredMoreThanOnce!=true){                                 //Αν σε όλη την ArrayList δεν έχω βρει 2 ίδιες μεταβλητές τότε την προσθέτω στην ArrayList και αυξάνω τον μετρητή
                           variablesUsedAndDeclared.add(token.getTokenData());
                           realVariableCounter++;
                       }
                       
                       token = lex.nextToken();                                 //Πάμε στο επόμενο token
                   }
                   else error("variable expected");                             //Εμφάνιση μηνύματος λάθους αν δεν έχουμε μεταβλητή
               }
           }
           else error("variable expected");                                     //Εμφάνιση μηνύματος λάθους αν δεν έχουμε μεταβλητή
       }
       else error("integer or real expected");                                  //Αν δεν καμία από τις λέξεις integer,real σαν είσοδο, εμφάνιση μηνύματος λάθους
    }
    //*************End of my code******************//

    
    public void statements(){
        statement();
        while (token.type.name()=="semicolTK"){
            token = lex.nextToken();
            statement();
        }    
    }

    //****************My code**********************//
    public void statement(){
        if (token.type.name()=="inputTK"){              //Αν έχουμε τη λέξη input
            token = lex.nextToken();                    //Πάμε στο επόμενο token
            input_tail();                               //Εκτέλεση ρουτίνας input_tail()
        }
        else if (token.type.name()=="printTK"){         //Αν έχουμε τη λέξη print
            token = lex.nextToken();                    //Πάμε στο επόμενο token
            print_tail();                               //Εκτέλεση ρουτίνας print_tail()
        }
        else if (token.type.name()=="variableTK"){      //Αν έχουμε μεταβλητή 
            
            variableUsedAndDeclared=false;                                      //Αρχικοποιώ την μεταβλητή σχετικά με την χρήση μεταβλητής ενώ έχει δηλωθεί κανονικά
            for (int i=0;i<variablesUsedAndDeclared.size();i++){                //Κάθε μεταβλητή που έχει δηλωθεί την συγκρίνω με την τρέχουσα και αν έχει δηλωθεί κανονικά κάνω την boolean σε true
               if (variablesUsedAndDeclared.get(i).equals(token.getTokenData())){
                    variableUsedAndDeclared=true;
               }
            }
            if (variableUsedAndDeclared==false){                            //Αν τελικά δεν έχει δηλωθεί την προσθέτω στο αντίστοιχο HashMap, βάζοντας το όνομα και την γραμμή που χρησιμοποιείται 
               variablesUsedButNoDeclared.put(token.getTokenData(), token.line);
            }
            
            variablesForUseAfterGivenValue.add(token.getTokenData());   //Προσθέτω στην ArrayList την μεταβλητή, καθώς σε αυτό το σημείο στην μεταβλητή εκχωρείται τιμή, μέσω του κανόνα εκχώρησης
            
            
            variableDeclaredInteger=false;                                          //Αρχικοποιω την μεταβλητη σχετικα με το αν η μεταβλητη είναι ακέραια
            for (int i=0; i<variablesDeclaredInteger.size(); i++){                  //Ελέγχω την τρεχουσα μεταβλητη με κάθε μεταβλητη που έχει δηλωθεί ως ακεραια στην αρχη του προγραμματος, 
                if (variablesDeclaredInteger.get(i).equals(token.getTokenData())){  //Αν η τρεχουσα εχει δηλωθει ως ακεραια τοτε βαζω την αντιστοιχη μεταβλητη boolean σε true
                    variableDeclaredInteger=true;
                }
            }
            if (variableDeclaredInteger==true){                                 //Αν η τρεχουσα μεταβλητη ειναι ακεραια τοτε δημιουργω ενα προσωρινο Token με τα ιδια στοιχεια, 
                tempToken = new Token(token.type, token.data, token.line);      //και βάζω την μεταβλητή σχετικα με την υπαρξη διαιρεσης σε false
                expressionWithDivine=false;
            }
            
            token = lex.nextToken();                    //Πάμε στο επόμενο token
            assign_tail();                              //Εκτέλεση ρουτίνας assing_tail()
            
            if(expressionWithDivine==true && variableDeclaredInteger==true){                            //Σε αυτο το σημειο εχει γινει ελεγχος αν υπαρχει τουλαχιστον μια διαιρεση στην εκφραση της μεταβλητης, 
                variablesDeclaredIntegerThatHasDivine.put(tempToken.getTokenData(), tempToken.line);    //αν υπαρχει και η μεταβλητη ειναι ακεραια τοτε την προσθετω στο αντιστοιχο HashMap
            }
            
        }
        else error("input, print or variable expected");                                                //Εμφάνιση μηνύματος λάθους αν δεν έχω καμία από τις επιθυμητές εισόδους
    }
    //*************End of my code******************//

    
    public void input_tail(){
        if (token.type.name()=="leftpTK"){
            token = lex.nextToken();
            if (token.type.name()=="variableTK"){
                
                variableUsedAndDeclared=false;                                  //Αρχικοποιώ την μεταβλητή σχετικά με την χρήση μεταβλητής ενώ έχει δηλωθεί κανονικά
                for (int i=0;i<variablesUsedAndDeclared.size();i++){            //Κάθε μεταβλητή που έχει δηλωθεί την συγκρίνω με την τρέχουσα και αν έχει δηλωθεί κανονικά κάνω την boolean σε true
                    if (variablesUsedAndDeclared.get(i).equals(token.getTokenData())){
                        variableUsedAndDeclared=true;
                    }
                }
                if (variableUsedAndDeclared==false){                            //Αν τελικά δεν έχει δηλωθεί την προσθέτω στο αντίστοιχο HashMap, βάζοντας το όνομα και την γραμμή που χρησιμοποιείται 
                variablesUsedButNoDeclared.put(token.getTokenData(), token.line);
                }
                
                variablesForUseAfterGivenValue.add(token.getTokenData());       //Προσθέτω στην ArrayList την μεταβλητή, καθώς σε αυτό το σημείο στην μεταβλητή εκχωρείται τιμή, μέσω της εντολής input 
                
                token = lex.nextToken();
                if (token.type.name()=="rightpTK")
                    token = lex.nextToken();
                else error("right parenthesis exprected");
            }
            else error("variable expected after opening parenthesis"); 
        }
        else error("left parenthesis exprected");
    }

    //****************My code**********************//
    public void print_tail(){
        if (token.type.name()=="leftpTK"){              //Αν ξεκινάει με αριστερή παρένθεση    
            token = lex.nextToken();                    //Πάμε στο επόμενο token                       
            expression();                               //Εκτέλεση ρουτίνας expression()
            if (token.type.name()=="rightpTK")          //Αν έχουμε δεξιά παρένθεση
                token = lex.nextToken();                //Πάμε στο επόμενο token
            else error("right parenthesis expected");   //Εμφάνιση μηνύματος λάθους αν δεν έχουμε δεξιά παρένθεση    
        }
        else error("left parenthesis expected");        //Εμφάνιση μηνύματος λάθους αν δεν έχουμε αριστερή παρένθεση    
    }
    //*************End of my code******************//


    public void assign_tail(){
        if (token.type.name()=="assignTK"){
            token = lex.nextToken();
            expression();
        } else error("assignment symbol expected after variable");
    }
                        
    public void expression(){
        sign();
        term();
        while (token.type.name()=="plusTK" || token.type.name()=="minusTK"){
            token = lex.nextToken();
            term();
        }
    }
               
    public void term(){
        factor();
        
        while (token.type.name()=="multTK" || token.type.name()=="divTK"){
            
            if (token.type.name()=="divTK"){        //Αν περιεχει μια διαιρεση κανω την boolean σε true
                expressionWithDivine=true;
            }
            
            token = lex.nextToken();
            factor();
        }
    }
     
    //****************My code**********************//
    public void factor(){                               
        //Επειδή το numerical_constant μπορεί να πάρει τιμές και ακέραιες και πραγματικές 
        //βάζω 2 υποπεριπτώσεις για να έχουμε και τα 2 ενδεχόμενα
        if (token.type.name()=="realconstTK")           //Αν έχουμε πραγματική τιμή
            token = lex.nextToken();                    //Πάμε στο επόμενο token
        else if (token.type.name()=="integerconstTK")   //Αν έχουμε ακέραια τιμή
            token = lex.nextToken();                    //Πάμε στο επόμενο token
        else if (token.type.name()=="variableTK"){       //Αν έχουμε μεταβλητή
            
            variableUsedAndDeclared=false;                                      //Αρχικοποιώ την μεταβλητή σχετικά με την χρήση μεταβλητής ενώ έχει δηλωθεί κανονικά
            for (int i=0;i<variablesUsedAndDeclared.size();i++){                //Κάθε μεταβλητή που έχει δηλωθεί την συγκρίνω με την τρέχουσα και αν έχει δηλωθεί κανονικά κάνω την boolean σε true
                if (variablesUsedAndDeclared.get(i).equals(token.getTokenData())){
                    variableUsedAndDeclared=true;
                }
            }
            if (variableUsedAndDeclared==false){                                //Αν τελικά δεν έχει δηλωθεί την προσθέτω στο αντίστοιχο HashMap, βάζοντας το όνομα και την γραμμή που χρησιμοποιείται 
                variablesUsedButNoDeclared.put(token.getTokenData(), token.line);
            }
            
            variableUsedAfterHadGivenValue=false;                               //Αρχικοποιώ την μεταβλητή σχετικά με την χρήση μεταβλητής μετά την εκχώρηση τιμής σε false
            for (int i=0; i<variablesForUseAfterGivenValue.size(); i++){        //Ελέγχω κάθε μεταβλητή στην οποία έχει γίνει εκχώρηση τιμής ήδη, αν είναι ίδια με αυτή που υπάρχει σε αυτό το σημείο. Αν ταιριάζει έχει γίνει σωστή χρήση της. Αν δεν υπάρχει μεταβλητή με αυτό το όνομα, σημαίνει ότι χρησιμοποιείται χωρίς να έχει προηγηθεί εκχώρησης τιμής και προστίθεται στο ανάλογο HashMap
                if (variablesForUseAfterGivenValue.get(i).equals(token.getTokenData())){
                    variableUsedAfterHadGivenValue=true;
                }
            }
            if (variableUsedAfterHadGivenValue==false){
                variablesUsedBeforeGiveValue.put(token.getTokenData(), token.line);
            }
               
            token = lex.nextToken();                    //Πάμε στο επόμενο token
        }                    
        else if (token.type.name()=="lefttpTK"){        //Αν έχουμε αριστερή παρένθεση
           token = lex.nextToken();                     //Πάμε στο επόμενο token
           expression();                                //Εκτέλεση ρουτίνας expression()
           if (token.type.name()=="righttpTK")          //Αν έχουμε δεξιά παρένθεση
               token = lex.nextToken();                 //Πάμε στο επόμενο token
           else error("right parenthesis expected");    //Εμφάνιση μηνύματος λάθους αν δεν έχουμε δεξιά παρένθεση
        }
        else error("real, integer, variable or left parenthesis expected"); //Εμφάνιση μηνύματος λάθους αν δεν έχω κάποια από τις επιτρεπτές εισόδους
    }
    //*************End of my code******************//

    
    public void sign(){
        if (token.type.name()=="plusTK" || token.type.name()=="minusTK")
            token = lex.nextToken();
    }
       
    
    public static void main(String args[]){

        Parser parser = new Parser("test2.txt");
        
        System.out.println("\nTotal variables declared: " + (intVariableCounter+realVariableCounter));
        System.out.println("\t" + intVariableCounter + " of them are integer");
        System.out.println("\t" + realVariableCounter + " of them are real" );
               
        if (!variablesWithMultipleDeclarations.isEmpty()){                                     //Αν το HashMap δεν είναι κενό, 
            System.out.println("\nVariables that have been declared more than once:");         //δηλαδή αν έχει υπάρξει έστω και μία μεταβλητή που έχει δηλωθεί πάνω από μία φορά   
            for (Object objectName: variablesWithMultipleDeclarations.keySet()){               //Για κάθε αντικείμενο του HashSet εκτυπώνω το όνομα της μεταβλητής και το πλήθος των δηλώσεων του αν είναι πάνω από 1 φορά
                if (variablesWithMultipleDeclarations.get(objectName)>1){
                    System.out.println("\tVariable: " + objectName + "\t\t,times of declaration: " + variablesWithMultipleDeclarations.get(objectName));
                }
            }
        }
        
        if (!variablesUsedButNoDeclared.isEmpty()){                                             //Αν το HashMap δεν είναι κενό, 
            System.out.println("\nVariables that are used but are not declared first are:");    //δηλαδή αν έχει υπάρξει έστω και μία μεταβλητή που έχει χρησιμοποιηθεί ενώ δεν έχει δηλωθεί
            for (Object objectName: variablesUsedButNoDeclared.keySet()){                       //Για κάθε αντικείμενο του HashSet εκτυπώνω το όνομα της μεταβλητής και την γράμμη που έχει εμφανιστεί
                System.out.println("\t Variable: " + objectName + "\t\t,line that is used: " + variablesUsedButNoDeclared.get(objectName));
            }
        }
        
        if(!variablesUsedBeforeGiveValue.isEmpty()){                                            //Αν το HashMap δεν είναι κενό, 
            System.out.println("\nVariables that are used before they had given value are:");   //δηλαδή αν έχει υπάρξει έστω και μία μεταβλητή που έχει χρησιμοποιηθεί ενώ δεν έχει γίνει εκχώρηση τιμής πρώτα
            for (Object objectName: variablesUsedBeforeGiveValue.keySet()){                     //Για κάθε αντικείμενο του HashSet εκτυπώνω το όνομα της μεταβλητής και την γράμμη που έχει εμφανιστεί
                System.out.println("\t Variable: " + objectName + "\t\t,line that is used: " + variablesUsedBeforeGiveValue.get(objectName));
            }
        }
        
        if(!variablesDeclaredIntegerThatHasDivine.isEmpty()){                                                       //Αν το HashMap δεν ειναι κενό,
            System.out.println("\nVariables that are integers and they include divine in their expression are:");   //δηλαδή αν υπάρχει έστω και μια μεταβλητή που είναι ακέραια αλλά περιέχει πράξη διαίρεσης
            for (Object objectName: variablesDeclaredIntegerThatHasDivine.keySet()){                       //Για κάθε αντικείμενο του HashSet εκτυπώνω το όνομα της μεταβλητής και την γράμμη που έχει εμφανιστεί
                System.out.println("\t Variable: " + objectName + "\t\t,line that is used: " + variablesDeclaredIntegerThatHasDivine.get(objectName));
            }
        }
                
        System.out.println("\n\nsyntactically correct program");
    }
}

