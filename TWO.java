/* 
This is our Main.
    1. The try-catch are for error catching and are placed inside a while-loop in
    order to grand the user the ability to re-try entering information.

    2. The user inputs the following:
            a) The matrix size MxN (LinesxColumns)
            
            b) The possitions of A and B at the start of the game.
            
            c) If he wants to block tiles 
            (if yes then he enters the coordinates of each tile he wishes to block)
            
            d) During the game, a number that shows what move he wants to make.
------------------------------------------- 
*/

//Imports.
import java.util.*;

//The Main Class.
class TWO{
    public static void main(String args[]){

        //Variables.
        Scanner scanner = new Scanner(System.in);       //a scanner for our inputs.
        String[] players = {"A","B"};                   //a list with the players.
        String blocks = "";
        int[] A = new int[2];                           //the possition of player A (start of the game).
        int[] B = new int[2];                           //the possition of player B (start of the game).
        int M=0;                                        //the lines of the matrix.
        int N=0;                                        //the columns of the matrix.
        int F=0;                                        //a value that if !=0 -> ends the game.
        boolean done=false;

        //Getting the matrix size.
        while(done==false){
            try{
                System.out.print("\n\nGive me a matrix size in the format 'Lines'x'Columns'(ex. 4x5): ");
                String size = scanner.nextLine();
                String[] token = size.split("x");
                M = Integer.valueOf(token[0]);      //lines.
                N = Integer.valueOf(token[1]);      //columns.
                done = true; 
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Please try again, your input was invalid.\n(proper ex. 4x5 <= 4 is the lines and 5 is the columns)");
                continue;
            }catch(NumberFormatException e){
                System.out.println("Please try again, your input was invalid.\n(proper ex. 4x5 <= 4 is the lines and 5 is the columns)");
                continue;
            }
        }

        //Getting the possitions of player A and B in the start of the game.
        done=false;     //reseting the value of done from true to false.
        for(String player : players){    
    
            if(player.equals("A")){ System.out.println("\nWhere should we place player A in your matrix?"); }
            else{ System.out.println("\nWhere should we place you - player B - in your matrix?"); }
        
            while(done==false){
                try{
                    System.out.println("lines start from 1 to "+M+", and columns from 1 to "+N+".");
                    System.out.print("\nWhere? (line,column): ");
                    String place = scanner.nextLine();
                    String[] tokens = place.split(",");
                    int x = Integer.valueOf(tokens[0]);
                    int y = Integer.valueOf(tokens[1]);
                    if(x<=M && y<=N && x>0 && y>0){
                        if(player.equals("A")){
                            A[0] = Integer.valueOf(tokens[0]);  //line.
                            A[1] = Integer.valueOf(tokens[1]);  //column.
                        }else{
                            B[0] = Integer.valueOf(tokens[0]);  //line.
                            B[1] = Integer.valueOf(tokens[1]);  //column.
                        }
                        done = true;
                    }else{
                        System.out.println("The matrix is smaller than that, please try again with a different input.");
                    }     
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("Please try again, your input was invalid.\n(proper ex. 4,5 <= 4 is the line and 5 is the column)");
                    continue;
                }catch(NumberFormatException e){
                    System.out.println("Please try again, your input was invalid.\n(proper ex. 4,5 <= 4 is the line and 5 is the column)");
                    continue;
                }
            }
            done=false;     //reseting the value of done from true to false.
        }
        
        Matrix m = new Matrix(M,N,A,B); //Initialising the matrix.
        
        //Checking if the user wants to block tiles before the start of the game.
        System.out.println("\nDo you wish to block tiles from your board before the game starts?");
        done=false;     //reseting the value of done from true to false.
        while(done==false){
            System.out.print("\n(Enter 'y' for yes and 'n' for no): ");
            blocks = scanner.nextLine();
            if(blocks.equals("n") || blocks.equals("y")){ done=true;}
        }

        //Getting the tiles the user wants to block. -> if he said he wanted to block tiles.
        if(blocks.equals("y")){
            System.out.println("\nGive me the blocked tiles in the format (line,column). \n When you are done, enter '$' !");
            while(true){
                try{
                    System.out.print("\n(line,column): ");
                    String block = scanner.nextLine();
                    if(block.equals("$")){ break; }
                    String[] tokens = block.split(",");
                    int x = Integer.valueOf(tokens[0]);
                    int y = Integer.valueOf(tokens[1]);
                    m.marker(x,y);
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("Please try again, your input was invalid.\n(proper ex. 4,5 <= 4 is the line and 5 is the column)");
                    continue;
                }catch(NumberFormatException e){
                    System.out.println("Please try again, your input was invalid.\n(proper ex. 4,5 <= 4 is the line and 5 is the column)");
                    continue;
                }
            }
        }

        Minimax playerA = new Minimax(m); //Initialising the minimax algorithm.

        //The game.
        System.out.println("---------------------------------------");
        System.out.println("---------------------------------------");
        System.out.println("\nNow we are ready to play!Let's begin!\n");
        m.matrixPrinter();
        System.out.println("---------------------------------------");
        System.out.println("---------------------------------------");
        while(true){
            System.out.println("  -> It is Player A's turn:");      //player A starts first.
            m = playerA.Amove(m);                                   //making A move with minimax.
            m.matrixPrinter();
            F = m.finish(false);                                         //checking if we have a winner.
            if(F != 0){ break; }
            System.out.println("---------------------------------------");
            System.out.println("---------------------------------------");
            System.out.println("  -> Now it is your turn: ");
            m.humanC();                                             //printing the options out for player B and executing his choices.
            m.matrixPrinter();
            F = m.finish(true);                                         //checking if we have a winner.
            if(F !=0){ break; }
        }

        //Printing the results.
        if(F == -1){ System.out.println("Looks like the winner is you! Pound it!"); }
        if(F == 1){ System.out.println("Oh no... You lost.\nDon't worry you will get him next time!"); }

        scanner.close();    //closing the scanner.
    }
}