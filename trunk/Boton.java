/* En esta clase vamos a generar el circulo y vamos a almacenar el puntero al
 * sprite, y la posicion a donde apunta la flecha.
 * Tener en cuenta que la posicion de estos botones estan referenciadas en la
 * punta superior izquierda.
 *
 */

package hello;


import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author agustin
 */
public class Boton {

    private int diametro;
    private int color;          //Color RGB
    private int orientation;
    private Sprite sprite;
    private boolean alive = true;   //si debe dibujarse en pantalla o no
    private boolean active;         //Fue apretado o no?
    private int x,y;
    private int sX,sY;              //sprite X e Y coords


    /* Constructor
     * Requires:
     *          s     = puntero a sprite que va a ser renderizado en el medio
     *          py    = alto de pantalla
     *          scale = scala del boton en proporcion a la pantalla
     */

    public Boton (Sprite s, int py, int scale, int orientation)
    {
        this.sprite = s;
        //calculamos ahora el diametro del circulo
        this.diametro = (scale * py)/100;
        this.orientation = orientation;
        //this.sprite.defineReferencePixel(0, 0);
        
        
    }
    public void setOrientation (int o)
    {
        this.orientation = o;
    }

    public int getDiametro ()
    {
        return this.diametro;
    }
    public void setAlive (boolean b)
    {
        this.alive = b;
    }
    public boolean isAlive ()
    {
        return this.alive;
    }
    public void setActive (boolean b)
    {
        this.active = b;
    }
    public boolean isActive ()
    {
        return this.alive;
    }
    public void setColor (int rgb)
    {
        this.color = rgb;
    }
    public void setPosition (int x, int y)
    {
        this.x = x;
        this.y = y;
        this.sX = this.x + this.diametro/2;
        this.sY = this.y + this.diametro/2;
    }
    
     public int getRedColor ()
     {
         int r;
         r=(this.color & 0x00FF0000)>>16;
         return r;
     }
     public int getGreenColor ()
     {
         int r;
         r=(this.color & 0x0000FF00)>> 8;
         return r;
     }
     public int getBlueColor ()
     {
         int r;
         r=(this.color & 0x000000FF);
         return r;
     }
    
    public void paint (Graphics g)
    {
        //vamos a graficar un circulo y despues el sprite
        g.setColor(this.color);
        g.fillArc(x, y, this.diametro, this.diametro, 0, 360);
        //seteamos la orientacion de la flecha
        this.sprite.setTransform(this.orientation);
        //seteamos la posicion del sprite
        this.sprite.setRefPixelPosition(this.sX, this.sY);
        
        this.sprite.paint(g);
        
    }

}
