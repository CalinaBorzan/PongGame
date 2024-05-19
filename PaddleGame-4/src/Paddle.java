import java.awt.*;
import java.awt.event.KeyEvent;

// The Paddle class represents a paddle in the game. It extends the Rectangle class from AWT for easy representation and collision detection.
public class Paddle extends Rectangle {

    int id;  // Identifier for the paddle (e.g., player 1 or player 2).
    int yVelocity;  // The vertical velocity of the paddle. Determines how fast the paddle moves.
    int speed = 10;  // The speed of the paddle, indicating how many pixels it moves per action.

    // Constructor for creating a Paddle object. It sets the paddle's position, size, and identifier.
    Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id){
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);  // Calls the constructor of the superclass (Rectangle) to set the paddle's position and size.
        this.id = id;  // Sets the identifier for the paddle.
    }

    // Method to handle key press events. This determines how the paddle moves based on user input.
    public void keyPressed(KeyEvent e) {
        switch(id) {
            case 1:  // Controls for player 1's paddle.
                if(e.getKeyCode() == KeyEvent.VK_W) {
                    setYDirection(-speed);  // Move up
                }
                if(e.getKeyCode() == KeyEvent.VK_S) {
                    setYDirection(speed);  // Move down
                }
                break;
            case 2:  // Controls for player 2's paddle.
                if(e.getKeyCode() == KeyEvent.VK_UP) {
                    setYDirection(-speed);  // Move up
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    setYDirection(speed);  // Move down
                }
                break;
        }
    }

    // Method to handle key release events. This stops the paddle when the keys are released.
    public void keyReleased(KeyEvent e) {
        switch(id) {
            case 1:  // Player 1's paddle controls.
                if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) {
                    setYDirection(0);  // Stop moving
                }
                break;
            case 2:  // Player 2's paddle controls.
                if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    setYDirection(0);  // Stop moving
                }
                break;
        }
    }

    // Sets the vertical direction (velocity) of the paddle.
    public void setYDirection(int yDirection) {
        yVelocity = yDirection;  // Updates the paddle's vertical velocity.
    }

    // Updates the paddle's position based on its velocity.
    public void move() {
        y += yVelocity;  // Changes the paddle's vertical position based on its velocity.
    }

    // Renders the paddle on the screen.
    public void draw(Graphics g) {
        if(id == 1)
            g.setColor(Color.GREEN);  // Set color for player 1's paddle.
        else
            g.setColor(Color.red);  // Set color for player 2's paddle.
        g.fillRect(x, y, width, height);  // Draws the paddle as a filled rectangle.
    }
}
