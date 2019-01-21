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

public class Bouncer extends ImageView{

    public static final String BOUNCER_IMAGE = "basketball.gif";
    public static final int BOUNCER_WIDTH = 15;
    public static final int BOUNCER_HEIGHT = 15;

    private int xDirection;
    private int yDirection;
    private int bouncerSpeed = 0;
    private int livesWidth = 30;
    private int livesHeight = 30;

    public Bouncer(int screenWidth, int screenHeight, double paddleHeight, int level, double levelFactor) {
        super();
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        this.setImage(image);
        this.setFitWidth(BOUNCER_WIDTH);
        this.setFitHeight(BOUNCER_HEIGHT);
        if (level > 3){
            this.setFitHeight(this.getBoundsInLocal().getHeight() / levelFactor);
            this.setFitWidth(this.getBoundsInLocal().getWidth() / levelFactor);
        }
        this.startingPosition(screenWidth, screenHeight, paddleHeight);
    }

    /**
     * Creates bouncers representing lives.
     */

    public Bouncer(int ballNumber, int screenWidth, int displayHeight){
        super();
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        this.setImage(image);
        placeLife(screenWidth, displayHeight, ballNumber);
        this.setFitWidth(livesWidth);
        this.setFitHeight(livesHeight);
    }

    /**
     * Removes bouncer from the lives indicator.
     */

    public void removeLife(){
        this.setX(1000);
        this.setY(1000);
    }

    /**
     * Adds bouncer to the lives indicator.
     */

    public void placeLife(int screenWidth, int displayHeight, int lifeNumber){
        this.setX(screenWidth - livesWidth * 1.1 * lifeNumber);
        this.setY((displayHeight / 2) - (livesHeight / 2));
    }

    /**
     * Resets to the original position of the bouncer.
     */

    public void startingPosition(int screenWidth, int screenHeight, double paddleHeight){
        this.setX((screenWidth / 2) - this.getBoundsInLocal().getWidth() / 2);
        this.setY(screenHeight - this.getBoundsInLocal().getHeight() - paddleHeight - 0.02);
        xDirection = 0;
        yDirection = -1;
    }

    /**
     * Creates movement of the bouncer.
     */

    public void updateBouncerMovement(double elapsedTime){
        this.setX(this.getX() + xDirection * bouncerSpeed * elapsedTime);
        this.setY(this.getY() + yDirection * bouncerSpeed * elapsedTime);
    }

    /**
     * Changes y direction of bouncer.
     */

    public void deflectY(){
        yDirection *= -1;
    }

    /**
     * Changes x direction of bouncer.
     */

    public void deflectX(){
        xDirection *= -1;
    }

    /**
     * Deflects bouncer to the left.
     */

    public void deflectLeft() {
        xDirection = -1;
    }

    /**
     * Deflects bouncer to the right.
     */

    public void deflectRight() {
        xDirection = 1;
    }

    /**
     * Sets the speed of the bouncer movement.
     */

    public void setBouncerSpeed(int newSpeed){
        bouncerSpeed = newSpeed;
    }

    /**
     * Increases the speed of the bouncer movement.
     */

    public void increaseBouncerSpeed(double factor){
        this.bouncerSpeed = (int)(this.bouncerSpeed * factor);
    }

    /**
     * Decreases the speed of the bouncer movement.
     */

    public void decreaseBouncerSpeed(double factor){
        this.bouncerSpeed = (int)(this.bouncerSpeed / factor);
    }
}
