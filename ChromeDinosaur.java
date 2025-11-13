import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener{

    int boardWidth = 1500;
    int boardHeight = 500;

    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;

    class Block {
        
        int x;
        int y;
        int width;
        int height;
        Image img;

        Timer gameLoop;

        Block(int x, int y, int width, int height, Image img) {

            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    //dinosaur
    int dinosaurWidth = 176;
    int dinosaurHeight = 198;
    int dinosaurX = 50;
    int dinosaurY = boardHeight - dinosaurHeight;
    Block dinosaur;

    //cactus
    int cactus1Width = 68;
    int cactus2Width = 138;
    int cactus3Width = 204;
    int cactusHeight = 140;
    int cactusX = 1400;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactiArray;
    
    //game loop
    Timer gameLoop;
    Timer placeCactusTimer;
    boolean gameOver = false;
    int score = 0;

    //physics
    int velocityY = 0;
    int gravity = 2;
    int cactusVelocityX = -12;


    public ChromeDinosaur() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        setBackground(Color.lightGray);
        addKeyListener(this);

        dinosaurImg = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();

        //dinosaur
        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);
        //cactus
        cactiArray = new ArrayList<Block>();

        //game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        placeCactusTimer.start();
    
    }

    void placeCactus() {
        double placeCactusChance = Math.random();

        if (gameOver) {
            return;
        }
        if (placeCactusChance > 0.90) {
            Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img);
            cactiArray.add(cactus);
        }
        else if (placeCactusChance > 0.70) {
            Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img);
            cactiArray.add(cactus);
        }
        else if (placeCactusChance > 0.50) {
            Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img);
            cactiArray.add(cactus);
        }

        if (cactiArray.size() > 10) {
            cactiArray.remove(0);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);

        for (Block cactus : cactiArray) {
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 64));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), 20, 70);
        }
        else {
            g.drawString("Score: " + String.valueOf(score), 20, 70);
        }
    }

    public void move() {
        //dinosaur
        velocityY += gravity;
        dinosaur.y += velocityY;

        if (dinosaur.y > dinosaurY) {
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        for (Block cactus : cactiArray) {
            cactus.x += cactusVelocityX;

            if (collision(dinosaur, cactus)) {
                dinosaur.img = dinosaurDeadImg;
                gameOver = true;
            }
        }

        //score
        score++;
    }

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
            a.x + a.width > b.x &&
            a.y < b.y + b.height &&
            a.y + a.height > b.y;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeCactusTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (dinosaur.y == dinosaurY) {
                velocityY = -34;
                dinosaur.img = dinosaurJumpImg;
            }

            if (gameOver) {
                dinosaur.y = dinosaurY;
                dinosaur.img = dinosaurImg;
                velocityY = 0;
                score = 0;
                cactiArray.clear();
                gameOver = false;
                gameLoop.start();
                placeCactusTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
