/*
 * Podriamos delimitar zonas en donde no se tiene que pintar, por el momento
 * vamos hacer que se impriman primero los efectos y despues el personaje, para
 * evitar "solapamientos".
 */

package hello;


import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import java.util.Random;
import java.util.Vector;
/**
 *
 * @author agustin
 */
public class Efecto implements Runnable{
    
    private Sprite sprite;
    private boolean alive = true;
    private boolean withTransformation = true;
    private int screenWidth;
    private int screenHeight;
    private Random rand;
    private int velocity = 300;
    private int sleepTime = 600;
    Thread tefecto;
    private Vector elems;
    
    //Graphics g;

   
    /*Constructor:
     * Requires:
     *          nomImg = nombre de la imagen a cargar
     *          px     = ancho de la pantalla
     *          py     = alto de la pantalla
     *          scale  = porcentaje que ocupa la imagen en la pantalla
     *          g      = donde va a ser renderizada la imagen
     */
    public Efecto (String nomImg, int px, int py, int scale)
    {
      //  this.g = g;
        Image image;
        this.screenHeight = py;
        this.screenWidth = px;
        this.rand = new Random();
        elems = new Vector();

        try {
            //cargamos la imagen
            image = Image.createImage(getClass().getResourceAsStream(nomImg));
            float factor = Resizer.getFactor(py, image.getHeight(), scale);

            //aca se puede usar Resizer.resizeImage o resizeBitmap
            image = Resizer.resizeBitmap(image, (int) (image.getWidth() * factor),
                    (int) (image.getHeight() * factor));
            this.sprite = new Sprite(image);
                
            //seteamos el pixel de referencia justo en el medio de la imagen
            this.sprite.defineReferencePixel(image.getWidth() / 2, image.getHeight() / 2);
            this.tefecto=new Thread(this);
            this.tefecto.start();
            this.setNumberOfObjects(5);
            
        } catch (Exception e){System.out.print("Error creando sprite "+nomImg+"\n");}
       

    }

    public void setAlive (boolean b)
    {
       if (b == true) {
            if (!this.alive) {
                this.alive = true;
                this.tefecto=new Thread(this);
                tefecto.start();  //chequear esto
            }             
        } else {            
            this.alive = false;
        }
    }
    public boolean isAlive ()
    {
        return this.alive;
    }
    public void setNumberOfObjects (int n)
    {
        if (elems.size() < n) {
            for (int i = 0; i < n - elems.size(); i++) {
                Point p = new Point(rand.nextInt(this.screenWidth), rand.nextInt(this.screenHeight));
                elems.addElement(p);
            }
        } else {
            
        }
    }
    public void activeTransformations(boolean b)
    {
        this.withTransformation = b;
    }
    public void setVelocity (int v)
    {
        this.velocity = v;
    }
    public void setSleepTime (int s)
    {
        this.sleepTime = s;
    }
    
    public void run ()
    {
        while (this.alive)
        {
            Point p;
            for (int i = 0; i < elems.size(); i++){
                //seteamos las posiciones de cada uno de los objetos
                p = (Point) elems.elementAt(i);
                p.x = this.rand.nextInt(this.screenWidth);
                p.y = this.rand.nextInt(this.screenHeight);
                if (this.withTransformation)
                    p.rotation = this.rand.nextInt(8);
            }
            
            try {
                Thread.sleep(this.velocity);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.alive = false;
            try {
                Thread.sleep(this.sleepTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.alive = true;
        }
    }

    public void paint (Graphics g)
    {
        if (this.alive) {
            Point p;
            for (int i = 0; i < elems.size(); i++){
                //seteamos las posiciones de cada uno de los objetos
                p = (Point) elems.elementAt(i);
                this.sprite.setPosition(p.x, p.y);
                this.sprite.setTransform(p.rotation);
                this.sprite.paint(g);
            }
        }
    }

   
}

class Point {
    public int x;
    public int y;
    public int rotation;

    Point (int x, int y) {this.x=x;this.y = y;};
}
