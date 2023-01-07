package com.example.sudokusolver;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SudokuBoard extends View {
    private final int boardColor;
    private final int cellFillColor;
    private final int cellHighlightColor;
    private final int letterColor;
    private final int letterColorSolve;
    private final Paint boardPaintColor = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellHighlightColorPaint = new Paint();
    private final Paint letterPaint = new Paint();
    private final Rect letterPaintBounds= new Rect();     //to obtain the height of the letter
    private int cellSize;
    private final Solver solver= new Solver();


    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SudokuBoard, 0, 0);
        try {
            boardColor = a.getInteger(R.styleable.SudokuBoard_boardColor, 0);
            cellFillColor = a.getInteger(R.styleable.SudokuBoard_cellFillColor, 0);
            cellHighlightColor = a.getInteger(R.styleable.SudokuBoard_cellHighlightColor, 0);
            letterColor= a.getInteger(R.styleable.SudokuBoard_letterColor,0);
            letterColorSolve= a.getInteger(R.styleable.SudokuBoard_letterColorSolve, 0);

        } finally {
            a.recycle();           //creates space
        }
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());   //to get the minimum of the width and height of the phone screen and ensure that the board is still a perfect square
        setMeasuredDimension(dimension, dimension);      //returns the smallest of width and height of the device as the dimensions of our sudoku board}
        cellSize = dimension / 9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boardPaintColor.setStyle(Paint.Style.STROKE);  //PAINT BRUSH
        boardPaintColor.setStrokeWidth(16);
        boardPaintColor.setColor(boardColor);        //paint bucket
        boardPaintColor.setAntiAlias(true);          //for crisp lines


        cellFillColorPaint.setStyle(Paint.Style.FILL);  //PAINT brush
        cellFillColorPaint.setAntiAlias(true);
        cellFillColorPaint.setColor(cellFillColor);        //paint bucket

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);


        cellHighlightColorPaint.setStyle(Paint.Style.FILL);  //PAINT brush
        cellHighlightColorPaint.setAntiAlias(true);
        cellHighlightColorPaint.setColor(cellHighlightColor);        //paint bucket


        colorCell(canvas, solver.getSelectedRow(), solver.getSelectedCol());
        canvas.drawRect(0, 0, getWidth(), getHeight(), boardPaintColor);         //to draw the outer rectangle
        drawBoard(canvas);        //draws the grid or implements the drawBoard function we created below
        drawNumbers(canvas);      //draws the user's input number"

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){          //to trace user's touch on the screen
        boolean isValid;
       float x= event.getX();                             //to obtain x coordinate of user's touch
       float y= event.getY();                              //to obtain y coordinate of user's touch
       int action= event.getAction();                      //to obtain the type of tap occurred(touch, swipe, slide etc)

        if(action==MotionEvent.ACTION_DOWN){              //if it is a click event
            solver.setSelectedCol((int) Math.ceil(x/cellSize));  //returns the integer value of user's touch as the column number
            solver.setSelectedRow((int) Math.ceil(y/cellSize));   //returns the integer value of user's touch as the row number
            isValid=true;
        }else{
            isValid= false;
        }


        return isValid;
    }

    private void drawNumbers(Canvas canvas){     //drawing the input number
        letterPaint.setTextSize(cellSize);
        for(int r=0; r<9; r++){
            for(int c=0; c<9; c++){
                if(solver.getBoard()[r][c]!= 0){       //if the input number is not 0
                    String text= Integer.toString(solver.getBoard()[r][c]);  //storing the user's input number in a string "text"
                    float width, height;

                    letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                    width= letterPaint.measureText(text);        //to obtain the width of the number
                    height= letterPaintBounds.height();         //to obtain the height of the number

                    canvas.drawText(text, c*cellSize + ((cellSize-width)/2), (r*cellSize + cellSize) - ((cellSize-height)/2), letterPaint);

                }
            }
        }

        letterPaint.setColor(letterColorSolve);

        for(ArrayList<Object> letter : solver.getEmptyBoxIndex()) {
          int r= (int) letter.get(0);
          int c= (int) letter.get(1);

            String text= Integer.toString(solver.getBoard()[r][c]);  //storing the user's input number in a string "text"
            float width, height;

            letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
            width= letterPaint.measureText(text);        //to obtain the width of the number
            height= letterPaintBounds.height();         //to obtain the height of the number

            canvas.drawText(text, c*cellSize + ((cellSize-width)/2), (r*cellSize + cellSize) - ((cellSize-height)/2), letterPaint);

        }
        }


    private void colorCell(Canvas canvas, int r, int c){
        if(solver.getSelectedCol()!= -1 && solver.getSelectedRow()!= -1){
            canvas.drawRect((c-1)*cellSize, 0, c*cellSize, cellSize*9, cellHighlightColorPaint);        //to highlight the col of the clicked cell
            canvas.drawRect(0, (r-1)*cellSize, 9*cellSize, cellSize*r, cellHighlightColorPaint);       //to highlight the row of the clicked cell
            canvas.drawRect((c-1)*cellSize,(r-1)*cellSize , c*cellSize, cellSize*r, cellFillColorPaint); //to highlight the clicked cell
        }
        invalidate();
    }

    private void drawThickLine() {
        boardPaintColor.setStyle(Paint.Style.STROKE);  //PAINT BRUSH
        boardPaintColor.setStrokeWidth(10);
        boardPaintColor.setColor(boardColor);        //paint bucket
        boardPaintColor.setAntiAlias(true);          //for crisp lines
    }

    private void drawThinLine() {
        boardPaintColor.setStyle(Paint.Style.STROKE);  //PAINT BRUSH
        boardPaintColor.setStrokeWidth(4);
        boardPaintColor.setColor(boardColor);        //paint bucket
        boardPaintColor.setAntiAlias(true);          //for crisp lines
    }

    private void drawBoard(Canvas canvas) {
        for (int c = 0; c < 10; c++) {           //cols
            if (c % 3 == 0) {
                drawThickLine();
            } else {
                drawThinLine();
            }
            canvas.drawLine(cellSize * c, 0, cellSize * c, getWidth(), boardPaintColor);
        }

        for (int r = 0; r < 10; r++) {           //rows
            if (r % 3 == 0) {
                drawThickLine();
            } else {
                drawThinLine();
            }

            canvas.drawLine(0, cellSize * r, getWidth(), cellSize*r, boardPaintColor);
        }
    }

    public Solver getSolver(){
        return this.solver;
    }
}
