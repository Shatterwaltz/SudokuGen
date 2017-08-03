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
    //Stores data for "pencilled in" numbers
    private String[][] pencilGrid = new String[9][9];
    Random rand = new Random();
    
    public Grid(){
        //Build a solved sudoku grid
        generateGrid(0,0);
        printGrid();
        //Generate pencilled in grid
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                pencilGrid[i][j]="";
            }
        }
        //Remove numbers from the solved puzzle while retaining
        //solvability. 
        removeNumbers();
    }
    
    public int[][] getGrid(){
        return grid;
    }
    
    public void printGrid(){
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
    
  /*  public void printPencil(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print("|"+pencilGrid[i][j]+"|");
            }
            System.out.println("");
        }
        System.out.println("\n");
    }*/
    
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
                if(hasSolution(i,j,tmp)){
                    grid[i][j]=tmp;
                }else{
                    printGrid();
                }
                i+=direction;
            }
            direction*=-1;
            i+=direction;
        }
        printGrid();
    }
    
    //Test board to see if has a one solution, with the exception of 
    //one that uses the value passed in through "exclude"
    //Pass in a zero to exclude nothing.
    //This is used when testing solvability after removing numbers to 
    //avoid finding the original solution. 
    //If a solution is found, it immediately returns, because that solution
    //is not unique. 
    private boolean hasSolution(int x, int y, int exclude){
        if(grid[x][y]!=0){
            if(x==8){
                if(y==8){
                    return true;
                }else{
                    return hasSolution(0, y+1, 0);
                }
            }else{
                return hasSolution(x+1, y, 0);
            }
        }else{
            int tmp = grid[x][y];
            for(int i=1;i<9;i++){
                if(i!=exclude){
                    grid[x][y]=i;
                    if(checkRow(y)&&checkColumn(x)&&checkBlock(x, y)){
                        if(x==8){
                            if(y==8){
                                grid[x][y]=tmp;
                                return true;
                            }else{
                                if(hasSolution(0, y+1, 0)){
                                    grid[x][y]=tmp;
                                    return true;
                                }
                            }
                        }else{
                            if(hasSolution(x+1, y, 0)){
                                grid[x][y]=tmp;
                                return true;
                            }
                        }
                    }
                    
                }
            }
            grid[x][y]=tmp;
            return false;
        }
    }
    
    //Updates the pencilled in grid. 
    //Pencilled in numbers are a list of every possible number
    //that could fit in a given space.
  /*  private void updatePencil(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                pencilGrid[i][j]="";
                if(grid[i][j]==0){
                    for(int k=1;k<=9;k++){
                        grid[i][j]=k;
                        if(checkRow(j)&&checkColumn(i)&&checkBlock(i,j)){
                            pencilGrid[i][j]+=k;
                        }
                    }
                    grid[i][j]=0;
                }
            }
        }
    } */
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
