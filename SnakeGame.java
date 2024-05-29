import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener,KeyListener{

    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x=x;
            this.y=y;
        }
    }
    private final int width;
    private final int height;
    int tileSize = 25;

    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    
    
    //Food
    Tile food;
    Random random;

    // game logic 
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;


    public SnakeGame(final int width ,final int height){
        super();
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width,height));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 1;

        gameLoop = new Timer(100,this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //Grid
        // for(int i=0;i<width/tileSize;i++){
        //     // x1,y1,x2,y2 
        //     g.drawLine(i*tileSize, 0 , i*tileSize , height);
        //     g.drawLine(0,i*tileSize, width , i*tileSize);

        // }
        //Food 
        g.setColor(Color.red);
        // g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize,true);

        // SnakeHead
        g.setColor(Color.green);
        // g.fillRect(snakeHead.x * tileSize,snakeHead.y * tileSize,tileSize,tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize,true);

        //snake Body
        for(int i=0;i< snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x * tileSize , snakePart.y * tileSize , tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize , snakePart.y * tileSize , tileSize, tileSize,true);

        }
        //score
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over : "+ String.valueOf(snakeBody.size()),tileSize-16 , tileSize);

        }
        else{
            g.drawString("Score : "+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
    }
    public void placeFood(){
        food.x = random.nextInt(width/tileSize);
        food.y = random.nextInt(height/tileSize);
    }

    public boolean collision(Tile tile1 , Tile  tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){

        //eat food 
        if(collision(snakeHead,food)){
            snakeBody.add(new Tile(food.x , food.y));
            placeFood();
        }

        // snake body
        for(int i=snakeBody.size()-1 ; i>=0;i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;

            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }


        //snake food
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game Over
        for(int i=0;i<snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);

            if(collision(snakeHead,snakePart)){
                gameOver = true;
            }
        }
        if(snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > width 
        || snakeHead.y * tileSize<0 || snakeHead.y*tileSize>height){
            gameOver=true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }
    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY!=1){
            velocityX=0;
            velocityY=-1;

        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1){
            velocityX=0;
            velocityY=1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
            velocityX=-1;
            velocityY=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }
    }
    @Override
    public void keyTyped(KeyEvent e){
        
    }
    @Override
    public void keyReleased(KeyEvent e){
        
    }
    // public void startGame(){
    //     setFocusable(true);
    //     setFocusTraversalKeysEnabled(false);
    //     requestFocusInWindow();
    //     addKeyListner(new KeyAdapter(){
    //         @Override
    //         public void KeyPressed(final KeyEvent e){
    //             if(e.getKeyCode() == KeyEvent.VK_SPACE){
    //                 System.out.println("SPACEBAR is pressed");
    //             }
    //         }
    //     });
    //     repaint();
    // }


    // @Override
    // protected void paintComponent(Graphics graphics){
    //     super.paintComponent(graphics);
    //     graphics.setColor(Color.WHITE);
    //     graphics.setFont(graphics.getFont().deriveFont(30F));
    //     int currentHeight = height/4;
    //     final var graphics2G = (Graphics2D) graphics;
    //     final var frc = graphics2G.getFontRenderContext();
    //     final String message = "Press SPACEBAR to Begin the Game";
    //     for(final var line : message.split("\n")){
    //         final var layout = new TextLayout(line, graphics.getFont(),frc);
    //         final var bounds = layout.getBounds();
    //         final var targetWidth = (float)(width - bounds.getWidth())/2;
    //         layout.draw(graphics2D, targetWidth, currentHeight);
    //         currentHeight+= graphics.getFontMetrics().getHeight();
    //     }
    //     // graphics.drawString("My string",400,300);
    // }


}