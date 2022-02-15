import acm.graphics.GRect;
import java.awt.Color;

public class Brick extends GRect {

    public static final int WIDTH = 44;
    public static final int HEIGHT = 20;

    public int brickHealth;
    public int brickValue;
    public int brickColumn;
    public int brickRow;

    public Brick(double x, double y, Color color, int brickHealth, int brickColumn, int brickRow, int brickValue){
        super(x,y,WIDTH, HEIGHT);
        this.setFillColor(color);
        this.setFilled(true);
        this.brickHealth = brickHealth;
        this.brickValue = brickHealth;
        this.brickRow = brickRow;
        this.brickColumn = brickColumn;
    }

    public int getHealth(){
        return brickHealth;
    }

    public int getValue(){
        return brickValue;
    }

    public int getColumn(){return brickColumn;}

    public int getRow(){return brickRow;}

    public void setHealth(int health){
        this.brickHealth = health;
    }
}