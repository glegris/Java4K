package java4k.i4kopter;

/*
 * i4kopter
 * Copyright (C) 2009 Bjarne Holen
 *
 * This file is part of i4kopter.
 *
 * i4kopter is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * F-Zero 4K is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import java.util.Random;
import java.applet.Applet;

/**
 * A simple icopter clone.
 *
 * @author bjarneh@ifi.uio.no
 */

public class I4Kopter extends Applet {

    private BufferedImage bufferImage;
    private BufferedImage backgroundImage;
    private BufferedImage helicopterImage1, helicopterImage2;
    private int w = 800, h = 400;                   // height and width of screen
    private Random random = new Random();          
    private boolean keyPressed=false, paused=false;
    private int heliY, heliX;                       // helicopter position X,Y
    private double speed = 0.0, gravity = 1.6;      
    private Point[] obstacles;                      // use Point's to store obstacle offset
    private int pathHeight = 250;                   // height of 'rockless path'
    private int backgroundLength = 17*this.w;       // number of screen lengths
    private int widthBar = 20;                      // rock-bar-width
    private int oheight  = 59;                      // height of obstacle
    private long lasted  = 0;                       // use time as score

    public I4Kopter(){
        super();
    }

    public void init(){
        this.setSize(this.w, this.h);
        this.enableEvents(AWTEvent.KEY_EVENT_MASK);
        this.setVisible(true);
    }

    public void start(){

        this.heliY = (int) this.h/2;
        this.heliX = (int) this.w/5;

        bufferImage = new BufferedImage(this.w, this.h, BufferedImage.TYPE_INT_RGB);
        backgroundImage = new BufferedImage(backgroundLength, this.h, BufferedImage.TYPE_INT_RGB);

        /* ******* start draw helicopters ********* */

        helicopterImage1 = new BufferedImage(50, 25, BufferedImage.TYPE_INT_ARGB); // alpha value
        Graphics2D helicopterBuffer1 = helicopterImage1.createGraphics();

        helicopterBuffer1.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        helicopterBuffer1.setColor(new Color(0,0,0,0.0f)); // make it transparent
        helicopterBuffer1.fillRect(0,0, 50, 25);

        helicopterBuffer1.setColor(Color.WHITE);
        helicopterBuffer1.fillOval(16,10, 21, 12);  // helicopter body
        helicopterBuffer1.fillPolygon(new int[] {2,22,17}, new int[] {10,15,19}, 3); // stabiliser body
        helicopterBuffer1.setColor(new Color(0,0,1,0.55f));
        helicopterBuffer1.fillOval(9,4, 35, 10);    // main rotor  
        helicopterBuffer1.fillOval(0,7, 7,7);       // stabiliser rotor
        helicopterBuffer1.setColor(Color.GREEN);
        helicopterBuffer1.drawLine(22,23,33,23);    // substructure - railing

        helicopterImage2 = new BufferedImage(52, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D helicopterBuffer2 = helicopterImage2.createGraphics();

        AffineTransform transform = helicopterBuffer2.getTransform();
        // we use helicopterImage1 as original and just flip it 12 degrees
        transform.rotate(Math.toRadians(-12), 26, 14);
        helicopterBuffer2.setTransform(transform);
        helicopterBuffer2.drawImage(helicopterImage1, 2, 2, null);
        helicopterBuffer2.setTransform(helicopterBuffer1.getTransform());
    
        /* ******* end draw helicopters ********* */


        /* ******* start draw background ********* */

        Graphics2D backgroundBuffer = backgroundImage.createGraphics();

        backgroundBuffer.setColor(Color.YELLOW);
        backgroundBuffer.fillRect(0,0, backgroundLength, this.h);

        boolean up = false;
        int currentY = 80;
        int ocounter = 0;
        obstacles = new Point[ backgroundLength / widthBar ];

        // dummy obstacles
        for(int i =0; i < this.w; i += widthBar){
            obstacles[ocounter++] = new Point(-1,-1);
        }

        // welcome screen
        BufferedImage heading = new BufferedImage(100,50, BufferedImage.TYPE_INT_RGB);
        Graphics2D headingG = heading.createGraphics();
        headingG.setColor(Color.BLACK);
        headingG.fillRect(0,0, 100,50);
        headingG.setColor(Color.WHITE);
        headingG.drawString("I4KOPTER",32,20);
        backgroundBuffer.drawImage(heading,0,0,this.w,this.h,null);
        backgroundBuffer.setColor(Color.BLACK);
        

        for(int i = this.w; i < (backgroundLength - (2*this.w)); i += widthBar){

            currentY += (up)? -10 : 10;

            backgroundBuffer.fillRect(i, currentY, widthBar, pathHeight);
            
            up = random.nextBoolean();

            if(currentY > 110){ up = true; }
            if(currentY < 40 ){ up = false; }

            obstacles[ocounter++] = new Point(currentY, -1);

            // add obstacle every 17 bar
            if(ocounter % 17 == 0){
                int middleOffset = random.nextInt(pathHeight - currentY) + currentY;
                backgroundBuffer.setColor(Color.YELLOW);
                backgroundBuffer.fillRect(i,
                                          middleOffset,
                                          widthBar,
                                          oheight);
                backgroundBuffer.setColor(Color.BLACK);
                obstacles[ocounter - 1].y = middleOffset;
            }
        }

        // dummy obstacles
        for(int i = (backgroundLength - (2*this.w)); i < backgroundLength; i += widthBar){
            obstacles[ocounter++] = new Point(-1,-1);
        }

        // the end screen..
        heading = new BufferedImage(200, 50, BufferedImage.TYPE_INT_RGB);
        headingG = heading.createGraphics();
        headingG.setColor(Color.BLACK);
        headingG.fillRect(0,0, 200,50);
        headingG.setColor(Color.WHITE);
        headingG.drawString("The end",75,20);
        backgroundBuffer.drawImage(heading, backgroundLength - (2*this.w),0,2*this.w,this.h, null);

        /* ******* end draw background ********* */

        // outer loop
        while(true){
            speed = 0.0;
            gravity = 1.6;
            heliY = this.h/2;
            lasted = 0;
            loop();
            Graphics g =  this.getGraphics();
            g.setColor(Color.YELLOW);
            g.drawString(" you lasted: "+(long)lasted/1000+" seconds ", heliX+200, 200);
            try{ Thread.sleep(1400); }catch(Exception e){}
        }
    }

    boolean collides(int bufferOffset, BufferedImage image){

        // calculate which obstacle we can hit

        int i = (int) (bufferOffset+heliX)/widthBar + 1;

        // obstacle is dummy
        if(obstacles[i].x == -1 || obstacles[i+1].x == -1){ return false; }

        // obstacle[?].x is offset of top rock, so if we are
        // below this value, we crash into rock at top of screen
        if(heliY < obstacles[i].x || heliY < obstacles[i+1].x)
        { return true ;}

        int imageHeight = image.getHeight();
        int imageWidth  = image.getWidth();

        // we crash into bottom rock if we are beyond this threshold
        if(heliY + imageHeight > obstacles[i].x + pathHeight ||
           heliY + imageHeight > obstacles[i+1].x + pathHeight)
        { return true; }

        // we can also crash with middle rock if our picture intersects with
        // middle obstacle, and it exists naturally :-)
        if(obstacles[i].y != -1 || obstacles[i+1].y != -1){
            Rectangle copter = new Rectangle(heliX, heliY, imageWidth, imageHeight);
            Rectangle oRectangle;

            if(obstacles[i].y != -1){
                oRectangle = new Rectangle(heliX, obstacles[i].y, widthBar, oheight);  
                if(copter.intersects(oRectangle)){ return true; }
            }

            if(obstacles[i+1].y != -1){
                oRectangle = new Rectangle(heliX, obstacles[i+1].y, widthBar, oheight);  
                if(copter.intersects(oRectangle)){ return true; }
            }
        }

        return false;
    }

    void updateHeliY(){
        if(keyPressed){
            speed -= gravity;
        }else{
            speed += (gravity*1.2);
        }
        heliY += speed;
    }

    void loop(){

        BufferedImage tmp;
        Graphics frameGraphics, bufferGraphics;
        int dx = 12, offset = 0, levelLength = this.w*2;
        int maxLen = backgroundLength - this.w;

        
        while(offset < maxLen){

            while(offset <= levelLength){

                if(! paused){

                    bufferGraphics = bufferImage.getGraphics();
                    bufferGraphics.drawImage(backgroundImage,0,0,this.w,this.h,
                                             offset, 0, (offset + this.w),this.h, null);

                    updateHeliY(); // pull from gravitation vs. chopper

                    bufferGraphics.setClip(heliX, heliY, 57, 57); // enough place for chopper

                    tmp = (keyPressed)? helicopterImage2 : helicopterImage1; //(keyPressed)?up:down

                    if(collides(offset, tmp)){ // we have collided draw explosion

                        int oldOffset, newOffset;

                        for(int i = 1; i < 1000; i++){
                            oldOffset = (int) ((i-1)*2)/2;
                            newOffset = (int) (i*2)/2;
                            frameGraphics = this.getGraphics();
                            frameGraphics.setColor(Color.YELLOW);
                            frameGraphics.drawOval(heliX - newOffset, heliY - newOffset, i*2, i*2);
                            frameGraphics.setColor(Color.BLACK);
                            frameGraphics.fillOval(heliX - oldOffset, heliY - oldOffset, (i-1)*2, (i-1)*2);
                            frameGraphics.dispose();
                        }

                        offset = maxLen*2; // will force game to end..

                        continue;  // jump to end of loop

                    }else{

                        bufferGraphics.drawImage(tmp, heliX, heliY, null);// draw one of our choppers

                    }
                    
                    frameGraphics = this.getGraphics();
                    frameGraphics.drawImage(bufferImage,0,0,this.w,this.h, this);

                    //flip it
                    frameGraphics.dispose();

                    if(offset == 0){ paused = true;}

                    offset += dx;
                    lasted += 49; // well...
                }
                try{
                    Thread.sleep(40);
                }catch(Exception ex){}
                
            }
            // increase level
            dx++;
            gravity += .17;
            levelLength += 2*this.w;
        }

    }

    public void processKeyEvent(KeyEvent k){

        int keyID = k.getID();

        if(keyID == KeyEvent.KEY_PRESSED){
            keyPressed = true;
            paused=false;
        }else if(keyID == KeyEvent.KEY_RELEASED){
            keyPressed=false;
        }

        // space will pause the game
        if(k.getKeyChar() == ' '){
            paused = true;
        }
    }
}