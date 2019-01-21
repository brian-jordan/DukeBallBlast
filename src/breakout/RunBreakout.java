package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Project 1: Breakout Game
 * Duke CompSci 308 Spring 2019 - Duvall
 * Date Created: 1/14/2019
 * Date Last Modified: 1/20/2019
 * @author Brian J. Jordan (bjj17)
 */

public class RunBreakout extends Application {

    public static final String TITLE = "Breakout Game";
    public static final String BLOCK_LAYOUT = "BlockDesigns.txt";
    public static final int WINDOW_WIDTH = 425;
    public static final int WINDOW_HEIGHT = 500;
    public static final int DISPLAY_HEIGHT = 50;
    public static final int BOUNCER_SPEED = 120;
    public static final int POWERBLOCK_SPEED = 70;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    public static final Paint LOSE_MESSAGE_COLOR = Color.RED;
    public static final Paint MESSAGE_COLOR = Color.BLUE;
    public static final double[] POWERUP_PERCENTAGES = {0.10, 0.15, 0.20, 0.25};
    public static final int HITS_WITH_POWER = 5;
    public static final double POWER_FACTOR = 1.5;
    public static final double LEVEL_FACTOR = 1.15;
    public static final String BACKGROUND_IMAGE = "Basketball_Court.gif"; // From http://seankingstons.blogspot.com/2010/05/duke-basketball-court.html
    public static final String BLUEDEVIL_IMAGE = "blueDevilLogo.gif"; // From https://www.ebay.com/itm/Duke-Blue-Devils-Basketball-stencil-logo-Reusalble-Pattern-10-Mil-Mylar-/162860259180
    public static final String START_MESSAGE = "Destroy all the bricks to move to the next level.\n\nPress the Space bar to begin";
    public static final String LOSE_MESSAGE = "You Lose";
    public static final String WIN_MESSAGE = "You Win!";

    private Stage myStage;
    private Scene myScene;
    private Group root;
    private KeyFrame myFrame;
    private Timeline myAnimation;
    private int level;
    private int lives;
    private int myScore;
    private int numBlocksLeft;
    private ImageView myBackground;
    private ImageView myLogo;
    private Paddle myPaddle;
    private Bouncer myBouncer;
    private Block[][] myBlocks;
    private String[] levelFormations;
    private ArrayList<PowerBlock> powerBlockList;
    private Bouncer[] myLives;
    private InfoBar myInfoBar;
    private Text myTitleText;
    private Text myLevelDisplay;
    private Text myScoreDisplay;
    private boolean paused;
    private int myPowerHits = -1;
    private char myCurrentPower;
    private boolean isPowerUpOn;

    /**
     * Initialize Display and updates
     */

    @Override
    public void start (Stage stage){
        myStage = stage;
        level = 0;
        staticScreen(true, false);
    }

    private void staticScreen(boolean start, boolean win){
        paused = true;
        root = new Group();
        myScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
        myInfoBar = new InfoBar(WINDOW_WIDTH, DISPLAY_HEIGHT);
        myTitleText = myInfoBar.createTitleText();
        createLogo(WINDOW_WIDTH, WINDOW_HEIGHT);
        createMessage(start, win);
        root.getChildren().add(myInfoBar);
        root.getChildren().add(myTitleText);
        setMyStage();
    }

    private void playGame(){
        level++;
        myScore = 0;
        lives = 3;
        myLives = new Bouncer[3];
        Scanner sc = new Scanner(this.getClass().getClassLoader().getResourceAsStream(BLOCK_LAYOUT));
        levelFormations = readFile(sc);
        powerBlockList = new ArrayList<>();
        newScene(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
        createAnimation();
    }

    private void newScene(int screenWidth, int screenHeight, Paint background){
        paused = true;
        myScene = setupGame(screenWidth, screenHeight, background);
        setMyStage();
    }

    private Scene setupGame(int screenWidth, int screenHeight, Paint background){
        root = new Group();
        var scene = new Scene(root, screenWidth, screenHeight, background);
        myPaddle = new Paddle(screenWidth, screenHeight, level, LEVEL_FACTOR);
        myBouncer = new Bouncer(screenWidth, screenHeight, myPaddle.getBoundsInLocal().getHeight(), level, LEVEL_FACTOR);
        myBackground = createBackgroundImage();
        myLevelDisplay = myInfoBar.updateLevelText(level,WINDOW_WIDTH);
        myScoreDisplay = myInfoBar.createScoreText(myScore);
        root.getChildren().add(myInfoBar);
        root.getChildren().add(myScoreDisplay);
        root.getChildren().add(myLevelDisplay);
        root.getChildren().add(myBackground);
        myBlocks = createBlockLayoutAndPowerUps();
        setBlockLayout(myBlocks);
        numBlocksLeft = 48;
        setLives();
        root.getChildren().add(myPaddle);
        root.getChildren().add(myBouncer);
        return scene;
    }

    private void setMyStage(){
        myStage.setScene(myScene);
        myStage.setTitle(TITLE);
        myStage.show();
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    }

    private void createAnimation(){
        myFrame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(myFrame);
        myAnimation.play();
    }

    private ImageView createBackgroundImage(){
        var backGround = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(BACKGROUND_IMAGE)));
        backGround.setFitWidth(WINDOW_WIDTH);
        backGround.setFitHeight(WINDOW_HEIGHT - DISPLAY_HEIGHT);
        backGround.setY(DISPLAY_HEIGHT);
        return backGround;
    }

    private void createMessage(boolean start, boolean win) {
        Text message = new Text();
        if (start) {
            message.setText(START_MESSAGE);
            message.setFill(MESSAGE_COLOR);
        } else if (win) {
            message.setText(WIN_MESSAGE);
            message.setFill(MESSAGE_COLOR);
        } else {
            message.setText(LOSE_MESSAGE);
            message.setFill(LOSE_MESSAGE_COLOR);
        }
        message.setTextAlignment(TextAlignment.CENTER);
        message.setFont(Font.font(null, FontWeight.BOLD, 20));
        message.setWrappingWidth(WINDOW_WIDTH * 0.75);                                                                  // Value chosen because displays well.
        message.setX((WINDOW_WIDTH / 2) - (message.getBoundsInLocal().getWidth() / 2));
        message.setY((WINDOW_HEIGHT - ((WINDOW_HEIGHT - (myLogo.getY() + myLogo.getBoundsInLocal().getHeight())) / 2))
                - (message.getBoundsInLocal().getHeight() / 2));
        root.getChildren().add(message);
    }

    private void createLogo(int screenWidth, int screenHeight){
        myLogo = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(BLUEDEVIL_IMAGE)));
        myLogo.setX((screenWidth / 2) - (myLogo.getBoundsInLocal().getWidth() / 2));
        myLogo.setY(DISPLAY_HEIGHT);
        root.getChildren().add(myLogo);
    }

    private void step (double elapsedTime){
        if (level != 0){
            myBouncer.updateBouncerMovement(elapsedTime);
            if (! powerBlockList.isEmpty()) {
                for (int i = 0; i < powerBlockList.size(); i++) {
                    PowerBlock pb = powerBlockList.get(i);
                    pb.updatePowerBlockMovement(elapsedTime);
                    if (pb.getBoundsInParent().intersects(myPaddle.getBoundsInParent()) && (pb.getY() + pb.getBoundsInLocal().getHeight()) <= (myPaddle.getY() + (0.1 * myPaddle.getBoundsInLocal().getHeight()))) {
                        paddleCatch(pb);
                    }
                    if (pb.getY() >= myScene.getHeight()) {
                        removePB(pb);
                    }
                }
            }

            if (myPowerHits == 0){
                endPowerUp();
            }

            if (myBouncer.getY() <= DISPLAY_HEIGHT + 0.05){                                                             // Buffer added to reduce glitches.
                myBouncer.deflectY();
            }
            else if (myBouncer.getX() <= -0.05 || myBouncer.getX() >= myScene.getWidth() + 0.05 - myBouncer.getBoundsInLocal().getWidth()){ // Buffer added to reduce glitches.
                myBouncer.deflectX();
            }
            else if (myBouncer.getBoundsInParent().intersects(myPaddle.getBoundsInParent())){
                paddleDeflect();
            }
            else if (myBouncer.getY() >= myScene.getHeight()){
                lives-= 1;
                if (lives == 0){
                    level = 5;
                    paused = true;
                    staticScreen(false, false);
                    myAnimation.stop();
                }
                else {
                    myLives[lives].removeLife();
                    paused = true;
                    endPowerUp();
                    if (! powerBlockList.isEmpty()){
                        for (int i = 0; i < powerBlockList.size(); i++) {
                            powerBlockList.get(i).removePowerBlock();
                        }
                    }
                myBouncer.setBouncerSpeed(0);
                myBouncer.startingPosition(WINDOW_WIDTH, WINDOW_HEIGHT, myPaddle.getBoundsInLocal().getHeight());
                myPaddle.startingPosition(WINDOW_WIDTH, WINDOW_HEIGHT);
                }

            }
            checkForBlockCollision(myBlocks);
            myInfoBar.updateScoreText(myScore);
            if (numBlocksLeft == 0){
                if (level == 4){
                    staticScreen(false, true);
                }
                else advanceLevel(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
            }

            if (myPaddle.getX() + myPaddle.getBoundsInParent().getWidth() <= 0){
                myPaddle.setX(myScene.getWidth());
                if (paused) {
                    warpBouncer();
                }
            }
            else if(myPaddle.getX() >= myScene.getWidth()){
                myPaddle.setX(-myPaddle.getBoundsInParent().getWidth());
                if (paused) {
                    warpBouncer();
                }
            }
        }
    }

    private void handleKeyInput (KeyCode code){
        if (code == KeyCode.RIGHT){
            myPaddle.setX(myPaddle.getX() + myPaddle.PADDLE_SPEED);
            if (paused){
                myBouncer.setX(myBouncer.getX() + myPaddle.PADDLE_SPEED);
            }
        }
        else if (code == KeyCode.LEFT){
            myPaddle.setX(myPaddle.getX() - myPaddle.PADDLE_SPEED);
            if (paused){
                myBouncer.setX(myBouncer.getX() - myPaddle.PADDLE_SPEED);
            }
        }
        else if (code == KeyCode.SPACE){
            if (level == 0){
                playGame();
            }
            else if (level == 1){
                paused = false;
                myBouncer.setBouncerSpeed(BOUNCER_SPEED);
            }
            else if (level > 1){
                paused = false;
                myBouncer.setBouncerSpeed((int)(BOUNCER_SPEED * LEVEL_FACTOR));
            }
        }
        else if (code == KeyCode.L){
            advanceLevel(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
        }
        else if (code == KeyCode.DIGIT1){
            level = 1;
            newScene(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
        }
        else if (code == KeyCode.DIGIT2){
            level = 2;
            newScene(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
        }
        else if (code == KeyCode.DIGIT3){
            level = 3;
            newScene(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
        }
        else if (code == KeyCode.DIGIT4){
            level = 4;
            newScene(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
        }
        else if (code == KeyCode.R){
            newScene(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
        }
        else if (code == KeyCode.A && lives < 3){
            lives++;
            myLives[lives - 1].placeLife(WINDOW_WIDTH, DISPLAY_HEIGHT, lives);
        }

    }

    private Block[][] createBlockLayoutAndPowerUps(){
        Block[][] createdBlocks = new Block[6][8];
        for (int y = 0; y < createdBlocks[0].length; y++){
            for (int x = 0; x < createdBlocks.length; x++){
                Block newBlock = new Block(x, y, DISPLAY_HEIGHT);
                double randomNumber = Math.random();
                boolean isPowerUp = false;
                if (randomNumber < POWERUP_PERCENTAGES[POWERUP_PERCENTAGES.length - level]){
                    isPowerUp = true;
                    createPowerBlock(isPowerUp, newBlock);
                }
                else if (randomNumber > (1 - POWERUP_PERCENTAGES[level - 1])){
                    createPowerBlock(isPowerUp, newBlock);
                }
                createdBlocks[x][y] = newBlock;
                root.getChildren().add(newBlock);
            }
        }
        return createdBlocks;
    }

    private void setBlockLayout(Block[][] blocks){
        String levelLayout = levelFormations[level];
        String[] blockCoordinates = levelLayout.split(" ");
        for (int i = 0; i < blockCoordinates.length; i++){
            int xValue = Character.getNumericValue(blockCoordinates[i].charAt(0));
            int yValue = Character.getNumericValue(blockCoordinates[i].charAt(1));
            blocks[xValue][yValue].setToBlue();
        }
    }

    private void createPowerBlock(boolean isPowerUp, Block currentBlock){
        PowerBlock newPowerBlock = new PowerBlock(isPowerUp, (currentBlock.getX() + (currentBlock.getBoundsInLocal().getWidth() / 2)), (currentBlock.getY() + (currentBlock.getBoundsInLocal().getHeight() / 2)));
        currentBlock.setPowerBlock(newPowerBlock);
        root.getChildren().add(newPowerBlock);
    }

    private void checkForBlockCollision(Block[][] twoDArray){
        for (int y = 0; y < twoDArray[0].length; y++) {
            for (int x = 0; x < twoDArray.length; x++) {
                if (myBouncer.getBoundsInParent().intersects(myBlocks[x][y].getBoundsInParent())){
                    if(myBouncer.getX() + myBouncer.getBoundsInLocal().getWidth() <= (myBlocks[x][y].getX() + (myBlocks[x][y].getBoundsInLocal().getWidth() * 0.1)) || // Buffer added to reduce glitches.
                            myBouncer.getX() >= myBlocks[x][y].getX() + (myBlocks[x][y].getBoundsInLocal().getWidth() * 0.9)){
                        myBouncer.deflectX();
                    }
                    else myBouncer.deflectY();
                    int alive = myBlocks[x][y].blockHit();
                    if (alive == 0){
                        numBlocksLeft--;
                        myScore++;
                        checkIfPowerBlock(myBlocks[x][y]);
                    }
                }
            }
        }
    }

    private void advanceLevel(int screenWidth, int screenHeight, Paint background){
        level++;
        newScene(screenWidth, screenHeight, background);
    }

    private void implementPower(PowerBlock pb){
        myPowerHits = HITS_WITH_POWER;
        myCurrentPower = pb.getPower();
        isPowerUpOn = pb.getIsPowerUp();
        if (myCurrentPower == 'p'){
            if (isPowerUpOn){
                myPaddle.increasePaddleSize(POWER_FACTOR);
            }
            else myPaddle.decreasePaddleSize(POWER_FACTOR);
        }
        else if (myCurrentPower == 'b'){
            if (isPowerUpOn) {
                myBouncer.decreaseBouncerSpeed(POWER_FACTOR);
            }
            else {
                myBouncer.increaseBouncerSpeed(POWER_FACTOR);
            }

        }
    }

    private void endPowerUp(){
        if (myCurrentPower == 'p'){
            if (isPowerUpOn){
                myPaddle.decreasePaddleSize(POWER_FACTOR);
            }
            else myPaddle.increasePaddleSize(POWER_FACTOR);
        }
        else if (myCurrentPower == 'b'){
            if(isPowerUpOn){
                myBouncer.increaseBouncerSpeed(POWER_FACTOR);
            }
            else{
                myBouncer.decreaseBouncerSpeed(POWER_FACTOR);
            }
        }
        myCurrentPower = 'n';
        myPowerHits = -1;
    }

    private void checkIfPowerBlock(Block hitBlock){
        if (hitBlock.isPowerBlock()){
            if (hitBlock.getMyPowerBlock().getPower() == 'd'){
                // destroy surrounding blocks
                if (hitBlock.getXVal() - 1 >= 0 && myBlocks[hitBlock.getXVal() - 1][hitBlock.getYVal()].getHealth() > 0){
                    myBlocks[hitBlock.getXVal() - 1][hitBlock.getYVal()].destroyBlock();
                    numBlocksLeft--;
                    myScore++;
                    checkIfPowerBlock(myBlocks[hitBlock.getXVal() - 1][hitBlock.getYVal()]);
                }
                if((hitBlock.getXVal() + 1 < myBlocks.length) && (myBlocks[hitBlock.getXVal() + 1][hitBlock.getYVal()].getHealth() > 0)){
                    myBlocks[hitBlock.getXVal() + 1][hitBlock.getYVal()].destroyBlock();
                    numBlocksLeft--;
                    myScore++;
                    checkIfPowerBlock(myBlocks[hitBlock.getXVal() + 1][hitBlock.getYVal()]);
                }
                if((hitBlock.getYVal() - 1 >= 0) && myBlocks[hitBlock.getXVal()][hitBlock.getYVal() - 1].getHealth() > 0) {
                    myBlocks[hitBlock.getXVal()][hitBlock.getYVal() - 1].destroyBlock();
                    numBlocksLeft--;
                    myScore++;
                    checkIfPowerBlock(myBlocks[hitBlock.getXVal()][hitBlock.getYVal() - 1]);
                }
                if((hitBlock.getYVal() + 1 < myBlocks[0].length) && myBlocks[hitBlock.getXVal()][hitBlock.getYVal() + 1].getHealth() > 0) {
                    myBlocks[hitBlock.getXVal()][hitBlock.getYVal() + 1].destroyBlock();
                    numBlocksLeft--;
                    myScore++;
                    checkIfPowerBlock(myBlocks[hitBlock.getXVal()][hitBlock.getYVal() + 1]);
                }
                if((hitBlock.getXVal() - 1 >= 0) && (hitBlock.getYVal() - 1 >= 0) && myBlocks[hitBlock.getXVal() - 1][hitBlock.getYVal() - 1].getHealth() > 0) {
                    myBlocks[hitBlock.getXVal() - 1][hitBlock.getYVal() - 1].destroyBlock();
                    numBlocksLeft--;
                    myScore++;
                    checkIfPowerBlock(myBlocks[hitBlock.getXVal() - 1][hitBlock.getYVal() - 1]);
                }
                if((hitBlock.getXVal() - 1 >= 0) && (hitBlock.getYVal() + 1 < myBlocks[0].length) && myBlocks[hitBlock.getXVal() - 1][hitBlock.getYVal() + 1].getHealth() > 0) {
                    myBlocks[hitBlock.getXVal() - 1][hitBlock.getYVal() + 1].destroyBlock();
                    numBlocksLeft--;
                    myScore++;
                    checkIfPowerBlock(myBlocks[hitBlock.getXVal() - 1][hitBlock.getYVal() + 1]);
                }
                if((hitBlock.getXVal() + 1 < myBlocks.length) && (hitBlock.getYVal() - 1 >= 0) && myBlocks[hitBlock.getXVal() + 1][hitBlock.getYVal() - 1].getHealth() > 0) {
                    myBlocks[hitBlock.getXVal() + 1][hitBlock.getYVal() - 1].destroyBlock();
                    numBlocksLeft--;
                    myScore++;
                    checkIfPowerBlock(myBlocks[hitBlock.getXVal() + 1][hitBlock.getYVal() - 1]);
                }
                if((hitBlock.getXVal() + 1 < myBlocks.length) && (hitBlock.getYVal() + 1 < myBlocks[0].length) && myBlocks[hitBlock.getXVal() + 1][hitBlock.getYVal() + 1].getHealth() > 0) {
                    myBlocks[hitBlock.getXVal() + 1][hitBlock.getYVal() + 1].destroyBlock();
                    numBlocksLeft--;
                    myScore++;
                    checkIfPowerBlock(myBlocks[hitBlock.getXVal() + 1][hitBlock.getYVal() + 1]);
                }
            }
            else {
                powerBlockList.add(hitBlock.getMyPowerBlock());
                hitBlock.getMyPowerBlock().setPowerBlockSpeed(POWERBLOCK_SPEED);
            }

        }
    }

    private void setLives(){
        for (int i = 0; i < myLives.length; i++){
            myLives[i] = new Bouncer(i + 1, WINDOW_WIDTH, DISPLAY_HEIGHT);
            root.getChildren().add(myLives[i]);
        }
    }

    private void paddleDeflect(){
        if (myBouncer.getX() + myBouncer.getBoundsInLocal().getWidth() <= myPaddle.getX() ||
            myBouncer.getX() >= myPaddle.getX() + myPaddle.getBoundsInLocal().getWidth()){
            myBouncer.deflectX();
        }
        else myBouncer.deflectY();

        if (myBouncer.getX() + (myBouncer.getBoundsInLocal().getWidth() / 2) <= (myPaddle.getX() + (myPaddle.getBoundsInLocal().getWidth() * 0.3))){
            myBouncer.deflectLeft();
        }
        else if ((myBouncer.getX() + (myBouncer.getBoundsInLocal().getWidth() / 2)) >= (myPaddle.getX() + (myPaddle.getBoundsInLocal().getWidth() * 0.7))){
            myBouncer.deflectRight();
        }
    }

    private void paddleCatch(PowerBlock movingPB){
        removePB(movingPB);
        if (myPowerHits != -1){
            endPowerUp();
        }
        implementPower(movingPB);
    }

    private void removePB(PowerBlock movingPB){
        movingPB.setPowerBlockSpeed(0);
        movingPB.removePowerBlock();
        powerBlockList.remove(movingPB);
    }

    private void warpBouncer(){
        myBouncer.setX(myPaddle.getX() + (0.5 * myPaddle.getBoundsInLocal().getWidth()) - (0.5 * myBouncer.getBoundsInLocal().getWidth()));
    }

    private String[] readFile(Scanner scanner){
        String[] stringArray = new String[5];
        int index = 1;
        while (scanner.hasNextLine()){
            stringArray[index] = scanner.nextLine();
            index++;
        }
        return stringArray;
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }

}
