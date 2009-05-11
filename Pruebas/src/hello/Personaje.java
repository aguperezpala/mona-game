/*
 * Personaje.
 * ! Faltaria hacer las secuencias de animaciones y la cantidad de animaciones !
 * Funciones:
 *  void set_pos (int x, int y);
 *  int get_xPos ();
 *  int get_yPos ();
 * 
 *  void set_pixRefPos (int x, int y);
 *  void set_pixRef (int x, int y);
 *  Sprite get_sprite(void);
 *  int add_sequence(int s[]);  //retorna el numero de animacion
 *
 *  void set_transformation (int transform);
 *  void set_visible (boolean b);
 *  boolean is_visible();
 *  void set_alive (boolean b);
 *  boolean is_alive();
 *  void set_vel (int v);
 *
 */

package hello;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import java.util.Random;
/**
 *
 * @author agustin
 */
public class Personaje implements Runnable {

    private static int PIXELS_TOLERANCE_FOR_RESIZE = 10;
    private Sprite sprite;
    private boolean alive = true;
    private Thread tpers;
    private int velocity = 70;    
    private Random rand = new Random();
    private static final int NORMAL_ANIM = 0;
    private static final int SALTO_ANIM = 1;
    private static final int HAE_ANIM = 2;
    private static final int MANOS_ARRIBA_ANIM = 3;
    private static final int CUELLO_ANIM = 4;


    private static final int ANIM_COUNT = 5;
    private static final int[] animNormal = {0,1,2,3,4,5,6,7,8};
    private static final int[] animSalto = {9,10,11,12,13,14,15,13,12,13,14,15}; //9 a 15
    private static final int[] animHae = {16,17,18,19};
    private static final int[] animManosArriba = {20,21,22,23,22,21,20};
    private static final int[] animCuello = {24,25,26,25,24};



    
    
    public Personaje (String nomImg, int px, int py,int width, int height, int scale)
    {
        int nx, ny; //numero de cuadros en X y en Y
         Image image;
        try {
            //cargamos la imagen
            image = Image.createImage(getClass().getResourceAsStream(nomImg));
            {
                int ix, iy;
                //calculamos cantidad de cuadros en filas y columnas
                nx = image.getWidth() / width;
                ny = image.getHeight() / height;
                float factor = Resizer.getFactor(py, height, scale);

                ix = ((int)(width * factor))*nx;
                iy = ((int)(height * factor))*ny;
                                
                
                if (Math.abs(ix - image.getWidth()) > PIXELS_TOLERANCE_FOR_RESIZE &&
                        Math.abs(iy - image.getHeight()) > PIXELS_TOLERANCE_FOR_RESIZE){
                    //hacemos un resize porque es mayor
                    image = Resizer.resizeImage(image, ix, iy);
                    
                    this.sprite = new Sprite(image, (int)(width *factor),
                            (int)(height * factor));
                    
                } else {
                    //no hacemos ningun resize
                    this.sprite = new Sprite(image, width,height);
                }
                /* ahora vamos a setear las secuencias de las animaciones */
                this.sprite.setFrameSequence(animNormal);
                this.sprite.setFrameSequence(animSalto);
                this.sprite.setFrameSequence(animHae);
                this.sprite.setFrameSequence(animManosArriba);
                this.sprite.setFrameSequence(animCuello);

        
            //seteamos el pixel de referencia justo en el medio de la imagen
            set_pixRef((int)(width *factor) / 2, (int)(height * factor) / 2);
            this.tpers=new Thread(this);
            this.tpers.start();
            }
        } catch (Exception e){System.out.print("Error creando sprite "+nomImg+"\n");}
       

    }

    public void set_alive (boolean b)
    {
        if (b == true) {
            if (!this.alive) {
                this.alive = true;
                this.tpers=new Thread(this);
                tpers.start();  //chequear esto
            }             
        } else {            
            this.alive = false;
        }
    }
    public boolean is_alive ()
    {
        return this.alive;
    }

    public void set_visible (boolean b)
    {
        this.sprite.setVisible(b);
    }
    public boolean is_visible ()
    {
        return this.sprite.isVisible();
    }

    public Sprite getSprite()
    {
        return this.sprite;
    }
    
    public void set_pixRefPos (int x, int y)
    {
        this.sprite.setRefPixelPosition(x, y);
    }
    
    public void set_pixRef (int x, int y)
    {
        this.sprite.defineReferencePixel(x, y);
    }

    public void set_pos (int x, int y)
    {
        //this.sprite.setPosition(x, y);
         this.sprite.setRefPixelPosition(x, y);
    }
    
    public int get_xPos ()
    {
        return this.sprite.getX();
    }
    
    public int get_yPos ()
    {
        return this.sprite.getY();
    }

    //para esta funcion usar get_sprite().TIPO_TRANSF
    public void set_transformation (int t)
    {
        this.sprite.setTransform(t);
    }
    public void set_velocity (int v)
    {
        this.velocity = v;
    }
    
    public void setAnim (int animType)
    {
        this.setRandAnim(animType);
    }
    

    public void run () {
        int frame = 0;
        while (this.alive) {
            if (this.sprite.getFrame() == this.sprite.getFrameSequenceLength() - 1) {
                /* debemos actualizar y/o cambiar de secuencia */
                frame = rand.nextInt(this.ANIM_COUNT);
                this.setRandAnim(frame);
            } else {
                this.sprite.nextFrame();
            }

            
            
            try {
                Thread.sleep(this.velocity);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void setRandAnim (int n)
    {
        /* ahora vamos a setear la animacion pero mirando para cualquiera
         * de los posibles lados
         */
        if (this.rand.nextInt(2) == 1) {
            this.sprite.setTransform(Sprite.TRANS_MIRROR);
        } else {
            this.sprite.setTransform(Sprite.TRANS_NONE);
        }
        switch (n) {
            case 0:
                this.sprite.setFrameSequence(animNormal);
                break;
            case 1:
                this.sprite.setFrameSequence(animSalto);
                break;
            case 2:
                this.sprite.setFrameSequence(animHae);
                break;
            case 3:
                this.sprite.setFrameSequence(animManosArriba);
                break;
            case 4:
                this.sprite.setFrameSequence(animCuello);
                break;
            default:
                this.sprite.setFrameSequence(animNormal); 

        }
        this.sprite.setFrame(0);
    }
  
    /*****************   ADICONALES   ****************************************/
    /*
    private int[] reescalaArray(int[] ini, int x, int y, int x2, int y2) {
    int out[] = new int[x2*y2];
    int dy, dx;
    for (int yy = 0; yy < y2; yy++) {
    dy = yy * y / y2;
    for (int xx = 0; xx < x2; xx++) {
    dx = xx * x / x2;
    out[(x2*yy)+xx]=ini[(x*dy)+dx];
    }
    }
        return out;  
    }
*/
   
    
}
