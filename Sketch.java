import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Sketch extends PApplet {

    // List the variables in the code
    PImage imgBackground;
    PImage imgBunny;
    PImage imgCarpet;
    PImage imgEgg;
    PImage imgWand;
    PImage imgWolf;
    PImage imgBullet;
    PImage imgTitle;
    PImage imgLives;

    boolean wPressed = false;
    boolean sPressed = false;
    boolean dPressed = false;
    boolean aPressed = false;
    float fltBunnyX;
    float fltBunnyY;

    int intLives = 5;
    int currentStage = 1; 
    int wolvesInCurrentStage = 1; 
    boolean eggShoot = false;
    boolean title = true;
    boolean wolfShoot = false;
    boolean collision = false;
    boolean gameOver = false;
    ArrayList<Float> eggXPosition = new ArrayList<>();
    ArrayList<Float> eggYPosition = new ArrayList<>();
    ArrayList<Float> bulletXPosition = new ArrayList<>();
    ArrayList<Float> bulletYPosition = new ArrayList<>();
    ArrayList<Float> wolfXPosition = new ArrayList<>();
    ArrayList<Float> wolfYPosition = new ArrayList<>();
    ArrayList<Float> wolfXMove = new ArrayList<>();
    ArrayList<Float> wolfYMove = new ArrayList<>();
    

    /** 
     * Called once at the beginning of execution, put your size call here
     */
    public void settings() {
      size(945, 590);
  }

  /**
   * Called once at the beginning of execution. Add initial set up values here i.e background, stroke, fill, etc.
   */
  public void setup() {
      imgBackground = loadImage("Background.png");
      imgBunny = loadImage("Bunnyshoot.png");
      imgBunny.resize(imgBunny.width / 4, imgBunny.height / 4);
      imgEgg = loadImage("egg.png");
      imgEgg.resize(imgEgg.width / 7, imgEgg.height / 7);
      imgWand = loadImage("wand.png");
      imgWand.resize(imgWand.width / 11, imgWand.height / 11);
      imgWolf = loadImage("wolf.png");
      imgWolf.resize(imgWolf.width / 5, imgWolf.height / 5);
      imgBullet = loadImage("bullet.png");
      imgBullet.resize(imgBullet.width / 15, imgBullet.height / 15);
      imgTitle = loadImage("titlescreen.png");
      imgTitle.resize(width, height);   
      imgLives = loadImage("lives.png");
      imgLives.resize(imgLives.width / 35, imgLives.height / 35);
      for (int i = 0; i < wolvesInCurrentStage; i++) {
        float wolfX = width - imgWolf.width; 
        float wolfY = random(0, height - imgWolf.height);
        float wolfXMovement = random(-2, -1); 
        float wolfYMovement = random(-1, 1); 
        wolfXPosition.add(wolfX);
        wolfYPosition.add(wolfY);
        wolfXMove.add(wolfXMovement);
        wolfYMove.add(wolfYMovement);
  }
}
  /**
     * Called repeatedly, anything drawn to the screen goes here
     */
    public void draw() {
        if (title) {
            drawLoadingScreen();
        } else {
            drawGameScreen();
            checkStageCompletion();
        }
    }
// Check for amount of wolves in the stage and spawn them according to the stage number
public void checkStageCompletion() {
    if (wolvesInCurrentStage == 0) {
        currentStage++;
        wolvesInCurrentStage = currentStage;
        spawnWolves();
    }
}
// Spawn wolves and define their movement
public void spawnWolves() {
    for (int i = 0; i < wolvesInCurrentStage; i++) {
        float wolfX = width - imgWolf.width; 
        float wolfY = random(0, height - imgWolf.height);
        float wolfXMovement = random(-2, -1); 
        float wolfYMovement = random(-1, 1); 
        wolfXPosition.add(wolfX);
        wolfYPosition.add(wolfY);
        wolfXMove.add(wolfXMovement);
        wolfYMove.add(wolfYMovement);
    }
}

  public void drawLoadingScreen() {

      // Set the background  
      background(imgTitle);

      fill(255);
      rect(width / 2 - 208, height - 100, 430, 80, 10);
        
      // Start button 
      fill(255, 0, 0);
      textSize(32);
      text("Start", width / 2 - 30, height - 50);
  }
  public void mousePressed() {

    if (title) {
        // Check if the mouse is pressed within a specific part of the screen
        if (mouseX > width / 2 - 215 && mouseX < width / 2 + 215 && mouseY > height - 100 && mouseY < height - 10) {
            title = false;
        }
    } else if (gameOver) {
        // Check if the mouse is pressed on the restart button
        if (mouseX > width / 2 - 50 && mouseX < width / 2 + 50 && mouseY > height / 2 + 100 && mouseY < height / 2 + 140) {
            restartGame();
        }
    }
}

 /**
     * Calculates the player's score based on remaining lives and current stage.
     * The score is determined by multiplying the remaining lives and the stage number.
     * @param remainingLives The number of lives that the player has.
     * @param currentStage   The current stage the player is on.
     * @return The calculated player score based on remaining lives and current stage.
     */
public int calculatePlayerScore(int remainingLives, int currentStage) {
    return remainingLives * currentStage;}
    
    public void drawGameScreen() {

        // Check if game is over
        if (gameOver) {
        drawGameOverScreen();
        }
        else{
         if (intLives <= 0) {
            gameOver = true;
        }
        {

        //generate the bunny, background, and wnad within the game along with stating the stage number

        background(255);
        image(imgBackground, 0, 0);
        text("Stage: " + currentStage, width / 2 - 30, 30);
        image(imgBunny, fltBunnyX, fltBunnyY);
        image(imgWand, fltBunnyX + 52, fltBunnyY + 20);
        }

        // Display the score from the method
        int playerScore = calculatePlayerScore(intLives, currentStage);
        fill(255);
        textSize(20);
        text("Score: " + playerScore, width / 2 + 145, 29);

    // Draw wolves and movement
    for (int i = wolvesInCurrentStage - 1; i >= 0; i--) {
        float wolfX = wolfXPosition.get(i);
        float wolfY = wolfYPosition.get(i);

        // Check for collisions between eggs and wolves
        boolean wolfHit = false;
        for (int a = eggXPosition.size() - 1; a >= 0; a--) {
            float eggX = eggXPosition.get(a);
            float eggY = eggYPosition.get(a);

            // Check if there is a collision
            if (eggX > wolfX && eggX < wolfX + imgWolf.width && eggY > wolfY && eggY < wolfY + imgWolf.height) {

                // Remove the egg
                eggXPosition.remove(a);
                eggYPosition.remove(a);

                // Mark the wolf as hit and remove it
                wolfHit = true;
                break; 
            }
        }

        // Draw wolf only if it is not hit
        if (!wolfHit) {
            image(imgWolf, wolfX, wolfY);

            // Adjust wolf positions 
            wolfX += wolfXMove.get(i);
            wolfY += wolfYMove.get(i);

            // Check for screen boundaries
            if (wolfX < 550 || wolfX > width - imgWolf.width) {
                wolfXMove.set(i, wolfXMove.get(i) * -1);
            }

            if (wolfY < 0 || wolfY > height - imgWolf.height) {
                wolfYMove.set(i, wolfYMove.get(i) * -1);
            }

            // Update positions
            wolfXPosition.set(i, wolfX);
            wolfYPosition.set(i, wolfY);
        } else {
            // Remove the wolf
            wolfXPosition.remove(i);
            wolfYPosition.remove(i);
            wolfXMove.remove(i);
            wolfYMove.remove(i);
            wolvesInCurrentStage--;
        }
    }
        // Draw the lives
        fill(255, 0, 0);
        for (int i = 0; i < intLives; i++) {
            image(imgLives, 10 + i * 30, 10);
        }
    // Draw wolves and their bullet shooting
    for (int i = wolvesInCurrentStage - 1; i >= 0; i--) {
        float wolfX = wolfXPosition.get(i);
        float wolfY = wolfYPosition.get(i);

        // Draw wolf
        image(imgWolf, wolfX, wolfY);

        // Check for bullets and the speed at rate of fire from the wolves
        if (frameCount % 90 == 0) {
            float wolfBulletX = wolfX;
            float wolfBulletY = wolfY + imgWolf.height / 2;
            bulletXPosition.add(wolfBulletX);
            bulletYPosition.add(wolfBulletY);
        }
    }

    // Draw wolf bullets and their movements
    for (int i = bulletXPosition.size() - 1; i >= 0; i--) {
        float wolfBulletX = bulletXPosition.get(i);
        float wolfBulletY = bulletYPosition.get(i);
        image(imgBullet, wolfBulletX, wolfBulletY);

        // Move the bullet
        wolfBulletX -= 4; 
        bulletXPosition.set(i, wolfBulletX);

        // Remove bullets that go off the screen
        if (wolfBulletX < 0) {
            bulletXPosition.remove(i);
            bulletYPosition.remove(i);
        }

        // Check for collisions between bunny excluding magic carpet and bullets
        if (dist(fltBunnyX + imgBunny.width / 4, fltBunnyY + imgBunny.height / 4, wolfBulletX, wolfBulletY) < imgBunny.width / 4 + imgBullet.width / 4) {
            intLives--;

            // Remove the bullet
            bulletXPosition.remove(i);
            bulletYPosition.remove(i);
        }
    }
        // Draw eggs and their movements
        for (int i = 0; i < eggXPosition.size(); i++) {
            float eggX = eggXPosition.get(i);
            float eggY = eggYPosition.get(i);
            image(imgEgg, eggX, eggY);

            // Adjust egg speed along with the right movement
            eggX += 10;
            eggXPosition.set(i, eggX);

            // Remove eggs that are off screen
            if (eggX > width) {
                eggXPosition.remove(i);
                eggYPosition.remove(i);
            }
        }
        // Movement & edge detection along with keeping the bunny on the left half of the screen
        if (wPressed && fltBunnyY >= 0) {
            fltBunnyY -= 3;
        }
        if (sPressed && fltBunnyY <= height - 100) {
            fltBunnyY += 3;
        }
        if (aPressed && fltBunnyX >= + 15) {
            fltBunnyX -= 3;
        }
        if (dPressed && fltBunnyX <= width - 550) {
            fltBunnyX += 3;
        }
      }
    }   

    // Check if w,a,s,d keys or space bar are pressed and execute accordingly
    public void keyPressed() {
        if (key == 'W' || key == 'w') {
            wPressed = true;
        } else if (key == 'A' || key == 'a') {
            aPressed = true;
        } else if (key == 'S' || key == 's') {
            sPressed = true;
        } else if (key == 'D' || key == 'd') {
            dPressed = true;
        } else if (key == ' ' && !eggShoot) {
            eggShoot = true;
            float eggX = fltBunnyX + 95;
            float eggY = fltBunnyY + 28;
            eggXPosition.add(eggX);
            eggYPosition.add(eggY);
        }
    }

    // Check if w,a,s,d keys or space bar are pressed and execute accordingly
    public void keyReleased() {
        if (key == 'W' || key == 'w') {
            wPressed = false;
        } else if (key == 'S' || key == 's') {
            sPressed = false;
        } else if (key == 'D' || key == 'd') {
            dPressed = false;
        } else if (key == 'A' || key == 'a') {
            aPressed = false;
        } else if (key == ' ') {
            eggShoot = false;
        }
    }

    // Generate the game over screen when the player loses
    public void drawGameOverScreen() {
        background(255);

        fill(255, 0, 0);
        textSize(40);
        textAlign(CENTER, CENTER);
        text("Game Over", width / 2, height / 2 - 50);
        textSize(20);
        fill(100, 255, 0);
        rect(width / 2 - 50, height / 2 + 99, 100, 46, 10);
        fill(150);
        textSize(18);
        textAlign(CENTER, CENTER);
        text("Restart", width / 2, height / 2 + 120);
    }

    // Restart game if desired
    public void restartGame() {
        intLives = 5;
        currentStage = 1;
        wolvesInCurrentStage = 1;
        gameOver = false;
        eggXPosition.clear();
        eggYPosition.clear();
        bulletXPosition.clear();
        bulletYPosition.clear();
        wolfXPosition.clear();
        wolfYPosition.clear();
        wolfXMove.clear();
        wolfYMove.clear();
        spawnWolves();
    }
}