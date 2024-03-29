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
    static final int MULT_POINTS_BLUE = 4;
    static final int MULT_POINTS_GREEN = 3;
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
    /* Esta variable determina la cantidad de aciertos que tiene que haber para
     * pasar al proximo "multiplicador"
     */
    private BarraTiempo spaceTime = null;
    public int resultsCount[] = {2,3,4,5};
    private int actualResultsCount = 0;             /* index del resultsCount */
    private int actualResults = 0;                  /* "determinador para avanzar resultcount */
    private final int VELOCITY_MULTIPLIER_FACTOR = 10;
    public int globalMultiplier = 1;   /* lo vamos a usar para el "X1" X2"...etc */
    public int points = 0;
    private boolean finish = false;     /* flag que determina el final de cada secuencia */
    private boolean finishOk = false;
    private Sprite sprite;      //flecha
    private boolean alive;
    private int velocity = 50;
    private Boton boton[] = new Boton[3];
    private Random rnd = new Random();
    private int actualButton = 0;
    Thread tbm;
    private int buttonsInactive = 0;
    /* este entero lo vamos a usar como mapa de bits para almacenar que botones
     * se apretaron durante una secuencia de la siguiente forma
     * 0x00000XXX donde X puede ser 1 2 3 4 dependiendo que tecla se apreto
     * (izquierda derecha arriba abajo respectivamente)
     */
    private int buttonsBits = 0;



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
        this.spaceTime = new BarraTiempo(6*px/7-1, py/9,  px-2, py);
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

        /*this.spaceTime = new BarraTiempo((6*px/7)-1,py/9, px/7, py);*/        
        

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
              /* debemos chequear que el thread haya muerto */
            if (!this.alive) {
                this.alive = true;
                /* si el thread no fue creado ni esta vivo */
                if (this.tbm == null || !this.tbm.isAlive()) {
                    this.tbm = null;
                    this.tbm = new Thread(this);
                    tbm.start();  //chequear esto
                }
            }
        } else {            
            this.alive = false;
        }
    }

    private int getPoints(int p)
    {
        return ((p/100) + 1);
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
        this.buttonsInactive++;
        this.actualButton = (this.actualButton + 1) % this.boton.length;

        if (blue != 0) {
            //entonces estamos en el caso azul/verde
            if (blue >= green) {
                this.points += (this.getPoints(blue) * MULT_POINTS_BLUE * this.globalMultiplier);
            } else {
                this.points += (this.getPoints(green) * MULT_POINTS_GREEN * this.globalMultiplier);
            }
        } else {
            //estamos en el caso verde/rojo
            if (green >= reed) {
                this.points += (this.getPoints(green) * MULT_POINTS_GREEN * this.globalMultiplier);
            } else {
                this.points += (this.getPoints(reed) * MULT_POINTS_REED * this.globalMultiplier);
            }
        }
        if (this.buttonsInactive == 2) {
            /* debemos reiniciar el mapa de bits */
            this.buttonsBits = 0;
        }

    }
    /* Esta funcion devuelve el mapa de bits de las teclas apretadas hasta ahora.
     * En caso de que todavia no se haya completado la secuencia se devuelve
     * simplemente 0, caso contrario la secuencia
     */
    public int pushButton (int button)
    {
        int result = 0;
        if (this.boton[this.actualButton].getOrientation() == this.orientations[button]) {
            /* primero vamos a setear el mapa de bits */
            this.buttonsBits = this.buttonsBits | ((button + 1) << ((this.boton.length-this.actualButton) * 4));
            if (this.buttonsInactive == 2)
                result = this.buttonsBits;
            //si apretamos el correcto entonces:
            this.doAcert();
        }
        return result;
    }

    public void run ()
    {
        int actualColor = 0;        
        
        while (this.alive)
        {            
            this.finish = false;
            this.finishOk = false;
            actualColor = 255;
            buttonsInactive = 0;
            this.actualButton = 0;  //el primer boton no fue apretado todavia
            //seteamos los colores principales
            for (int i = 0; i < 3; i++) {
                this.boton[i].setColor(0x000000FF);//seteamos todo azul
                this.boton[i].setActive(true);     //activamos todos los botones
                //ahora le vamos a setear un random de orientacion
                this.boton[i].setOrientation(BotonManager.orientations[rnd.nextInt(4)]);
            }

            this.spaceTime.totalTime = 500;
            this.spaceTime.actualTime = 500;
            //******     TRANSFORMACION DE AZUL A VERDE **************
            while (actualColor > 0 && buttonsInactive < 3) {
                actualColor = actualColor - BotonManager.COLOR_CHANGE_VELOCITY;
                this.spaceTime.actualTime -= BotonManager.COLOR_CHANGE_VELOCITY;

                for (int i = buttonsInactive; i < 3; i++) {
                  //  if (boton[i].isActive()) {  //si esta activo cambiamos de color
                        //le ponemos el color azul y verde combinados
                    boton[i].setColor(actualColor | ((Math.abs(255-actualColor)) << 8));
                    //}
                }
                try {Thread.sleep(this.velocity);} catch (InterruptedException ex) {
                 ex.printStackTrace();
                }                
            }            
            //******     TRANSFORMACION DE VERDE A ROJO **************
            actualColor = 255;
            while (actualColor > 0 && buttonsInactive < 3) {
                actualColor = actualColor - BotonManager.COLOR_CHANGE_VELOCITY;
                this.spaceTime.actualTime -= BotonManager.COLOR_CHANGE_VELOCITY;

                for (int i = buttonsInactive; i < 3; i++) {
                    //if (boton[i].isActive()) {  //si esta activo cambiamos de color
                        //le ponemos el color azul y verde combinados
                    boton[i].setColor((actualColor << 8) | ((Math.abs(255-actualColor)) << 16));
                    //}
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


             try {Thread.sleep(this.velocity+200);} catch (InterruptedException ex) {
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

    /* Funcion que aumenta hasta el proximo multiplicador devolviendo el actual */
    public int setNextMultiplier ()
    {
        if (this.resultsCount[this.actualResultsCount] <= this.actualResults) {
            /* debemos aumentar el actualresultcount y aumentar el multiplier */
            /* aumentamos el multiplier */
            if (this.globalMultiplier < 4) {
                this.globalMultiplier = this.globalMultiplier + 1;
                /* debemos aumentar la velocidad ==> disminuir velocity */
                this.velocity -= VELOCITY_MULTIPLIER_FACTOR;
                /* aumentamos el actualResultsCount */
                this.actualResultsCount = ((this.actualResultsCount + 1) % this.resultsCount.length);
                this.actualResults = 0;
            }
            
        } else {
            /* aumentamos el results */
            this.actualResults++;
        }
        return this.actualResultsCount;
    }

    public void setMultiplier (int n)
    {
        if (n >= 1 && n <= 4) {
            /* falta el tema de la velocidad */
            this.globalMultiplier = n;
        }
    }

    public int setPrevMultiplier ()
    {
        if (this.globalMultiplier > 1) {
            this.globalMultiplier = this.globalMultiplier - 1;
            /* debemos disminuir la velocidad ==> aumentar velocity */
            this.velocity += VELOCITY_MULTIPLIER_FACTOR;
            /* decrementamos el actual results count */
            this.actualResultsCount = ((this.actualResultsCount - 1) % this.resultsCount.length);
            this.actualResults = 0;
        }
        return this.actualResultsCount;
    }

    public void paint (Graphics g)
    {       
        //si estan "vivos" los botones los dibujamos
        for (int i = 0; i < 3; i++){
            if (boton[i].isAlive())
                boton[i].paint(g);
        }
        this.spaceTime.paint(g);
        

    }
    
    
      

}
