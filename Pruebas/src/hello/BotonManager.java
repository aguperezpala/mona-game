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
    static final int MULT_POINTS_BLUE = 8;
    static final int MULT_POINTS_GREEN = 4;
    static final int MULT_POINTS_REED = 2;
    
    //debe ser multiplo de 255, es la var que controla que tan rapido cambia de color
    static final int COLOR_CHANGE_VELOCITY = 5; 


    /*                      ORIENTACIONES
     * Izquierda = 0
     * Derecha   = 1
     * Arriba    = 2
     * Abajo     = 3
    */

    static final int IZQUIERDA = 0;
    static final int DERECHA = 1;
    static final int ARRIBA = 2;
    static final int ABAJO = 3;
    static final int orientations[] = {Sprite.TRANS_NONE, Sprite.TRANS_MIRROR,
    Sprite.TRANS_ROT90, Sprite.TRANS_ROT270};
    

    /* Vamos a modificar la velocidad gradualmente segun una constante para cada
     * "multiplicador"
     */
    private final int VELOCITY_MULTIPLIER_FACTOR = 10;
    private int globalMultiplier = 1;   /* lo vamos a usar para el "X1" X2"...etc */
    private int points = 0;
    private boolean finish = false;     /* flag que determina el final de cada secuencia */
    private boolean finishOk = false;
    private Sprite sprite;      //flecha
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

    private int getPoints(int p)
    {
        return ((p/10) + 1) * this.globalMultiplier;
    }
    //Funcion que es llamada cuando se produce un acierto
    private void doAcert()
    {
        /* Los puntos los vamos a determinar segun el color que tenga el boton
         * Hay 2 posibilidades:
         * - Que este en azul con verde.
         * - Que este en verde con rojo.
         * Vamos a calcular los puntos de la siguiente manera:
         * Vamos a tener 3 multiplicadores, xblue,xgreen,xreed. Vamos a obtener
         * cada porcentaje de color y comparar entre si, y segun eso multiplicar
         * por el multiplicador correspondiente.
        */
        int reed = this.boton[this.actualButton].getReedColor();
        int green = this.boton[this.actualButton].getGreenColor();
        int blue = this.boton[this.actualButton].getBlueColor();

        //desactivamos el boton
        this.boton[this.actualButton].setActive(false);
        this.actualButton = (this.actualButton + 1) % 4;

        if (blue != 0) {
            //entonces estamos en el caso azul/verde
            if (blue >= green) {
                this.points += this.getPoints(blue) * MULT_POINTS_BLUE;
            } else {
                this.points += this.getPoints(green) * MULT_POINTS_GREEN;
            }
        } else {
            //estamos en el caso verde/rojo
            if (green >= reed) {
                this.points += this.getPoints(green) * MULT_POINTS_GREEN;
            } else {
                this.points += this.getPoints(reed) * MULT_POINTS_REED;
            }
        }
    }
    
    public void pushButton (int button)
    {
        if (this.boton[this.actualButton].getOrientation() == this.orientations[button]) {
                    //si apretamos el correcto entonces:
                    this.doAcert();
        }                
    }

    public void run ()
    {
        int actualColor = 0;
        
        while (this.alive)
        {            
            this.finish = false;
            this.finishOk = false;
            actualColor = 255;
            this.actualButton = 0;  //el primer boton no fue apretado todavia
            //seteamos los colores principales
            for (int i = 0; i < 3; i++) {
                this.boton[i].setColor(0x000000FF);//seteamos todo azul
                this.boton[i].setActive(true);     //activamos todos los botones
                //ahora le vamos a setear un random de orientacion
                this.boton[i].setOrientation(BotonManager.orientations[rnd.nextInt(4)]);
            }

            
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
                actualColor = actualColor - BotonManager.COLOR_CHANGE_VELOCITY;

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
            /* chequeamos si hubo acierto para todos los elementos */

            this.finishOk = true;
            for (int i = 0; i < this.boton.length; i++)
                this.finishOk &= !(this.boton[i].isActive());
            this.finish = true;


             try {Thread.sleep(this.velocity);} catch (InterruptedException ex) {
                 ex.printStackTrace();
             }
        }

    }

    public boolean isFinish ()
    {
        return this.finish;
    }

    /* vamos a tunear esta cochinada */
    public void setIsFinish (boolean b)
    {
        this.finish = b;
    }
    public boolean isFinishOk()
    {
        return this.finishOk;
    }

    /* Funcion que aumenta hasta el proximo multiplicador */
    public void setNextMultiplier ()
    {
        if (this.globalMultiplier < 4) {
            this.globalMultiplier = this.globalMultiplier + 1;
            /* debemos aumentar la velocidad ==> disminuir velocity */
            this.velocity -= VELOCITY_MULTIPLIER_FACTOR;
        }
    }

    public void setMultiplier (int n)
    {
        if (n >= 1 && n <= 4) {
            /* falta el tema de la velocidad */
            this.globalMultiplier = n;
        }
    }

    public void setPrevMultiplier ()
    {
        if (this.globalMultiplier > 1) {
            this.globalMultiplier = this.globalMultiplier - 1;
            /* debemos disminuir la velocidad ==> aumentar velocity */
            this.velocity += VELOCITY_MULTIPLIER_FACTOR;
        }
    }

    public void paint (Graphics g)
    {
        //si estan "vivos" los botones los dibujamos
        for (int i = 0; i < 3; i++){
            if (boton[i].isAlive())
                boton[i].paint(g);
        }
        //graficamos momentaneamente los puntos
        g.drawString("PUNTOS: "+ this.points, 50, 23, 0);

    }
    
    
      

}
