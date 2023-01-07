package com.example.sudokusolver;



import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class Solver {
    int board[][];


    ArrayList<ArrayList<Object>> emptyBoxIndex;

     int selected_row;
     int selected_col;

    Solver(){
        selected_row=-1;
        selected_col=-1;

        board= new int[9][9];

        for(int r=0; r<9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = 0;
            }
        }
            emptyBoxIndex= new ArrayList<>();      //stores the array list in this variable
            }

            public void getEmptyBoxIndexes() {           //obtaining the indexes of all the zeroes in the board
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        if (this.board[r][c] == 0) {
                            this.emptyBoxIndex.add(new ArrayList<>());   //creates a new array list when it finds a zero
                            this.emptyBoxIndex.get(this.emptyBoxIndex.size() - 1).add(r); //finds the index of the last zero
                            this.emptyBoxIndex.get(this.emptyBoxIndex.size() - 1).add(c);
                        }
                    }
                }
            }

            public boolean check(int row, int col) {
                for (int i = 0; i < 9; i++) {
                    if (this.board[i][col] == this.board[row][col] && row != i) {   //checking for same column
                       return false;
                    }

                        if (this.board[row][i] == this.board[row][col] && col != i) {   //checking for same row
                            return false;
                        }
                    int boxRow= row/3;
                        int boxCol= col/3;

                        for(int r= boxRow*3;  r<boxRow*3 + 3; r++) {            //checking for same cell in the box
                            for(int c= boxCol*3;  c<boxCol*3 + 3; c++) {
                                if(this.board[r][c]==this.board[row][col] && r!=row && c!= col) {
                                    return false;
                                }
                            }
                        }

                }
                return true;
            }

            public boolean solve(SudokuBoard display) {
                int row = -1;
                int col = -1;

                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        if ((this.board[r][c] == 0)) { //finding an empty cell
                            row = r;
                            col = c;
                            break;
                        }
                    }
                }
                        if (row == -1 || col == -1) {
                            return true;
                        }


                        for (int i = 1; i < 10; i++) {
                            this.board[row][col] = i;         //placing the number in the empty cell
                            display.invalidate();

                            if (check(row, col)) {          //check function is satisfied after placing the number
                                if (solve(display)) {       //you can place the next number
                                    return true;
                                }
                    }
                    this.board[row][col]=0;    //backtracking
                }
                 return false;
            }

            public void resetBoard() {
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        board[r][c]=0;
                    }
                }
             this.emptyBoxIndex = new ArrayList<>();    //clears the board
            }

    public int[][] getBoard() {
        return this.board;
    }



    public ArrayList<ArrayList<Object>> getEmptyBoxIndex () {
        return this.emptyBoxIndex;
    }



    public void setNumberPos(int num) {

        if(this.selected_row!=-1 && this.selected_col!=-1) {
            if (this.board[this.getSelectedRow()-1][this.getSelectedCol()-1 ] == num) {   //same num clicked twice in a cell
                this.board[this.getSelectedRow()-1][this.getSelectedCol()-1] = 0;
            }else{
                this.board[this.getSelectedRow()-1][this.getSelectedCol()-1] = num;
            }
        }
    }




    public int getSelectedRow() {
        return selected_row;
    }

    public int getSelectedCol() {
        return selected_col;
    }

    public void setSelectedRow(int r){
        selected_row=r;
    }

    public void setSelectedCol(int c){
        selected_col=c;
    }
}
