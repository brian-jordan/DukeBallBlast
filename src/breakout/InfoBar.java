package breakout;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Project 1: Breakout Game
 * Duke CompSci 308 Spring 2019 - Duvall
 * Date Created: 1/14/2019
 * Date Last Modified: 1/20/2019
 * @author Brian J. Jordan (bjj17)
 */

public class InfoBar extends Rectangle {

    public static final String TITLE = "Duke BallBlast";
    public static final String SCORE = "Score: ";
    public static final String LEVEL = "Level: ";
    public static final Paint INFOBAR_COLOR = Color.BLUE;
    public static final Paint TITLE_COLOR = Color.WHITE;

    private DropShadow effect;
    private Text myScoreText;

    public InfoBar(double width, double height){
        super(0, 0, width, height);
        this.setFill(INFOBAR_COLOR);
    }

    /**
     * Creates title Text.
     */

    public Text createTitleText(){
        Text title = createEffect();
        title.setX(this.getX());
        title.setY(this.getY());
        title.setText(TITLE);
        title.setFont(Font.font(null, FontWeight.BOLD, 40));
        return title;
    }

    /**
     * Creates level text.
     */

    public Text updateLevelText(int level, int screenWidth){
        Text levelText = createEffect();
        levelText.setText(LEVEL + level);
        levelText.setFont(Font.font(null, FontWeight.BOLD, 30));
        levelText.setX((screenWidth / 2) - (levelText.getBoundsInLocal().getWidth() / 2));
        this.setY(this.getY());
        return levelText;

    }

    /**
     * Creates score text.
     */

    public Text createScoreText(int score){
        Text scoreText = createEffect();
        scoreText.setText(SCORE + score);
        scoreText.setFont(Font.font(null, FontWeight.BOLD, 30));
        scoreText.setX(this.getX());
        this.setY(this.getY());
        scoreText.setTranslateX(this.getX());
        myScoreText = scoreText;
        return scoreText;
    }

    /**
     * Updates score text with current score.
     */

    public void updateScoreText(int score){
        myScoreText.setText(SCORE + score);
    }

    // Text effects found on https://docs.oracle.com/javafx/2/text/jfxpub-text.html
    private Text createEffect(){
        effect = new DropShadow();
        effect.setOffsetY(3.0f);
        effect.setColor(Color.color(0.4f, 0.4f, 0.4f));

        Text message = new Text();
        message.setEffect(effect);
        message.setCache(true);
        message.setFill(TITLE_COLOR);
        message.setTranslateX(this.getX());
        message.setTranslateY(40);
        return message;
    }


}
