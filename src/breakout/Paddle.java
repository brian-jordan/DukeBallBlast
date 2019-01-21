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

public class Paddle extends ImageView{

    public static final String PADDLE_IMAGE = "hoop.gif";
    public static final int PADDLE_SPEED = 20;
    public static final int PADDLE_WIDTH = 60;
    public static final int PADDLE_HEIGHT = 15;

    public Paddle(int screenWidth, int screenHeight, int level, double levelFactor){
        super();
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE)); // From: http://clipart-library.com/basketball-goal-cliparts.html
        this.setImage(image);
        this.setFitWidth(PADDLE_WIDTH);
        this.setFitHeight(PADDLE_HEIGHT);
        if (level > 2){
            this.setFitWidth(this.getBoundsInLocal().getWidth() / levelFactor);
        }
        this.startingPosition(screenWidth, screenHeight);
    }

    /**
     * Sets paddle to starting position.
     */

    public void startingPosition(int screenWidth, int screenHeight){
        this.setX((screenWidth / 2) - (this.getBoundsInLocal().getWidth() / 2));
        this.setY(screenHeight - (this.getBoundsInLocal().getHeight()));
    }

    /**
     * Increases size of the paddle
     */

    public void increasePaddleSize(double factor){
        this.setFitWidth(this.getBoundsInLocal().getWidth() * factor);
    }

    /**
     * Decreases size of the paddle.
     */

    public void decreasePaddleSize(double factor){
        this.setFitWidth(this.getBoundsInLocal().getWidth() / factor);
    }
}
