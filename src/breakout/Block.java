package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Project 1: Breakout Game
 * Duke CompSci 308 Spring 2019 - Duvall
 * Date Created: 1/14/2019
 * Date Last Modified: 1/21/2019
 * @author Brian J. Jordan (bjj17)
 */

public class Block extends ImageView{

    public static final String BLUE_BLOCK_IMAGE = "brick7.gif";
    public static final String WHITE_BLOCK_IMAGE = "brick3.gif";

    private int xVal;
    private int yVal;
    private int health;
    private PowerBlock myPowerBlock;
    private boolean hasPowerBlock = false;



    public Block(int xValue, int yValue, int spacingFromTop){
        super();
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(WHITE_BLOCK_IMAGE));
        this.setImage(image);
        xVal = xValue;
        yVal = yValue;
        this.setFitHeight(this.getBoundsInLocal().getHeight() * 1.5);
        this.setX(xValue * this.getBoundsInLocal().getWidth() * 1.02);
        this.setY((yValue * this.getBoundsInLocal().getHeight() * 1.02) + spacingFromTop);
        this.health = 1;
    }

    /**
     * Decrease health, change block color, and call to remove block
     */

    public int blockHit(){
        this.health--;
        if (this.health == 0){
            removeBlock();
        }
        else changeColor();
        return this.health;
    }

    private void changeColor(){
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(WHITE_BLOCK_IMAGE));
        this.setImage(image);
    }

    /**
     * Return current health value.
     */

    public int getHealth(){
        return this.health;
    }

    private void removeBlock(){
        this.setX(1000);
        this.setY(1000);
    }

    /**
     * Removes blocks taken out by power up.
     */

    public void destroyBlock(){
        this.health = 0;
        removeBlock();
    }

    /**
     * Gives block power up;
     */

    public void setPowerBlock(PowerBlock pb){
        hasPowerBlock = true;
        myPowerBlock = pb;
    }

    /**
     * Returns if block has power up.
     */

    public boolean isPowerBlock(){
        return this.hasPowerBlock;
    }

    /**
     * Returns power block.
     */

    public PowerBlock getMyPowerBlock(){
        return myPowerBlock;
    }

    /**
     * Sets the block color to blue.
     */

    public void setToBlue(){
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BLUE_BLOCK_IMAGE));
        this.setImage(image);
        this.health = 2;
    }

    /**
     * Returns blocks x value.
     */

    public int getXVal(){
        return xVal;
    }

    /**
     * Returns blocks y value.
     */

    public int getYVal(){
        return yVal;
    }
}
