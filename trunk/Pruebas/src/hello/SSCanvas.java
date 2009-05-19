package hello;

import java.util.Random;
import javax.microedition.lcdui.Graphics;
//import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;

class SSCanvas extends Canvas implements Runnable{


	 //private Sprite miSprite=new Sprite(1);
	 //private int pos_agu=80;
	 //private int delta_x=0;
    /* aca almacenamos los tiempos de cada nivel (por las dudas que no se pueda
     * reproducir el sonido
     */
    private final int levelTime[] = {184000,184000,184000,184000};
    private int actualTime;

    private LevelSelector levelSel = new LevelSelector("mapa_chico2.png");
	private boolean activo = true;
    private boolean gameFinish = false;
    private Random random=new Random();
    private int puntaje;
    private Personaje mona = new Personaje("tira_enorme.png", this.getWidth(), this.getHeight(), 100, 150, 70);
    private LayerManager lmanager;
    private Efecto luces = new Efecto("luz.png", this.getWidth(), this.getHeight(), 10);
    private Cartel bienAhi;    
    private BotonManager btnmng = new BotonManager ("flecha.png", this.getWidth(),this.getHeight(),15,this.getHeight()-100,7);
    public boolean musicOn = true;
    private SoundPlayer sp = new SoundPlayer ("tema1.mid");


         public SSCanvas()
         {
             /* iniciamos el tiempo del nivel actual */
             this.actualTime = this.levelTime[0];
             mona.set_pos(this.getWidth()/2, this.getHeight()/2);             
             lmanager = new LayerManager();
             lmanager.setViewWindow(0, 0, this.getWidth(), this.getHeight());
             //lmanager.insert(mona.getSprite(), 0);
             lmanager.append(mona.getSprite());
             luces.setNumberOfObjects(3);
             bienAhi = new Cartel("bien_ahi.png", this.getWidth(), this.getHeight(),10);
             bienAhi.setPos(this.getWidth()/2, bienAhi.getSprite().getHeight()/2);
             bienAhi.setTimeToShow(2000);
             lmanager.append(bienAhi.getSprite());
             btnmng.setAlive(true);
             mona.set_velocity(100);
             sp.startMusic();
         }
	 
	 
	 public void run () {
         int lastMultiplier = 0;
         int actualMultiplier = 0;
         int levelShowTime = LevelSelector.showLevelTime;         

         while (this.activo == true) {
             repaint();
             serviceRepaints();

             /* debemos chequear que el tiempo de la musica no se haya
              * terminado, si se termina la musica se termina el "juego"
              */
             if (actualTime >= 0) {
                 /* todavia no se termino el juego */


                 if (btnmng.isFinish()) {
                     /* debemos hacer:
                      * 1) sacar determinar el multiplicador y/o
                      * sacar el multiplicador en caso de no haber terminado
                      * correctamente la secuencia.
                      * 2) Mostrar el cartel si una sola vez? o lo mostramos
                      * durante todo el tiempo?
                      */

                     /* (1) */
                     if (btnmng.isFinishOk()) {
                         /* si termino ok aumentamos el multiplyer */
                         actualMultiplier = btnmng.setNextMultiplier();
                         if (actualMultiplier > lastMultiplier) {
                             /* mostramos un "bien ahi" pòrque aumento de nivel */
                             bienAhi.show();
                             /* deberiamos cambiar el "multiplicador" (imagen) */
                             lastMultiplier = actualMultiplier;
                         }
                     } else {
                         /* tenemos 2 posibilidades, o resetearlo al multiplicador
                          * 1 o bajarlo de a uno*/
                         actualMultiplier = btnmng.setPrevMultiplier();
                         lastMultiplier = actualMultiplier;
                     }
                     btnmng.setIsFinish(false);
                 }
             } else {
                 /* si se termino el juego */
                 if (!this.gameFinish){
                    this.gameFinish = true;
                    levelShowTime = LevelSelector.showLevelTime;
                    /* aca chequeamos en realidad si podemos pasar de nivel o no */
                    this.levelSel.actualLevel++;
                    this.setLevel(this.levelSel.actualLevel);
                    
                    this.levelSel.loadLevel(this.levelSel.actualLevel);
                    System.out.print("TERMINO EL JUEGO! AHROA MOSTRAMOS EL MAPA\n");
                 } else {
                     levelShowTime = levelShowTime - 5; /* depende del sleep del ciclo */
                     /* ahora deberiamos chequear que si termino de mostrarse volvemos
                      * a cargar el nuevo nivel, musica, escenografia y esas cosas */
                     if (levelShowTime <= 0) {
                         this.gameFinish = false;
                         this.levelSel.hide();
                         /* debemos reiniciar el tiempo de del nivel */
                         actualTime = this.levelTime[this.levelSel.actualLevel % this.levelTime.length];
                     }

                 }
             }
             actualTime -= 5;   /* depende del sleep de abajo */
             try {
                 Thread.sleep(5);
             } catch (InterruptedException e) {
                 System.out.println(e.toString());
             }
         }
     }

	    public void keyPressed(int keyCode) {
	        int action=getGameAction(keyCode);
            int btnBits = 0;

	        switch (action) {
                    case FIRE:                        
                      //  flecha.setTransform(Sprite.TRANS_ROT90);
                       // flecha.setPosition(0, 0);
                        break;
                    case KEY_NUM5:
         
                        break;
	            case LEFT:
                        mona.set_transformation(Sprite.TRANS_MIRROR);
                        btnBits = this.btnmng.pushButton(BotonManager.IZQUIERDA);
	            	break;

                    case RIGHT:
                        btnBits = this.btnmng.pushButton(BotonManager.DERECHA);
                        break;
                    case UP:
                        btnBits = this.btnmng.pushButton(BotonManager.ARRIBA);
                        break;
                    case DOWN:
                        btnBits = this.btnmng.pushButton(BotonManager.ABAJO);
                        break;
	        }
            if (btnBits != 0) {
                /* si se completo la secuencia enviamos el mapa de bits al personaje
                 * para realizar determinada accion */
                //System.out.print("1:"+((btnBits & 0x000000F0)>>4) +"\n");
                mona.selectAnimFromMultiplier(btnBits);
            }
	    }

        public void setMusicOff ()
        {
            this.musicOn = false;
            this.sp.setVolume(0);
        }
        public void setMusicOn ()
        {
            this.musicOn = true;
            this.sp.setVolume(100);

        }
        public void setLevel (int level)
        {
            this.actualTime = this.levelTime[level % this.levelTime.length];
            this.levelSel.actualLevel = level % this.levelTime.length;
            /* aca seteamos la velocidad del nivel y los valores de la cantidad
             * de aciertos para cada multiplicador
             */
            switch (level)
            {
                case 0:
                    this.btnmng.setVelocity(50);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = i+1;
                    }
                    break;
                case 1:
                    this.btnmng.setVelocity(45);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = 2*i+1;
                    }
                    break;
                case 2:
                    this.btnmng.setVelocity(40);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = 2*i+2;
                    }
                    break;
                case 3:
                    this.btnmng.setVelocity(35);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = 3*i+1;
                    }
                    break;
                default:
                    this.btnmng.setVelocity(50);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = i+1;
                    }
                    break;
            }
        }


        public void GamePause (boolean t)
        {
            this.activo = !t;
            mona.set_alive(!t);
            this.btnmng.setAlive(!t);
            /* deberiamos reactivar la musica */
            if (t)
                this.sp.stop();
            else
                this.sp.startMusic();
        }


public void paint (Graphics g)
{
    g.setColor(0,0,0);
    g.fillRect(0,0,getWidth(),getHeight());
    
    if (this.gameFinish) {
        /* si se termino el juego debemos pasar al proximo nivel, mostrando
         * todo lo que corresponda al nivel
         */
        this.levelSel.paint(g);
    } else {        
        luces.paint(g);
        lmanager.paint(g, 0, 0);
        g.setColor(255, 0, 0);
        //g.setFont(Font.getFont(Font.FONT_STATIC_TEXT, Font.STYLE_ITALIC, Font.SIZE_LARGE));
       // g.drawString("BIEN AHI!!", 50, 10, 0);
        //g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
        //g.drawString("BIEN AHI!!", 50, 25, 0);
        g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE));
     //   g.drawString("Memory: "+ Runtime.getRuntime().freeMemory(), 50, 40, 0);
        //g.drawString("Free memory: "+ Runtime.getRuntime().freeMemory(), 50, 10, 0);
        //derecha.paint(g);
    //    flecha.paint(g);
       // g.drawString("time:"+this.actualTime, 0, 0, 0);

        this.btnmng.paint(g);
    }


}

 }
