import java.awt.*;
import java.util.Random;  // Importing the Random class for generating random numbers.

public class Ball extends Rectangle {

    Random random;  // Random object for generating random directions.
    int xVelocity;  // Horizontal velocity of the ball.
    int yVelocity;  // Vertical velocity of the ball.
    int initialSpeed = 2;  // Initial speed of the ball.

    // Constructor for Ball. It sets up the ball's initial position and movement.
    Ball(int x, int y, int width, int height) {
        super(x, y, width, height);  // Calling the Rectangle class constructor to set ball's position and size.
        random = new Random();  // Initializing the Random object.

        // Generating a random initial horizontal direction.
        int randomXDirection = random.nextInt(2); // Randomly picks 0 or 1.
        if (randomXDirection == 0)
            randomXDirection--; // Changes 0 to -1 to allow movement in the opposite direction.
        // Setting the initial horizontal direction and speed.
        setXDirection(randomXDirection * initialSpeed);

        // Generating a random initial vertical direction.
        int randomYDirection = random.nextInt(2); // Randomly picks 0 or 1.
        if (randomYDirection == 0)
            randomYDirection--; // Changes 0 to -1 for movement in the opposite direction.

        // Setting the initial vertical direction and speed.
        setYDirection(randomYDirection * initialSpeed);
    }

    // Setter method for the horizontal velocity.
    public void setXDirection(int randomXDirection) {
        xVelocity = randomXDirection;
    }

    // Setter method for the vertical velocity.
    public void setYDirection(int randomYDirection) {
        yVelocity = randomYDirection;
    }

    // Method to update the ball's position based on its velocity.
    public void move() {
        x += xVelocity; // Updates the horizontal position.
        y += yVelocity; // Updates the vertical position.
    }

    // Method to draw the ball on the screen.
    public void draw(Graphics g) {
        g.setColor(Color.white); // Setting the color for the ball.
        g.fillOval(x, y, height, width); // Drawing the ball as an oval using its position and size.
    }
}