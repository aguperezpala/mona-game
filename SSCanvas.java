package hello;

import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.Canvas;

class SSCanvas extends Canvas implements Runnable{

	 //private Sprite miSprite=new Sprite(1);
	 //private int pos_agu=80;
	 //private int delta_x=0;
        
	 public boolean activo=false;
         private Random random=new Random();
         private int puntaje;
         private Personaje subZero = new Personaje("tira_mona_paso1.png", this.getWidth(), this.getHeight(), 100, 150, 60);
         private LayerManager lmanager;
       
         public SSCanvas()
         {         
           //random.setSeed(System.currentTimeMillis());
           subZero.set_pos(this.getWidth()/2, this.getHeight()/2);
           //subZero.set_transformation(subZero.getSprite().TRANS_MIRROR);
           lmanager = new LayerManager();
           lmanager.setViewWindow(0, 0, this.getWidth(), this.getHeight());
           //lmanager.insert(subZero.getSprite(), 0);
           lmanager.append(subZero.getSprite());
           
       
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
                        subZero.set_alive(true);         
                        break;
                    case KEY_NUM5:
         
                        break;
	            case LEFT:
                        subZero.set_transformation(Sprite.TRANS_MIRROR);
                        subZero.set_alive(false);
         
	            	break;
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
    g.setColor(255,0,0);
    g.fillRect(0,0,getWidth(),getHeight());
    lmanager.paint(g, 0, 0);
    g.drawString("PUNTAJE: "+ subZero.getSprite().getFrame(), 50, 10, 0);

}

 }
