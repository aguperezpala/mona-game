package hello;

import java.util.Random;
import javax.microedition.lcdui.Graphics;
//import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

class SSCanvas extends Canvas implements Runnable{


	 //private Sprite miSprite=new Sprite(1);
	 //private int pos_agu=80;
	 //private int delta_x=0;
    /* aca almacenamos los tiempos de cada nivel (por las dudas que no se pueda
     * reproducir el sonido
     */
    private final int levelTime[] = {184000,299000,257000,141000};
    private int actualTime;

    private LevelSelector levelSel = new LevelSelector("mapa.png", this.getWidth(), this.getHeight());
	private boolean activo = true;
    private boolean gameFinish = false;
    private Random random = new Random();
    private Personaje mona = new Personaje("tira_enorme.png", this.getWidth(), this.getHeight(), 100, 150, 70);
    //private Personaje mona = new Personaje("galpon.png", this.getWidth(), this.getHeight(), 5, 21, 70);
    private LayerManager lmanager;
    private Efecto luces = new Efecto("luz.png", this.getWidth(), this.getHeight(), 10);
    private Cartel bienAhi;    
    private BotonManager btnmng = new BotonManager ("flecha1.png", this.getWidth(),this.getHeight(),15,this.getHeight()-100,7);
    public boolean musicOn = true;
    private SoundPlayer sp = new SoundPlayer ("tema1.mid");
    public boolean firstRun = true;
    public boolean terminateGame = false;
    private Image finalImage = null;
    private int finalStrCoord[] = {0,0};
    private String finalStr = null;
    private BarraTiempo spaceTime = null;
    private int textCoords[] = {this.getWidth()/3, 2 * this.getWidth()/3, 0};


         public SSCanvas()
         {
             /* calculamos el tamaño Y de el puntaje y el multiplicador */
             this.textCoords[2] = Font.getFont(Font.FACE_SYSTEM,
                                 Font.STYLE_BOLD,Font.SIZE_LARGE).getBaselinePosition()+2;
             /* vamos a cargar la barra de tiempo en las posiciones (0,1/9) hasta
              * (1/7,windowHeight) */
             this.spaceTime = new BarraTiempo(1,this.getHeight()/9, this.getWidth()/7,
                     this.getHeight());
             mona.set_pos(this.getWidth()/2, this.getHeight()/2);             
             lmanager = new LayerManager();
             lmanager.setViewWindow(0, 0, this.getWidth(), this.getHeight());
             //lmanager.insert(mona.getSprite(), 0);             
             luces.setNumberOfObjects(3);
             bienAhi = new Cartel("bien_ahi.png", this.getWidth(), this.getHeight(),10);
             bienAhi.setPos(this.getWidth()/2, bienAhi.getSprite().getHeight()/2 +
                     this.textCoords[2]);
             bienAhi.setTimeToShow(2000);
             lmanager.append(bienAhi.getSprite());
             lmanager.append(mona.getSprite());
             btnmng.setAlive(true);
             mona.set_velocity(100);
             this.sp.setVolume(100);
            
            // sp.startMusic();          
             
         }
	 
	 
	 public void run () {
         int lastMultiplier = 0;
         int actualMultiplier = 0;
         int levelShowTime = LevelSelector.showLevelTime;
         long oldTime = System.currentTimeMillis();
         

         /* ahora vamos a chequear si es la primera corrida, si es asi entonces
          * vamos a mostrar el mapa al compienzo y despues seguir normalmente */
         if (this.firstRun) {
             this.firstRun = false;
             levelShowTime = LevelSelector.showLevelTime;             
             this.levelSel.loadLevel(this.levelSel.actualLevel, this.getHeight());
             this.gameFinish = true;
             while (levelShowTime >= 0) {
                 repaint();
                 serviceRepaints();
                 levelShowTime  -= (int) (System.currentTimeMillis() - oldTime);
                 oldTime = System.currentTimeMillis();
                 try {
                     Thread.sleep(5);
                 } catch (InterruptedException e) {
                     System.out.println(e.toString());
                }
             }
             this.gameFinish = false;
             this.levelSel.hide();
             levelShowTime = LevelSelector.showLevelTime;
             this.setLevel(0);
         }


         while (this.activo == true) {
             repaint();
             serviceRepaints();             


             /* debemos chequear que el tiempo de la musica no se haya
              * terminado, si se termina la musica se termina el "juego"
              */
             if (actualTime >= 0) {
                 /* todavia no se termino el juego */

                 /* actualizamos la spaceTime */
                 this.spaceTime.actualTime = actualTime;


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
                    levelShowTime = LevelSelector.showLevelTime;
                    /* aca chequeamos en realidad si podemos pasar de nivel o no */
                    this.levelSel.actualLevel++;
                    if (this.levelSel.actualLevel >= 4) {
                        /* TERMINO EL JUEGO */
                        /* reseteamos todos los valores variables etc nivel */                      
                         try {
                             this.finalImage = Image.createImage(getClass().getResourceAsStream("portada2.png"));
                             float factor = Resizer.getFactor(this.getHeight(),this.finalImage.getHeight(), 100);                             
                             this.finalStrCoord[1] = (int) (43 * factor);
                             this.finalImage = Resizer.resizeImage(this.finalImage, this.getWidth(),
                                     this.getHeight());
                             System.gc();
                         } catch (Exception e){System.out.print("Error cargando finalImage\n");}
                         this.finalStr = "Puntaje: " + this.btnmng.points;
                         this.finalStrCoord[0] = Font.getFont(Font.FACE_SYSTEM,
                                 Font.STYLE_BOLD,Font.SIZE_LARGE).stringWidth(finalStr);
                         this.finalStrCoord[0] = (this.getWidth() - this.finalStrCoord[0]) / 2;
                         terminateGame = true;
                         this.setMusicOff();
                         this.gameFinish = true;
                         this.setLevel(0);
                         this.firstRun = true;
                    } else {
                        this.levelSel.loadLevel(this.levelSel.actualLevel, this.getHeight());
                        this.setMusicOff();
                        this.gameFinish = true;
                    }
                 } else {
                     levelShowTime -= (int) (System.currentTimeMillis() - oldTime); /* depende del sleep del ciclo */
                     /* ahora deberiamos chequear que si termino de mostrarse volvemos
                      * a cargar el nuevo nivel, musica, escenografia y esas cosas */                     
                     if (levelShowTime <= 0) {
                         if (this.terminateGame) {
                             this.GamePause(true);

                         } else {
                             this.setMusicOn();
                             this.gameFinish = false;
                             this.levelSel.hide();
                             /* debemos reiniciar el tiempo de del nivel */
                             /*actualTime = this.levelTime[this.levelSel.actualLevel % this.levelTime.length];*/
                            /* Aca esta el problema, deberiamos hacer lo siguiente:
                             * deberiamos chequear si podemos aumentar de nivel o no, si
                             * no podemos es porque se termino el juego! entonces deberiamos
                             * mostrar algun "final".
                             */
                            this.setLevel(this.levelSel.actualLevel);
                         }
                     }

                 }
             }
             
             /*actualTime = (int) (this.sp.getDuration() - this.sp.getActualTime())/100;   /* depende del sleep de abajo */
             actualTime -= (int) (System.currentTimeMillis() - oldTime);
            /* System.out.print("dif: "+ (int) (System.currentTimeMillis() - oldTime) +"\t");*/
             oldTime = System.currentTimeMillis();
             /*System.out.print("actual:"+this.sp.getActualTime()+"\tduration: "+
                     this.sp.getDuration()+"\tdif: "+actualTime);*/
             try {
                 Thread.sleep(5);
             } catch (InterruptedException e) {
                 System.out.println(e.toString());
             }
         }
         if (this.terminateGame != false) {
             this.btnmng.points = 0;
             this.terminateGame = false;
             this.finalStr = null;
             this.finalImage = null;
             /* recargamos el tiempo del la primera cancion */
             /*actualTime = this.levelTime[this.levelSel.actualLevel % this.levelTime.length];*/
             actualTime = (int) this.sp.getDuration()/1000;
         }
         System.gc();
     }

	    public void keyPressed(int keyCode) {
	        boolean noApreto = false;
            int btnBits = 0;

	        switch (getGameAction(keyCode)) {
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
                default:
                    noApreto = true;
                    break;
            }
            if (noApreto) {
                switch (keyCode) {

                case KEY_NUM4:
                        mona.set_transformation(Sprite.TRANS_MIRROR);
                        btnBits = this.btnmng.pushButton(BotonManager.IZQUIERDA);
	            	break;

                    case KEY_NUM6:
                        btnBits = this.btnmng.pushButton(BotonManager.DERECHA);
                        break;
                    case KEY_NUM2:
                        btnBits = this.btnmng.pushButton(BotonManager.ARRIBA);
                        break;
                    case KEY_NUM8:
                        btnBits = this.btnmng.pushButton(BotonManager.ABAJO);
                        break;
              /*  case KEY_NUM1:
                    this.GamePause(true);
                    break;*/
	        }
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
            /*this.actualTime = this.levelTime[level % this.levelTime.length];*/
            this.levelSel.actualLevel = level % this.levelTime.length;
            /* aca seteamos la velocidad del nivel y los valores de la cantidad
             * de aciertos para cada multiplicador
             */
            switch (this.levelSel.actualLevel)
            {
                case 0:
                    this.btnmng.setVelocity(50);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = i+1;
                    }
                    /* seleccionamos la musica adecuada */
                    this.sp.setMusic("tema1.mid");
                    
                    break;
                case 1:
                    this.btnmng.setVelocity(45);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = 2*i+1;
                    }
                    /* seleccionamos la musica adecuada */
                    this.sp.setMusic("beso_a_beso.mid");
                    break;
                case 2:
                    this.btnmng.setVelocity(40);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = 2*i+2;
                    }
                    /* seleccionamos la musica adecuada */
                    this.sp.setMusic("el_enamorado.mid");
                    break;
                case 3:
                    this.btnmng.setVelocity(35);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = 3*i+1;
                    }
                    /* seleccionamos la musica adecuada */
                    this.sp.setMusic("tomao_to_vino.mid");
                    break;
                default:
                    this.btnmng.setVelocity(50);
                    for (int i = 0; i < this.btnmng.resultsCount.length; i++){
                        this.btnmng.resultsCount[i] = i+1;
                    }
                    break;
            }
            this.spaceTime.totalTime = (int) this.sp.getDuration()/1000;
            actualTime = this.spaceTime.totalTime;            
            this.sp.startMusic();
            /* reseteamos el multiplicador */
            this.btnmng.setMultiplier(1);
        }


        public void GamePause (boolean t)
        {
            this.activo = !t;
            mona.set_alive(!t);
            this.btnmng.setAlive(!t);
            /* deberiamos reactivar la musica */
            if (!this.firstRun) {
                if (t)
                    this.sp.stop();
                else
                    this.sp.startMusic();
            }
        }


public void paint (Graphics g)
{    
    if (this.gameFinish) {
        /* si se termino el juego debemos pasar al proximo nivel, mostrando
         * todo lo que corresponda al nivel
         */
        if (this.terminateGame) {
            /* dibujamos la tapa */
            g.setColor(0);
            if (finalImage != null)
                g.drawImage (finalImage, 0, 0, Graphics.LEFT|Graphics.TOP);
            if (this.finalStr != null){
                g.setFont( Font.getFont(Font.FACE_SYSTEM,
                                 Font.STYLE_BOLD,Font.SIZE_LARGE));
                g.drawString(finalStr, this.finalStrCoord[0], this.finalStrCoord[1],
                        Graphics.TOP|Graphics.LEFT);
            }
        } else {
            g.setColor(0xd6e6d2);
            g.fillRect(0,0,getWidth(),getHeight());
            this.levelSel.paint(g);
        }
    } else {
        int aux;
        g.setColor(0,0,0);
        g.fillRect(0,0,getWidth(),getHeight());
        luces.paint(g);
        lmanager.paint(g, 0, 0);
        g.setColor(255, 0, 0);    
        g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE));
        this.btnmng.paint(g);
        g.setColor(0x000000FF); /* azul */
        this.spaceTime.paint(g);

         //graficamos momentaneamente los puntos
        /*g.setColor(0x00FFFFFF);
        g.fillRect(0, 0, this.textCoords[0], this.textCoords[2]);
        g.fillRect(this.textCoords[1], 0, this.textCoords[0], this.textCoords[2]);
        g.setColor(0);*/
        g.setColor(0x00FFFFFF);
        aux = (this.textCoords[0] - g.getFont().stringWidth(""+this.btnmng.points))/2;
        g.drawString(""+this.btnmng.points, aux, 1,
                Graphics.TOP|Graphics.LEFT);
        g.drawString("X"+this.btnmng.globalMultiplier, this.textCoords[1] +
                this.textCoords[0]/2, 1, Graphics.TOP|Graphics.LEFT);
    }


}
/*
protected void hideNotify ()
{
    this.GamePause(true);    
}
*/
 }
