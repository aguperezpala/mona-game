/*
 * 
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
    private int timeToShow = 600;
    private boolean alive = false;
    Thread tcartel;


    //
    
    public Cartel (String nomImg, int px, int py, int scale)
    {
        this.sprite = Resizer.spriteResized(nomImg, px, py, scale, true);
        this.sprite.setVisible(false);
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
             this.sprite.setVisible(false);
            this.alive = false;
        }
    }
    public void setTimeToShow(int v)
    {
        this.timeToShow = v;
    }
    public void setPos(int x, int y)
    {
        this.sprite.setRefPixelPosition(x, y);
    }

    public void show ()
    {
        this.setAlive(true);
    }
    public void hide ()
    {
        this.setAlive(false);
    }
    public Sprite getSprite()
    {
        return this.sprite;
    }

    public void run ()
    {
        try {
            Thread.sleep(this.timeToShow);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //ahora hacemos que no se muestre mas
        this.sprite.setVisible(false);
        this.alive = false;
    }
    

}
