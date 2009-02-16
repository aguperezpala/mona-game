/*
 * Podriamos delimitar zonas en donde no se tiene que pintar, por el momento
 * vamos hacer que se impriman primero los efectos y despues el personaje, para
 * evitar "solapamientos".
 */

package hello;


import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import java.util.Random;
/**
 *
 * @author agustin
 */
public class Efecto{
    
    private Sprite sprite;
    private Image image;
    private boolean alive = true;
    private boolean withTransformation = true;
    private int numOfObjects = 1;
    private int screenWidth;
    private int screenHeight;
    private Random rand;
    
    Graphics g;

   
    /*Constructor:
     * Requires:
     *          nomImg = nombre de la imagen a cargar
     *          px     = ancho de la pantalla
     *          py     = alto de la pantalla
     *          scale  = porcentaje que ocupa la imagen en la pantalla
     *          g      = donde va a ser renderizada la imagen
     */
    public Efecto (String nomImg, int px, int py, int scale, Graphics g)
    {
        this.g = g;
        this.screenHeight = py;
        this.screenWidth = px;
        this.rand = new Random();

        try {
            //cargamos la imagen
            image = Image.createImage(getClass().getResourceAsStream(nomImg));
            float factor = Resizer.getFactor(py, image.getHeight(), scale);

            this.image = Resizer.resizeImage(image, (int) (image.getWidth() * factor),
                    (int) (image.getHeight() * factor));
            this.sprite = new Sprite(image);
                
            //seteamos el pixel de referencia justo en el medio de la imagen
            this.sprite.defineReferencePixel(image.getWidth() / 2, image.getHeight() / 2);
            
            
        } catch (Exception e){System.out.print("Error creando sprite "+nomImg+"\n");}
       

    }

    public void setAlive (boolean b)
    {
        this.alive = b;
    }
    public boolean isAlive ()
    {
        return this.alive;
    }
    public void setNumberOfObjects (int n)
    {
        this.numOfObjects = n;
    }
    public void activeTransformations(boolean b)
    {
        this.withTransformation = b;
    }

    public void paint ()
    {
        if (this.alive) {
            for (int i = 0; i < this.numOfObjects; i++){
                //chequeamos si soporta transofrmacion
                if (this.withTransformation) {
                    this.sprite.setTransform(rand.nextInt(8));
                }
                //posicionamos dentro de la pantalla
                this.sprite.setPosition(this.rand.nextInt(this.screenWidth),
                        this.rand.nextInt(this.screenHeight));
                //dibujamos ahora en cualquier lugar
                this.sprite.paint(g);

            }
        }
    }

   
}
