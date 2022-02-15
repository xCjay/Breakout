import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

import java.awt.*;
import java.awt.event.MouseEvent;

import svu.csc213.Dialog;

public class Breakout extends GraphicsProgram {

    private Ball ball;
    private Paddle paddle;
    private ColumnBreaker colBreak;

    private GLabel livesLabel;
    private GLabel pointsLabel;

    private Color rowColor[] = {Color.RED, Color.RED, Color.orange, Color.orange, Color.yellow, Color.yellow, Color.green, Color.green, Color.cyan, Color.cyan};

    private int rowHealth[] = {5,5,4,4,3,3,2,2,1,1};
    private int brickRow[] = {1,2,3,4,5,6,7,8,9,10};

    private int numBricksInRow;

    private int lives = 3;
    private int points = 0;


    //makes the bricks under the brick you just broke break
    private int test;
    private boolean unstable = false;

    @Override
    public void init(){

        numBricksInRow = (int) (getWidth() / (Brick.WIDTH + 5.0));

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < numBricksInRow; col++) {

                double brickX = 10 + col * (Brick.WIDTH + 5);
                double brickY = Brick.HEIGHT + row * (Brick.HEIGHT + 5);

                Brick brick = new Brick(brickX, brickY, rowColor[row], rowHealth[row], col, brickRow[row], rowHealth[row]);
                add(brick);
            }
        }

        ball = new Ball(getWidth()/2, 350, 10, this.getGCanvas());
        add(ball);

        paddle = new Paddle(230, 430, 50 ,10);
        add(paddle);

        colBreak = new ColumnBreaker(10, this.getGCanvas());


        livesLabel = new GLabel("Lives :" + lives);
        add(livesLabel, getWidth()/3, 10);

        pointsLabel = new GLabel("Points :" + points);
        add(pointsLabel, (getWidth()/3)*2, 10);


    }

    @Override
    public void run(){
        addMouseListeners();
        waitForClick();
        gameLoop();
    }

    @Override
    public void mouseMoved(MouseEvent me){
        // make sure that the paddle doesn't go offscreen
        if((me.getX() < getWidth() - paddle.getWidth()/2)&&(me.getX() > paddle.getWidth() / 2)){
            paddle.setLocation(me.getX() - paddle.getWidth()/2, paddle.getY());
        }
    }

    private void columnBreaker(int col, int row){
            add(colBreak, 10+(col*Brick.WIDTH) + (5*col), row * Brick.HEIGHT + 5*row);
            while (true) {
                colBreak.handleMove();
                handleColBreak();
                pause(0);
                if (colBreak.getY() >= 430){
                    remove(colBreak);
                    break;
                }
            }
        gameLoop();
    }

    private void completeCheck(){
        if (getElementCount() == 2){
            Dialog.showMessage("you win");
        }
    }

    private void handleColBreak(){
        GObject obj = null;

        // check to see if the ball is about to hit something

        if(obj == null){
            // check the top right corner
            obj = this.getElementAt(colBreak.getX()+colBreak.getWidth(), colBreak.getY());
        }

        if(obj == null){
            // check the top left corner
            obj = this.getElementAt(colBreak.getX(), colBreak.getY());
        }
        //check the bottom right corner for collision
        if (obj == null) {
            obj = this.getElementAt(colBreak.getX() + colBreak.getWidth(), colBreak.getY() + colBreak.getHeight());
        }
        //check the bottom left corner for collision
        if (obj == null) {
            obj = this.getElementAt(colBreak.getX(), colBreak.getY() + colBreak.getHeight());
        }

        // see if we hit something
        if(obj != null){
            if(obj instanceof Brick){
                points += ((Brick) obj).getValue();
                pointsLabel.setLabel("Points :" + points);
                remove(obj);
                if (points == 450){
                    Dialog.showMessage("you win");
                }

            }

        }
    }

    private void gameLoop(){
        while(true){
            // move the ball
            ball.handleMove();

            // handle collisions
            handleCollisions();

            // handle losing the ball
            if(ball.lost){
                handleLoss();
            }
        }
    }

    private void handleCollisions(){
        // obj can store what we hit
        GObject obj = null;

        // check to see if the ball is about to hit something

        if(obj == null){
            // check the top right corner
            obj = this.getElementAt(ball.getX()+ball.getWidth(), ball.getY());
        }

        if(obj == null){
            // check the top left corner
            obj = this.getElementAt(ball.getX(), ball.getY());
        }

        //check the bottom right corner for collision
        if (obj == null) {
            obj = this.getElementAt(ball.getX() + ball.getWidth(), ball.getY() + ball.getHeight());
        }
        //check the bottom left corner for collision
        if (obj == null) {
            obj = this.getElementAt(ball.getX(), ball.getY() + ball.getHeight());
        }

        // see if we hit something
        if(obj != null){

            // lets see what we hit!
            if(obj instanceof Paddle){

                if(ball.getX() < (paddle.getX() + (paddle.getWidth() * .2))){
                    // did I hit the left side of the paddle?
                    ball.bounceLeft();
                } else if(ball.getX() > (paddle.getX() + (paddle.getWidth() * .8))) {
                    // did I hit the right side of the paddle?
                    ball.bounceRight();
                } else {
                    // did I hit the middle of the paddle?
                    ball.bounce();
                }

            }


            if(obj instanceof Brick){
                // bounce the ball
                ball.bounce();
                // destroy the brick
                ((Brick) obj).brickHealth -=1;
                switch (((Brick) obj).brickHealth){
                    case 1: ((Brick) obj).setFillColor(Color.cyan);
                        break;
                    case 2: ((Brick) obj).setFillColor(Color.green);
                        break;
                    case 3: ((Brick) obj).setFillColor(Color.yellow);
                        break;
                    case 4: ((Brick) obj).setFillColor(Color.orange);
                        break;
                    case 5: ((Brick) obj).setFillColor(Color.red);
                        break;
                }
                if (((Brick) obj).brickHealth == 0){
                    if (unstable){
                        columnBreaker(((Brick) obj).getColumn(), ((Brick) obj).getRow());
                    } else {
                        remove(obj);
                        points += ((Brick) obj).getValue();
                        pointsLabel.setLabel("Points :" + points);
                        if (points == 450){
                            Dialog.showMessage("you win");
                        }
                    }
                }
            }

        }

        // if by the end of the method obj is still null, we hit nothing
    }

    private void handleLoss(){
        ball.lost = false;
        reset();
    }

    private void reset(){
        ball.setLocation(getWidth()/2, 350);
        paddle.setLocation(230, 430);
        waitForClick();
        lives -= 1;
        livesLabel.setLabel("Lives :" + lives);
        if (lives == 0){
            Dialog.showMessage("You lose");
            if (Dialog.getYesOrNo("Try Again?")) {
                lives = 3;
                points = 0;
                pointsLabel.setLabel("Points :" + points);
                livesLabel.setLabel("Lives :" + lives);
                run();
            }
        }
    }

    public static void main(String[] args) {
        new Breakout().start();
    }

}