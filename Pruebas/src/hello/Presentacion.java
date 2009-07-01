/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author agustin
 */
public class Presentacion extends Canvas implements Runnable {
    private Image backImg = null;
    private int imgPos[] = {0,0};
    private int strPos[] = {0,0};
    private int showTime = 10000;    /* milisec */
    private String str = "PRESENTA";

    public Presentacion (String back)
    {
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
         try {
             float factor;
             this.backImg = Image.createImage(getClass().getResourceAsStream(back));
             factor = Resizer.getFactor(this.getWidth(), this.backImg.getWidth(), 80);
             this.backImg = Resizer.resizeImage(this.backImg, factor);
             System.gc();
             } catch (Exception e){System.out.print("Error cargando "+back+"\n");}
         
         this.imgPos[1] = this.getHeight()/2 - this.backImg.getHeight()/2;
         this.imgPos[0] = (this.getWidth() - this.backImg.getWidth())/2;
         this.strPos[1] = this.imgPos[1] + this.backImg.getHeight() + font.getBaselinePosition() + 2;
         this.strPos[0] = (this.getWidth() - font.stringWidth(str)) / 2;

    }

    public void run ()
    {
        while (showTime >= 0) {
            this.showTime-=5;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {System.out.println(e.toString());}
        }
        this.backImg = null;
        System.gc();
    }

    protected void paint(Graphics g) 
    {
        g.setColor(0);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (this.backImg != null)
            g.drawImage(backImg, this.imgPos[0], this.imgPos[1], Graphics.TOP|Graphics.LEFT);

        g.setColor(0x00FFFFFF);
        g.drawString(str, this.strPos[0], this.strPos[1], Graphics.TOP|Graphics.LEFT);
        
    }

}
