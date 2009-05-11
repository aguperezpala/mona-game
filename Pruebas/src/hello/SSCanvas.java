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
        
	 public boolean activo=false;
         private Random random=new Random();
         private int puntaje;
         private Personaje mona = new Personaje("tira_enorme.png", this.getWidth(), this.getHeight(), 100, 150, 70);
         private LayerManager lmanager;
         private Efecto luces = new Efecto("luz.png", this.getWidth(), this.getHeight(), 10);
         private Cartel bienAhi;
         //private Sprite flecha = Resizer.spriteResized("flecha.png", this.getWidth(), this.getHeight(), 10, false);
         //private Boton derecha = new Boton(flecha, this.getHeight(),10,Sprite.TRANS_MIRROR);
         private BotonManager btnmng = new BotonManager ("flecha.png", this.getWidth(),this.getHeight(),15,this.getHeight()-100,7);
         private SoundPlayer sp = new SoundPlayer ("tema1.mid");

       
         public SSCanvas()
         {         
           //random.setSeed(System.currentTimeMillis());
           mona.set_pos(this.getWidth()/2, this.getHeight()/2);
           //mona.set_transformation(mona.getSprite().TRANS_MIRROR);
           lmanager = new LayerManager();
           lmanager.setViewWindow(0, 0, this.getWidth(), this.getHeight());
           //lmanager.insert(mona.getSprite(), 0);
           lmanager.append(mona.getSprite());
           luces.setNumberOfObjects(3);
           bienAhi = new Cartel("bien_ahi.png", this.getWidth(),this.getHeight(),10);
           bienAhi.setPos(this.getWidth()/2, bienAhi.getSprite().getHeight()/2);
           lmanager.append(bienAhi.getSprite());
           btnmng.setAlive(true);
          /* derecha.setColor(0x0fffffff);
           derecha.setAlive(true);
           derecha.setPosition(150, 100);
           */
          // flecha.setPosition(0, 0);
           mona.set_velocity(100);
           sp.startMusic();
       
	 }
	 
	 
	 public void run() {
	        //this.activo=false;
            
             
                  while (true) {                 
                    repaint();
                    serviceRepaints();

                    try {
	                Thread.sleep(5);
	            } catch (InterruptedException e) {
	                System.out.println(e.toString());
	            }
	        }
                //this.activo=false;
	     
	    }  

	    public void keyPressed(int keyCode) {
	        int action=getGameAction(keyCode);

	        switch (action) {
                    case FIRE:                        
                      //  flecha.setTransform(Sprite.TRANS_ROT90);
                       // flecha.setPosition(0, 0);
                        break;
                    case KEY_NUM5:
         
                        break;
	            case LEFT:
                        mona.set_transformation(Sprite.TRANS_MIRROR);
                        this.btnmng.pushButton(BotonManager.IZQUIERDA);         
	            	break;

                    case RIGHT:
                        bienAhi.show();
                        this.btnmng.pushButton(BotonManager.DERECHA);
                        break;
                    case UP:
                        this.btnmng.pushButton(BotonManager.ARRIBA);
                        break;
                    case DOWN:
                        this.btnmng.pushButton(BotonManager.ABAJO);
                        break;
	        }
	    }
            public void GamePause (boolean t)
            {
                if (t) {
                    mona.set_alive(!t);
                    this.btnmng.setAlive(!t);
                    this.sp.stop();
                    this.activo = !t;
                }

            }

public void keyReleased (int keyCode) {
    int action=getGameAction(keyCode);

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
        
    }
}

public void paint (Graphics g)
{

    g.setColor(0,0,0);
    g.fillRect(0,0,getWidth(),getHeight());
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
