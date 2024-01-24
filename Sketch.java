import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Sketch extends PApplet {

    PImage imgBackground;
    PImage imgBunny;
    PImage imgCarpet;
    PImage imgEgg;
    PImage imgWand;
    PImage imgWolf;
    PImage imgBullet;
    PImage imgTitle;

    boolean wPressed = false;
    boolean sPressed = false;
    boolean dPressed = false;
    boolean aPressed = false;
    float fltBunnyX;
    float fltBunnyY;
    float fltWandX;
    float fltWandY;


    int intLives = 5;
    int currentStage = 1; 
    int wolvesInCurrentStage = 2; 
    boolean eggShoot = false;
    boolean title = true;
    boolean wolfShoot = false;
    boolean collision = false;
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
      size(945, 600);
  }

  /**
   * Called once at the beginning of execution. Add initial set up values here i.e background, stroke, fill, etc.
   */
  public void setup() {
      imgBackground = loadImage("Background.png");
      imgBunny = loadImage("Bunnyshoot.png");
      imgBunny.resize(imgBunny.width / 3, imgBunny.height / 3);
      imgEgg = loadImage("egg.png");
      imgEgg.resize(imgEgg.width / 7, imgEgg.height / 7);
      imgWand = loadImage("wand.png");
      imgWand.resize(imgWand.width / 8, imgWand.height / 8);
      imgWolf = loadImage("wolf.png");
      imgWolf.resize(imgWolf.width / 4, imgWolf.height / 4);
      imgBullet = loadImage("bullet.png");
      imgBullet.resize(imgBullet.width / 10, imgBullet.height / 10);
      imgTitle = loadImage("titlescreen.png");
      imgTitle.resize(width, height);   
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

public void checkStageCompletion() {
    if (wolvesInCurrentStage == 0) {
        currentStage++;

        wolvesInCurrentStage += 2;

        
        spawnWolves();
    }
}

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
        
      // The start button
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
      }
    }
    public void drawGameScreen() {
        background(255);
        image(imgBackground, 0, 0);
        image(imgBunny, fltBunnyX, fltBunnyY);
        image(imgWand, fltBunnyX + 67, fltBunnyY + 26);

        
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
            if (eggX > wolfX && eggX < wolfX + imgWolf.width &&
                eggY > wolfY && eggY < wolfY + imgWolf.height) {

                // Remove the egg
                eggXPosition.remove(a);
                eggYPosition.remove(a);

                // Mark this wolf as hit and remove it
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
        
        fill(255, 0, 0);
        for (int i = 0; i < intLives; i++) {
            rect(10 + i * 30, 10, 20, 20);
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
        if (wPressed && fltBunnyY >= 0) {
            fltBunnyY -= 3;
        }
        if (sPressed && fltBunnyY <= height - 150) {
            fltBunnyY += 3;
        }
        if (aPressed && fltBunnyX >= +10) {
            fltBunnyX -= 3;
        }
        if (dPressed && fltBunnyX <= width - 140) {
            fltBunnyX += 3;
        }
      }
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
    }