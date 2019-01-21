package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Project 1: Breakout Game
 * Duke CompSci 308 Spring 2019 - Duvall
 * Date Created: 1/14/2019
 * Date Last Modified: 1/20/2019
 * @author Brian J. Jordan (bjj17)
 */

public class PowerBlock extends ImageView{

    public static final String BALL_SPEED_IMAGE = "extraballpower.gif";
    public static final String PADDLE_SIZE_IMAGE = "sizepower.gif";
    public static final char[] POWERS = {'p', 'b', 'd'};
    public static final int yDirection = 1;

    private boolean powerUp;
    private char power;
    private int powerBlockSpeed;


    public PowerBlock(boolean isPowerUp, double xValue, double yValue){
        super();
        if (isPowerUp){
            this.powerUp = true;
        }
        else {
            powerUp = false;
        }
        determinePowerType(isPowerUp);
        if (this.power == 'p'){
            var image = new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_SIZE_IMAGE));
            this.setImage(image);
        }
        else if (this.power == 'b'){
            var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_SPEED_IMAGE));
            this.setImage(image);
        }
        this.setFitWidth(this.getBoundsInLocal().getWidth() * 2);
        this.setFitHeight(this.getBoundsInLocal().getHeight() * 2);
        this.setX(xValue - (this.getBoundsInLocal().getWidth() / 2));
        this.setY(yValue - (this.getBoundsInLocal().getHeight() / 2));
        powerBlockSpeed = 0;
    }

    /**
     * Moves power up location.
     */

    public void updatePowerBlockMovement(double elapsedTime){
        this.setY(this.getY() + yDirection * powerBlockSpeed * elapsedTime);
    }

    /**
     * Sets speed of movement.
     */

    public void setPowerBlockSpeed(int pbSpeed){
        powerBlockSpeed = pbSpeed;
    }

    /**
     * Removes power up from screen.
     */

    public void removePowerBlock(){
        this.setX(1000);
        this.setY(1000);
    }

    /**
     * Randomly chooses power type.
     */

    public void determinePowerType(boolean isPowerUp){
        if (isPowerUp){
            this.power = POWERS[(int)(Math.random()*3)];
        }
        else this.power = POWERS[(int)(Math.random()*2)];
    }

    /**
     * Returns power type
     */

    public char getPower(){
        return  this.power;
    }

    /**
     * Returns if is a power up.
     */

    public boolean getIsPowerUp(){
        return this.powerUp;
    }
}
