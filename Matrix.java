/* 
Here are some things that are important for one who wants to read this code.
we mark the matrix tiles as: 
                        1. unvisited (0)
                        2. visited (1)
                        3. has player A (10)
                        4. has player B (-10)

we also mark the possible moves as: (in dictionary "directions")
                        0: Up left 1 tile                    8: Up left 2 tiles
                        1: Up tile                           9: Up 2 tiles
                        2: Up right 1 tile                   10: Up right 2 tiles
                        3: left 1 tile                       11: left 2 tiles
                        4: right 1 tile                      12: right 2 tiles
                        5: down left 1 tile                  13: down left 2 tiles
                        6: down 1 tile                       14: down 2 tiles
                        7: down right 1 tile                 15: down right 2 tiles

and we will be refering to A and B as: (in methods that we will need to differentiate them)
						1. A = true.
						2. B = false.
---------------------------------------------------
*/

//Imports.
import java.util.*;

//Matrix Class.
class Matrix{

    //Private Fields.
    private int lines, columns;                                                         		//the number of lines and columns of the matrix.
    private int Aline, Acolumn;                                                         		//the coordinates for A.
    private int Bline, Bcolumn;                                                         		//the coordinates for B.
    private int[][] matrix;                                                             		//the matrix.
    private Scanner scanner = new Scanner(System.in);                                   		//our scanner.
    private LinkedHashMap<Integer,String> directions = new LinkedHashMap<Integer,String>();		//a dictionary with all the possible moves in our game.
	
    //Constractor.
    public Matrix(int M, int N, int[] A, int[] B){
        this.lines = M;
        this.columns = N;
        this.matrix = new int[M][N];

        //setting all the positions to "unvisited"(0).
        for(int i=0; i<this.lines; i++){ for(int j=0; j<this.columns; j++){ matrix[i][j] = 0; } }
        
        //A and B will be at the same position but opposite on the matrix. (kind of like chess)
        this.matrix[A[0]-1][A[1]-1] = 10;           //placing A.
        this.Aline = A[0]-1;
        this.Acolumn = A[1]-1;
        this.matrix[B[0]-1][B[1]-1] = -10;          //placing B.
        this.Bline = B[0]-1;
        this.Bcolumn = B[1]-1;

		//placing the positions in hashmap "directions".
        directions.put(0, "Up Left 1 tile.");
        directions.put(1, "Up 1 tile.");
        directions.put(2, "Up Right 1 tile.");
        directions.put(3, "Left 1 tile.");
        directions.put(4, "Right 1 tile.");
        directions.put(5, "Down Left 1 tile.");
        directions.put(6, "Down 1 tile.");
        directions.put(7, "Down Right 1 tile.");
        directions.put(8, "Up Left 2 tiles.");
        directions.put(9, "Up 2 tiles.");
        directions.put(10, "Up Right 2 tiles.");
        directions.put(11, "Left 2 tiles.");
        directions.put(12, "Right 2 tiles.");
		directions.put(13, "Down Left 2 tiles.");
		directions.put(14, "Down 2 tiles.");
		directions.put(15, "Down Right 2 tiles."); 
    }

    //Constructor for copying a matrix.
    public Matrix(Matrix m){
    	lines = m.matrix.length;
    	columns = m.matrix[0].length;
    	matrix = new int[lines][columns];
    	
    	for (int i = 0; i < lines; i++){
    		for (int j = 0; j < columns; j++){ matrix[i][j] = m.matrix[i][j]; }
    	}
    	
    	Aline = m.Aline;
    	Acolumn = m.Acolumn;
    	Bline = m.Bline;
    	Bcolumn = m.Bcolumn;
    	
		//placing the positions in hashmap "directions".
        directions.put(0, "Up Left 1 tile.");
        directions.put(1, "Up 1 tile.");
        directions.put(2, "Up Right 1 tile.");
        directions.put(3, "Left 1 tile.");
        directions.put(4, "Right 1 tile.");
        directions.put(5, "Down Left 1 tile.");
        directions.put(6, "Down 1 tile.");
        directions.put(7, "Down Right 1 tile.");
        directions.put(8, "Up Left 2 tiles.");
        directions.put(9, "Up 2 tiles.");
        directions.put(10, "Up Right 2 tiles.");
        directions.put(11, "Left 2 tiles.");
        directions.put(12, "Right 2 tiles.");
		directions.put(13, "Down Left 2 tiles.");
		directions.put(14, "Down 2 tiles.");
		directions.put(15, "Down Right 2 tiles."); 	
    }
    
    //Method that makes "unvisited positions" (0) -> "visited"(1). (used for blocking tiles in the start of the game)
    public void marker(int line, int column){ 
        if(this.matrix[line-1][column-1] == 0){ this.matrix[line-1][column-1] = 1; }
    }

    //Method that prints the matrix for the players to see as the game continues.
    public void matrixPrinter(){
        System.out.println("\n------------ THE MATRIX ------------");
        for(int i=0; i<this.lines; i++){
            System.out.print("|");
            for(int j=0; j<this.columns; j++){
                System.out.print("| ");
                if(matrix[i][j] == 0){ System.out.print("  "); }
                if(matrix[i][j] == 1){ System.out.print("X "); }
                if(matrix[i][j] == 10){ System.out.print("A "); }
                if(matrix[i][j] == -10){ System.out.print("B "); }
            }
            System.out.print("||");
            System.out.println("");
        }
		System.out.println("");
    }

    //Method that sees the Winner. (will be used in minimax algorithm for predictions)
    //returns an int that indicates that someone won (we have a conclusion to the game)
	public int finish(boolean isA){ 
        int result = 0;
        boolean stuckA = true;                              //boolean that shows if A is stuck or not. (stuck == true)
		boolean stuckB = true;                              //boolean that shows if B is stuck or not. (stuck == true)
        int[] movesA = getMoves(true);                      //the moves that player A can do.
        int[] movesB = getMoves(false);                     //the moves that player B can do.
        
        for(int i=0; i<16; i++){                            //checking if the players have somewhere to go, if they do the boolean of stuck becomes false.
        	if (movesA[i] != 0) { stuckA = false; }
        	if (movesB[i] != 0) { stuckB = false; }
        }

        if(stuckA){ result = -1; }                           //case 1: Player B wins.
        if(stuckB){ result = 1; }                            //case 2: Player A wins.
        if(stuckA && stuckB){                                //case 3: if both players are stuck. -> the one who has his turn will lose first.
            if(isA){ result = -1; }
            else{ result = 1; }
        }
        return result;
    }

	//Method that gets the human-interaction (the choices of player B for his moves).
    public void humanC(){   
		
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();	//an array with all the possible choices for player B.
		int[] moves = this.getMoves(false);								//getting the possible moves for player B.
        
		//printing out the moves that the player can make.
        System.out.println("The moves you can select are: ");
        for(int i=0; i<16; i++){
        	if(moves[i] != 0){
        		System.out.println("	"+(i+1) + ": Move " + directions.get(i));
        		possibleMoves.add(i+1);									//adding the moves to the array of the possible choices.
        	}
        }
		//getting the human's choice. (player B)
        System.out.print("Please enter a choice number: ");
        int c = this.scanner.nextInt();
        if(possibleMoves.contains(c)){ this.move(c,false); }				   	//executing the move.
		else{														   			//the invalid input catcher.
			System.out.print("Your choice was invalid. \nPlease enter a valid choice number.");
			this.humanC();
		}
    }
	
    //Method that finds and returns the possible moves for both players (depending on the boolean isA) -> true == A and false == B
	public int[] getMoves(boolean isA){
        int row, column, enemy;
        int[] moves = new int[16];
        int pointer = 0;

        if (isA){ row = this.Aline; column = this.Acolumn; enemy = -10;}    //if we have player A.
        else { row = this.Bline; column = this.Bcolumn; enemy = 10;}        //if we have player B.

        //checking if we can move 1 tile in all directions.
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                try{
                    if(!(matrix[row+i][column+j] == 1 || matrix[row+i][column+j] == enemy)){ moves[pointer] = 1; }
                    else{ moves[pointer] = 0; }
                } 
                catch (Exception e){ moves[pointer] = 0; }
                if(!(i == j && i == 0)){ pointer++; }
            }
        }
        //checking if we can move 2 tiles in all directions.    
        for (int i = -2; i <= 2; i+=2){
            for (int j = -2; j <= 2; j+=2){
                if (moves[pointer-8] == 1){ //If we can make 1 move towards that direction
                    try{ if (!(matrix[row+i][column+j] == 1 || matrix[row+i][column+j] == enemy)){ moves[pointer] = 1; }
                    else { moves[pointer] = 0; } } 
                    catch (Exception e) { moves[pointer] = 0; }
                }
                if(!(i == j && i == 0)){ pointer++; }
            }
        }          
        return moves;    
    }
    
	//Method that executes the moves for A(true) and B(false).
    public void move(int dir, boolean isA){

		int player, row, column;
    	
		if(isA){ player = 10; row = this.Aline; column = this.Acolumn; }	//if we have player A.
    	else{ player = -10; row = this.Bline; column = this.Bcolumn; }		//if we have player B.
    	
		//Direction dir follows same pattern as Moves array, but +1.
    	this.matrix[row][column] = 1;
    	switch(dir){
    		case 1: //Up Left 1 tile.
    			row--;
    			column--;
    			break;
    		case 2: //Up 1 tile.
    			row--;
    			break;
    		case 3: //Up right 1 tile.
    			row--;
    			column++;
    			break;
    		case 4: //Left 1 tile.
    			column--;
    			break;
    		case 5: //Right 1 tile.
    			column++;
    			break;
    		case 6: //Down Left 1 tile.
    			row++; 
				column--;
    			break;
    		case 7: //Down 1 tile.
    			row++;
    			break;
    		case 8: //Down Right 1 tile.
    			row++; 
				column++;
    			break;
			case 9: //Up Left 2 tiles.
				this.matrix[row-1][column-1] = 1;
				row-=2;
				column-=2;
				break;
			case 10: //Up 2 tiles.
				this.matrix[row-1][column] = 1;
				row-=2;
				break;	
			case 11: //Up right 2 tiles.
				this.matrix[row-1][column+1] = 1;
				row-=2;
				column+=2;
				break;
			case 12: //Left 2 tiles.
				this.matrix[row][column-1] = 1;
				column-=2;
				break;
			case 13: //Right 2 tiles.
				this.matrix[row][column+1] = 1;
				column+=2;
				break;
			case 14: //Down Left 2 tiles.
				this.matrix[row+1][column-1] = 1;
				row+=2;
				column-=2;
				break;
			case 15: //Down 2 tiles.
				this.matrix[row+1][column] = 1;
				row+=2;
				break;
			case 16: //Down Right 2 tiles.
				this.matrix[row+1][column+1] = 1;
				row+=2;
				column+=2;
				break;
    		default:
    			break;
    	}
    	this.matrix[row][column] = player;
    	if(isA){ this.Aline = row; this.Acolumn = column; }		//if we have player A -> set the new position.
    	else{ this.Bline = row; this.Bcolumn = column; }		//else if we have player B -> do the same.
    }
}