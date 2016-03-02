package cs193a.stanford.edu.hw5_snake;

import android.content.*;
import android.graphics.*;
import android.util.AttributeSet;

import java.util.*;
import stanford.androidlib.graphics.*;
import stanford.androidlib.util.*;

public class SnakeGameView extends GCanvas {

    private GSprite head;
    private GSprite body;
    private GLabel score;
    private int direction;
    private float gridSquareWidth;
    private Vector<GSprite> snake;
    private GSprite food;
    int points;
    private boolean gameOver;


    public SnakeGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        setUpGame();

    }

    private void setUpGame() {
        points = 0;
        snake = new Vector<>();
        direction = 1;
        head = new GSprite(loadScaledBitmap(R.drawable.snakehead, 4));
        gridSquareWidth = head.getWidth();
        head.setCollisionMargin(10);
        add(head, 10 + 5*gridSquareWidth, 10 + 5*gridSquareWidth);
        body = new GSprite(loadScaledBitmap(R.drawable.snakebody, 4));
        add(body, 10 + 4*gridSquareWidth, 10 + 5*gridSquareWidth);
        snake.add(body);
        score = new GLabel("Score: 0", 10, 10);
        score.setFont(Typeface.MONOSPACE, Typeface.BOLD, 40f);
        add(score);
        food = new GSprite(loadScaledBitmap(R.drawable.food, 9));
        add(food);
        generateFood();
    }

    public void startNewGame() {
        if (!gameOver) {
            animate(5);
        } else {
            removeAll();
            setUpGame();
            animate(5);
        }
    }

    public Bitmap loadScaledBitmap(int id, int factor) {
        Bitmap img = BitmapFactory.decodeResource(getResources(), id);
        img = Bitmap.createScaledBitmap(
                img, img.getWidth()/factor,
                img.getHeight()/factor,
                true
        );
        return img;
    }

    public void generateFood() {
        RandomGenerator randy = RandomGenerator.getInstance();

        int upperLimit = Math.round(getWidth()/head.getWidth()) - 1;
        float x = 10 + gridSquareWidth * randy.nextInt(1, upperLimit - 1);
        float y = 10 + gridSquareWidth * randy.nextInt(1, upperLimit - 1);
        food.setLocation(x, y);
    }

    @Override
    public void onAnimateTick() {
        super.onAnimateTick();
        score.sendToFront();

        if (head.getX() >= getWidth() - 40 || head.getX() <= 10 || head.getY() <= 0 || head.getY() >= getHeight() - 10) {
            score.setFontSize(80f);
            animationPause();
            gameOver = true;
        }


        float initialX = head.getX();
        float initialY = head.getY();


        if (direction == 0) {
            head.setY(head.getY() - gridSquareWidth);
        }
        if (direction == 1) {
            head.setX(head.getX() + gridSquareWidth);
        }
        if (direction == 2) {
            head.setY(head.getY() + gridSquareWidth);
        }
        if (direction == 3) {
            head.setX(head.getX() - gridSquareWidth);
        }


        if (snake.size()>1) {
           for (int i = snake.size() - 1; i > 0; i--) {
               if (snake.get(i).collidesWith(head)) {
                   animationPause();
                   gameOver = true;
               }
                snake.get(i).setLocation(snake.get(i-1).getX(), snake.get(i-1).getY());
            }
        }

        snake.get(0).setLocation(initialX, initialY);

        if (foodEaten()) {
            generateFood();
            addToTail();
            points++;
            score.setText("Score: " + points);
        }

    }

    private boolean foodEaten() {
        if(food.collidesWith(head)) return true;
        for (int i = snake.size() - 1; i > 0; i--) {
            if (snake.get(i).collidesWith(food)) {
                return true;
            }
        }
        return false;
    }

    public void startGame() {}


    public void turnLeft() {
        direction = direction > 0 ? direction - 1 : 3;
    }

    public void turnRight() {
        direction = direction < 3 ? direction + 1 : 0;
    }

    public void addToTail() {
        GSprite newTail = new GSprite(loadScaledBitmap(R.drawable.snakebody, 4));
        add(newTail, snake.get(snake.size() - 1).getX(), snake.get(snake.size() - 1).getY());
        snake.add(newTail);
    }
}
