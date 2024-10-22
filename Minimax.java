//Imports.
import java.util.*;

//Minimax Algorithm Class.
class Minimax{
    
    //Private fields.
    private Matrix m, undo;
    private int chosen;

    //Constructor.
    public Minimax(Matrix m){ this.m = m; }

    //Method that finds the best move for player A. (starts the minimax algorithm)
    public Matrix Amove(Matrix m){  
        this.chosen = 0;
        int bestScore = Integer.MIN_VALUE;
        int[] list = m.getMoves(true);

        for(int i=0; i<list.length; i++){
            if(list[i]!=0){
                undo = new Matrix(m);
                m.move(i+1,true);
                int score = minimax(m,false);
                m = undo;
                if(score >= bestScore){
                    bestScore = score;
                    this.chosen = i+1;
                }
            }
        }
        m.move(chosen,true);
        return m;
    }   

    //Recursive Minimax Algorithm. -> 
    public int minimax(Matrix m, boolean isA){
        
        int bestScore,score;
        int[] list;
        Matrix undo; 

        int y = m.finish(isA);
        if(y != 0){ return y; }       //checking if we have a terminal condition (breaks the recursiveness!)

        //if we have player A's turn.
        if(isA){
            bestScore = Integer.MIN_VALUE;
            list = m.getMoves(true);
            for(int i=0; i<list.length; i++){
                if(list[i]!=0){
                    undo = new Matrix(m);
                    m.move(i+1,true);
                    score = minimax(m,false);
                    m = undo;
                    bestScore = Math.max(score,bestScore);
                }
            }
        }else{  //if we have player B's turn.
            bestScore = Integer.MAX_VALUE;
            list = m.getMoves(false);          
            for(int i = 0; i < list.length; i++){
                if(list[i] != 0){      
                    undo = new Matrix(m);
                    m.move(i+1,false);
                    score = minimax(m, true);
                    m = undo;
                    bestScore = Math.min(score,bestScore);
                }
            }
        }
        return bestScore;
    }
}