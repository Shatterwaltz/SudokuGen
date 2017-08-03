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
    private int[][] grid = new int[9][9];
    private String[][] pencilGrid = new String[9][9];
    Random rand = new Random();
    
    public Grid(){
        generateGrid(0,0);
        printGrid();
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                pencilGrid[i][j]="";
            }
        }
        removeNumbers();
        System.out.println(pencilGrid[0][0].length());
        printPencil();
        printGrid();
    }
    
    public int[][] getGrid(){
        return grid;
    }
    
    public void printGrid(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(grid[i][j]);
            }
            System.out.println("");
        }
        System.out.println("\n");
    }
    
    public void printPencil(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print("|"+pencilGrid[i][j]+"|");
            }
            System.out.println("");
        }
        System.out.println("\n");
    }
    
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
    
    
    private void removeNumbers(){
        boolean solvable=true;
        while(solvable){
            printGrid();
            solvable=false;
            int x=rand.nextInt(9);
            int y=rand.nextInt(9);
            int tmp=grid[x][y];
            grid[x][y]=0;
            
            updatePencil();
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    if(pencilGrid[i][j].length()==1){
                        solvable=true;
                    }
                }
            }
            if(!solvable){
                grid[x][y]=tmp;
            }
            
        }
    }
    
    private void updatePencil(){
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
