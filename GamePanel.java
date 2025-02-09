import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;

//array to store the coordinates of the body parts of the snake including head
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
//initial amount of body parts for the snake
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    JButton replayBtn;
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        //create replay btn
        replayBtn = new JButton("Replay");
        replayBtn.setFocusable(false);
        replayBtn.setBounds((SCREEN_WIDTH - 100) / 2, SCREEN_HEIGHT / 2 + 50, 100, 40);
        replayBtn.setVisible(false); // Hide initially
        replayBtn.addActionListener(e -> restartGame());

        this.setLayout(null);
        this.add(replayBtn);

        startGame();
    }
    public void startGame() {
        newApple();//create new apple on screen
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if(running) {

            // for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
            //    g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);//for vertical lines in the panel
            //    g.drawLine(0, i*UNIT_SIZE,SCREEN_WIDTH , i*UNIT_SIZE);//for horizontal lines
            // }
            //to draw the apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
   
            //to color body of the snake
            for(int i = 0;i<bodyParts;i++) {
               //head
               if(i == 0) {
                   g.setColor(Color.green);
                   g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
               } 
               //body
               else { 
                   g.setColor(new Color(45, 180, 0));
                 //g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                   g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
               }
            }
        g.setColor(Color.red);
        g.setFont(new Font("Arial Rounded MT Bold",Font.PLAIN, 40));
        FontMetrics  metrics = getFontMetrics(g.getFont());
        g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:" + applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    public void newApple() {
        //code to place the apple within a cell

         // Generates a random x-coordinate for the apple
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
         // Generates a random y-coordinate for the apple
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move() {
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple() {
        //checks if the head touches the apple
        if((x[0] == appleX && y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions() {
        //checks if head collides with body
        for(int i=bodyParts;i>0;i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //check if head touches left border
        if(x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running) {
            timer.stop();
        }
        if (!running) {
            timer.stop();
            gameOver(getGraphics()); // Call gameOver to show the button
        }
        
    }
    public void gameOver(Graphics g) {
        //score
        g.setColor(Color.red);
        g.setFont(new Font("Arial Rounded MT Bold",Font.PLAIN, 40));
        FontMetrics  metrics = getFontMetrics(g.getFont());
        g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:" + applesEaten))/2, g.getFont().getSize());
        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("Arial Rounded MT Bold",Font.PLAIN, 75));
        //to center text
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        //To show the replay btn
        replayBtn.setVisible(true);
    }
    public void restartGame() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        running = true;
    
        // Reset snake position
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
    
        // Create a new apple
        newApple();
    
        // Hide the replay button
        replayBtn.setVisible(false);
    
        // Restart the timer
        timer.start();
    
        repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        } 
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                //to limit the movement to 90 
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
