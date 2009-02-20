/* Clase que se va a encargar de manejar los botones:
 * -Crearlos y posicionarlos.
 * -Color (y cambiarlos de color a medida que pasa el tiempo.
 * -Tiempo mostrado en pantalla.
 * -Son apretados o no (activarlos y/o desactivarlos.
 *
 * El sprite de la flecha debe estar señalando a la izquierda.
 */

package hello;

import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.Graphics;
import java.util.Random;


/**
 *
 * @author agustin
 */
public class BotonManager implements Runnable{

    /* CONFIGURACIONES */
    static final int BLUE_TIME = 8;
    
    //debe ser multiplo de 255, es la var que controla que tan rapido cambia de color
    static final int COLOR_CHANGE_VELOCITY = 17; 


    /*                      ORIENTACIONES
     * Izquierda = 0
     * Derecha   = 1
     * Arriba    = 2
     * Abajo     = 3
    */

    static final int orientations[] = {Sprite.TRANS_NONE, Sprite.TRANS_MIRROR,
    Sprite.TRANS_ROT270, Sprite.TRANS_ROT90};
   
    //private int numOfButtons;
    private Sprite sprite;      //flecha
    //private int distBetwenButtons;
    private boolean alive;
    private int velocity = 50;
    private Boton boton[] = new Boton[3];
    private Random rnd = new Random();
    private int actualButton = 0;
    Thread tbm;
    

    /* Constructor:
     * Requires:
     *          nomImg = nombre de la imagen de la flecha
     *          py     = alto de la pantall
     *          px     = ancho de la pantalla
     *          scale  = escala de cuanto es boton (EL CIRCULO)
     *          pos    = posicion de los botones con respecto a Y
     *          dbb    = distancia entre los botones (porcentaje)
    */

    public BotonManager (String nomImg, int px, int py, int scale, int pos, int dbb)
    {
        //cargamos imagen con Resizer
        this.sprite = Resizer.spriteResized(nomImg, 0, py, scale-5, true);
        //CALCULAMOS LA ESCALA DE LA FLECHA
        scale = scale - 1;
        //creamos la cantidad de botones (boton = new Boton[nb]
        boton[0] = new Boton(this.sprite, py, scale, Sprite.TRANS_NONE);
        boton[1] = new Boton(this.sprite, py, scale, Sprite.TRANS_MIRROR);
        boton[2] = new Boton(this.sprite, py, scale, Sprite.TRANS_ROT270);
       //DEBEMOS CALCULAR EL PORCENTAJE dbb

        //calculamos la posicion en X de los 3 botones que se van a mostrar
        int posy = py - this.boton[0].getDiametro() - 3;  //dejamos 3 pixeles
        int posx = (px / 2) - (3 * boton[0].getDiametro())/2 - dbb;
        this.boton[0].setPosition(posx, posy);
        posx = posx + dbb + boton[0].getDiametro();
        this.boton[1].setPosition(posx, posy);
        posx = posx + dbb + boton[0].getDiametro();
        this.boton[2].setPosition(posx, posy);
        
        this.boton[0].setOrientation(BotonManager.orientations[rnd.nextInt(4)]);
        this.boton[1].setOrientation(BotonManager.orientations[rnd.nextInt(4)]);
        this.boton[2].setOrientation(BotonManager.orientations[rnd.nextInt(4)]);
        /*
        this.boton[0].setAlive(true);
        this.boton[1].setAlive(true);
        this.boton[2].setAlive(true);
        */
    }


    public void setVelocity(int v)
    {
        this.velocity = v;
    }
    public int getVelocity ()
    {
        return this.velocity;
    }
    public void setAlive (boolean b)
    {
          if (b == true) {
            if (!this.alive) {
                this.alive = true;
                this.tbm=new Thread(this);
                tbm.start();  //chequear esto
            }             
        } else {            
            this.alive = false;
        }
    }

    public void run ()
    {
        int actualColor;
        while (this.alive)
        {
            actualColor = 255;
            this.actualButton = 0;  //el primer boton no fue apretado todavia
            //seteamos los colores principales
            for (int i = 0; i < 3; i++)
                boton[i].setColor(0x000000FF);//seteamos todo azul

            
            //******     TRANSFORMACION DE AZUL A VERDE **************
            while (actualColor > 0) {
                actualColor = actualColor - this.COLOR_CHANGE_VELOCITY;

                for (int i = 0; i < 3; i++) {
                    if (boton[i].isActive()) {  //si esta activo cambiamos de color
                        //le ponemos el color azul y verde combinados
                        boton[i].setColor(actualColor | ((Math.abs(255-actualColor)) << 8));
                    }                    
                }
                try {Thread.sleep(this.velocity);} catch (InterruptedException ex) {
                 ex.printStackTrace();
                }                
            }
            
            //******     TRANSFORMACION DE VERDE A ROJO **************
            actualColor = 255;
            while (actualColor > 0) {
                actualColor = actualColor - this.COLOR_CHANGE_VELOCITY;

                for (int i = 0; i < 3; i++) {
                    if (boton[i].isActive()) {  //si esta activo cambiamos de color
                        //le ponemos el color azul y verde combinados
                        boton[i].setColor((actualColor << 8) | ((Math.abs(255-actualColor)) << 16));
                    }                    
                }
                try {Thread.sleep(this.velocity);} catch (InterruptedException ex) {
                 ex.printStackTrace();
                }                
            }
            
             try {Thread.sleep(this.velocity);} catch (InterruptedException ex) {
                 ex.printStackTrace();
             }
        }

    }
    public void paint (Graphics g)
    {
        //si estan "vivos" los botones los dibujamos
        for (int i = 0; i < 3; i++){
            if (boton[i].isAlive())
                boton[i].paint(g);
        }

    }
    
    
      

}
