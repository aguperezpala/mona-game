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
        
	private boolean activo = true;
    private boolean gameFinish = false;
    private Random random=new Random();
    private int puntaje;
    private Personaje mona = new Personaje("tira_enorme.png", this.getWidth(), this.getHeight(), 100, 150, 70);
    private LayerManager lmanager;
    private Efecto luces = new Efecto("luz.png", this.getWidth(), this.getHeight(), 10);
    private Cartel bienAhi;    
    private BotonManager btnmng = new BotonManager ("flecha.png", this.getWidth(),this.getHeight(),15,this.getHeight()-100,7);
    private SoundPlayer sp = new SoundPlayer ("tema1.mid");


         public SSCanvas()
         {             
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

         while (this.activo == true) {
             repaint();
             serviceRepaints();

             /* debemos chequear que el tiempo de la musica no se haya
              * terminado, si se termina la musica se termina el "juego"
              */
             if (this.sp.getActualTime() < this.sp.getDuration() || !this.gameFinish) {
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
                 if (!this.gameFinish)
                    this.gameFinish = true;
                 else {

                 }
             }
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
                mona.setAnim(btnBits);
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
               /* else
                     ejecutamos la musiva correspondiente al nivel;
                     */
               
            }

public void keyReleased (int keyCode) {
   /* int action=getGameAction(keyCode);

    switch (action) {

        case LEFT:
            //miSprite.setX(miSprite.getX()+5);
        	break;
        case RIGHT:
        	//miSprite.setX(miSprite.getX()-5);
    //    	delta_x=0;
            break;
        case UP:
      //  	miSprite.setY(miSprite.getY()+1);
            break;
        case DOWN:       
        //	miSprite.setY(miSprite.getY()-5);
            break;
        
    }*/
}

public void paint (Graphics g)
{
    g.setColor(0,0,0);
    g.fillRect(0,0,getWidth(),getHeight());
    
    if (this.gameFinish) {
        /* si se termino el juego debemos pasar al proximo nivel, mostrando
         * todo lo que corresponda al nivel
         */
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

        this.btnmng.paint(g);
    }


}

 }
