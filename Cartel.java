/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;


import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author agustin
 */
public class Cartel implements Runnable {
    
    
    private Sprite sprite;
    private int velocity;
    private boolean alive = true;
    Thread tcartel;


    //
    
    public Cartel (String nomImg, int px, int py, int scale)
    {
        this.sprite = Resizer.spriteResized(nomImg, px, py, scale);
    }
    
    
    public boolean isAlive ()
    {
        return this.alive;
    }
    public void setAlive (boolean b)
    {
         if (b == true) {
            if (!this.alive) {
                this.alive = true;
                this.sprite.setVisible(true);
                this.tcartel=new Thread(this);
                tcartel.start();  //chequear esto
            }             
        } else {            
            this.alive = false;
        }
    }
    public void setVelocity(int v)
    {
        this.velocity = v;
    }
    public void setPos(int x, int y)
    {
        this.sprite.setPosition(x, y);
    }

    public void run ()
    {
        try {
            Thread.sleep(this.velocity);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //ahora hacemos que no se muestre mas
        this.sprite.setVisible(false);
        this.alive = false;
    }
    

}
