import acm.graphics.GCanvas;
import acm.graphics.GOval;

import java.awt.*;

public class ColumnBreaker extends GOval {

    private double deltaY = 1;
    private GCanvas screen;


    public ColumnBreaker(double size, GCanvas screen){
        super(size, size);
        setVisible(false);
        setFilled(true);
        this.screen = screen;
        setFillColor(Color.gray);
    }

    public void handleMove(){
        move(0, deltaY);
    }
}