/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
import java.util.LinkedList;
import java.util.Random;
/**
 *
 * @author shatterwaltz
 */
public class Grid {
    //Stores Sudoku board
    private int[][] grid = new int[9][9];
    private int[][] solution=new int[9][9];
    Random rand = new Random();
    
    public Grid(){
        do{
            //Build a solved sudoku grid
            generateGrid(0,0);
            //Save the completed grid as solution
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    solution[i][j]=grid[i][j];
                }
            }
            //Removes numbers while maintaining solvability. 
            removeNumbers();
        //Keep looping if, for some reason, puzzle was unsolvable. 
        }while(!hasSolution(0,0,new Triple(0,0,0)));
        
    }
    
    public int[][] getGrid(){
        return grid;
    }
    
    public int[][] getSolution(){
        return solution;
    }
    
    public static void printGrid(int[][] grid){
        for(int i=0;i<9;i++){
            System.out.println(" _ _ _ _ _ _ _ _ _ ");
            for(int j=0;j<9;j++){
                System.out.print("|");
                if(grid[i][j]==0){
                    System.out.print(" ");
                }else{
                    System.out.print(grid[i][j]);
                }
                
            }
            System.out.println("");
        }
        System.out.println("\n");
    }
    
    
    //Recursively generate random numbers for each space in grid. 
    //If a generated number breaks one of the rules of sudoku,
    //generate a different number in its place. If no numbers
    //are valid, then steps back and regenerates other parts
    //of the board until complete. 
   private boolean generateGrid(int x, int y){
       LinkedList<Integer> choices = new LinkedList<>();
       for(int i=1;i<=9;i++){
           choices.add(i);
       }
       
       boolean valid=false;
       while(!valid&&choices.size()>0){
           int choice = rand.nextInt(choices.size());
           int val=choices.get(choice);
           choices.remove(choice);
           
           grid[x][y]=val;
           if(checkColumn(x)&&checkRow(y)&&checkBlock(x, y)){
               if(x==8){
                   if(y==8){
                       valid=true;
                   }else{
                       valid= generateGrid(0, y+1);
                   }
               }else{
                   valid= generateGrid(x+1, y);
               }
           }
       }
       if(!valid){
           grid[x][y]=0;
       }
       return valid;
   }
    
    //Remove numbers from grid in an S pattern, while still retaining
    //solvability
    private void removeNumbers(){
        int i=0;
        int direction=1;
        for(int j=0;j<9;j++){
            while(i<9&&i>=0){
                int tmp=grid[i][j];
                grid[i][j]=0;
                if(hasSolution(0,0, new Triple(i,j,tmp))){
                    grid[i][j]=tmp;
                }
                i+=direction;
            }
            direction*=-1;
            i+=direction;
        }
    }
    
    //Test board to see if has unique solution.
    //LastRemoved stores the x/y coordinates and value of the 
    //tile we most recently removed. 
    //This is used so that we do not find that solution, and instead
    //return if we find any other solution. 
    private boolean hasSolution(int x, int y, Triple lastRemoved){
        //Just move on if this tile isn't empty
        if(grid[x][y]!=0){
            if(x==8){
                if(y==8){
                    //If it's the final tile, we must have a solution
                    return true;
                }else{
                    //end of row, move on.
                    return hasSolution(0, y+1, lastRemoved);
                }
            }else{
                //next tile in row.
                return hasSolution(x+1, y, lastRemoved);
            }
        }else{
            //If this tile isn't a given, start plugging values in. 
            for(int i=1;i<=9;i++){
                    //First, make sure this isn't the solution we already know exists. 
                    if(x!=lastRemoved.getL()||y!=lastRemoved.getM()||i!=lastRemoved.getR()){
                        //Plug in current number
                        grid[x][y]=i;
                        //See if it passes checks
                        if(checkRow(y)&&checkColumn(x)&&checkBlock(x, y)){
                            //If it does, try rest of puzzle with this number
                            if(x==8){
                                if(y==8){
                                    //If we're at the end of the puzzle, since it
                                    //passed the checks, this must be a 
                                    //solution.
                                    grid[x][y]=0;
                                    return true;
                                }else{
                                    //Otherwise, check rest of puzzle. 
                                    if(hasSolution(0, y+1, lastRemoved)){
                                        grid[x][y]=0;
                                        return true;
                                    }
                                }
                            }else{
                                if(hasSolution(x+1, y, lastRemoved)){
                                    grid[x][y]=0;
                                    return true;
                                }
                            }
                        }
                }
            }
            grid[x][y]=0;
            //could not find a solution
            return false;
        }
    }
    
    //Check column 0-8 for inconsistencies.
    //False means invalid
    private boolean checkColumn(int column){
        boolean[] found = new boolean[9];
        for(int i=0;i<9;i++){
            if(grid[column][i]!=0){
                if(found[grid[column][i]-1]){
                    return false;
                }
                found[grid[column][i]-1]=true;
            }
        }
        return true;
    }
    
    //Check row 0-8 for inconsistencies
    //False means invalid
    private boolean checkRow(int row){
        boolean[] found = new boolean[9];
        for(int i=0;i<9;i++){
            if(grid[i][row]!=0){
                if(found[grid[i][row]-1]){
                    return false;
                }
                found[grid[i][row]-1]=true;
            }
        }
        return true;
    }
    
    //Check block for inconsistencies. 
    //Specify block by top left tile coordinates.
    //False means invalid.
    private boolean checkBlock(int blockX, int blockY){
        boolean[] found = new boolean[9];
        if(blockX>=6){
            blockX=6;
        }else if(blockX>=3){
            blockX=3;
        }else{
            blockX=0;
        }
        
        if(blockY>=6){
            blockY=6;
        }else if(blockY>=3){
            blockY=3;
        }else{
            blockY=0;
        }
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(grid[i+blockX][j+blockY]!=0){
                    if(found[grid[i+blockX][j+blockY]-1]){
                        return false;
                    }
                    found[grid[i+blockX][j+blockY]-1] = true;
                }
            }
        }
        
        return true;
    }
    
}

class Triple{
    int l;
    int m;
    int r;
    public Triple(int l, int m, int r){
        this.l=l;
        this.m=m;
        this.r=r;
    }
    
    public int getL(){return l;}
    public int getM(){return m;}
    public int getR(){return r;}
}
