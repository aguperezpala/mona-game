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
    private boolean alive;
    private boolean active;
    private int x,y;


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
    }
    public void paint (Graphics g)
    {
        if (this.alive){
            //vamos a graficar un circulo y despues el sprite
            g.setColor(this.color);
            g.fillArc(x, y, this.diametro, this.diametro, 0, 360);
            //seteamos la orientacion de la flecha
            this.sprite.setTransform(this.orientation);
            //seteamos la posicion del sprite
            this.sprite.setRefPixelPosition(this.x+(this.diametro/2), this.y+(this.diametro/2));
            this.sprite.paint(g);
        }
        
        

    }

}
