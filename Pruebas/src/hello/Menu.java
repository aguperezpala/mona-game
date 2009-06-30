/* Esta clase lo que hace es generar un menu, devolviendo en caso de seleccion,
 * el numero de menu seleccionado. Lo que hace es muestra en pantalla una lista
 * de strings con un icono al lado. Todos los strings van a estar puestos 2/5
 * hacia la derecha del comienzo de pantalla y ubicados en el medio separados
 * por SIZE_BETWEN_OPTIONS_Y.
 * Sera pintado primero el fondo del color backColor, luego la imagen de fondo
 * en el centro de la pantalla, y luego los strings del color menuColor.
 * */
package hello;


import javax.microedition.lcdui.Graphics;
//import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;

class Menu extends Canvas implements Runnable {

    static final int SIZE_BETWEN_OPTIONS_Y = 15;
    static final int SIZE_OF_STRINGS = 10;      /*suponemos el tamaño de la fuente*/
    static final int SPACE_SELECTOR_STRINGS = 20;
    static final int SELECTOR_PORC_SIZE = 5;   /* porcentaje en cuanto a la pantalla */

    private boolean alive = false;
    public String options[];   /*array donde almacenamos las opciones de menu*/
    private int startYPos;      /*desde donde van a ser dibujados los strings*/
    private int startXPos;      /*posicion en X de los strings*/
    private int selectorPosX;   /*posicion del icono selector*/
    private Image backImg;
    //private int backImgY;
    //private int backImgX;
    private Image selectorImg;  /*iconito selector*/
    private int backColor;      /*color del fondo: negro*/
    private int menuColor;      /*color de las letras*/
    private int actualOps = 0;  /*en que opcion esta el selector*/
    private int response[];     /*donde se va a guardar la respuesta*/




	/*Constructor:
         * backImg & selectorImg no van a ser resizesadas
         * resp != NULL
        */
         public Menu(String[] ops, String backImg, String selectorImg, int backColor,
                 int menuColor, int[]resp)
         {
             this.alive = true;
             this.options = ops;
             this.backColor = backColor;
             this.menuColor = menuColor;

             if (resp == null)
                 System.out.print("ERROR: resp == NULL al crear el menu\n");
             else
                 this.response = resp;


             /*Cargamos las imagenes*/
             try {
                 this.selectorImg = Image.createImage(getClass().getResourceAsStream(selectorImg));
                 /* ahora la rotamos */
                 try {
                     this.selectorImg = Resizer.rotateImage(this.selectorImg, 180);
                 } catch (Exception e) {System.out.print("Menu: error rotate image\n");}
                    //calculamos cantidad de cuadros en filas y columnas
                    float factor = Resizer.getFactor(this.getHeight(),this.selectorImg.getHeight(),
                            SELECTOR_PORC_SIZE);
                   // this.selectorImg = Resizer.resizeImage(this.selectorImg, factor);
                    System.gc();
             } catch (Exception e){System.out.print("Error cargando "+selectorImg+"\n");}
             try {
                 this.backImg = Image.createImage(getClass().getResourceAsStream(backImg));
                 float factor = Resizer.getFactor(this.getHeight(),this.backImg.getHeight(),
                            100);
                 this.backImg = Resizer.resizeImage(this.backImg, factor);
                 System.gc();
               //  this.backImgX = this.getWidth()/2 - this.backImg.getWidth()/2;
               //  this.backImgY = this.getHeight()/2 - this.backImg.getHeight()/2;
             } catch (Exception e){System.out.print("Error cargando "+backImg+"\n");}

             /*comenzamos un nuevo thread*/

             this.startXPos = (this.getWidth() )/ 3;
             /*obtenemos el Y*/
             this.startYPos = (this.getHeight() - (this.options.length * SIZE_OF_STRINGS +
                     (this.options.length-1) * SIZE_BETWEN_OPTIONS_Y))/2;
             /*posicionamos el selector*/
             this.selectorPosX = this.startXPos - SPACE_SELECTOR_STRINGS -
                     this.selectorImg.getWidth()/2;


	 }


	 public void run() {
	        //this.activo=false;         

                  while (this.alive) {
                    repaint();
                    serviceRepaints();

                try {
	                Thread.sleep(5);
	            } catch (InterruptedException e) {
	                System.out.println(e.toString());
	            }
	        }
	    }


         public void keyPressed(int keyCode)
         {
             
             switch (keyCode) {
                 case -1://UP
                     this.decActualPos();
                     break;

                 case -2://DOWN
                     this.incActualPos();
                     break;

                 case 50://UP (KEY_NUM2)
                     this.decActualPos();
                     break;

                 case 56://DOWN (KEY_NUM8)
                     this.incActualPos();
                     break;
                 case 53: //ENTER (KEY_NUM5)
                     this.finishMenu();
                     break;
                 case -5:   //ENTER
                     this.finishMenu();
                     break;
             }
         }


         public void keyReleased (int keyCode) {
         }


         public void paint (Graphics g)
         {
             g.setColor(this.backColor);
             g.fillRect(0,0,getWidth(),getHeight());
             /*graficamos en el siguiente orden:
              * 1) Imagen de fondo.
              * 2) Strings.
              * 3) Imagen Selector
              */
             /*(1)*/
             g.drawImage(this.backImg, this.getWidth()/2, this.getHeight()/2, Graphics.HCENTER|Graphics.VCENTER);

             /*(2)*/
             /*seteamos el color de los strings*/
             g.setColor(this.menuColor);
             for (int i = 0; i < this.options.length; i++) {
                 g.drawString(this.options[i], this.startXPos, this.startYPos + (i*(SIZE_OF_STRINGS+SIZE_BETWEN_OPTIONS_Y)),
                         Graphics.TOP|Graphics.LEFT);
             }

             /*(3)*/
             g.drawImage(this.selectorImg, this.selectorPosX, this.startYPos + (this.actualOps * (SIZE_OF_STRINGS+SIZE_BETWEN_OPTIONS_Y))
                     + this.selectorImg.getHeight()/4, Graphics.VCENTER|Graphics.LEFT);

         }

         public void setAlive (boolean b)
         {
             this.alive = b;
         }

         private void incActualPos ()
         {
             this.actualOps = (this.actualOps + 1) % this.options.length;
         }
         private void decActualPos ()
         {
             if (this.actualOps - 1 < 0) {
                 this.actualOps = this.options.length - 1;
             } else {
                 this.actualOps--;
             }
         }

         private void finishMenu()
         {
             /*seteamos la respuesta*/
             this.response[0] = this.actualOps;
             this.alive = false;    /*finalizamos el thread*/
         }
         private void destroyMenu()
         {
             this.backImg = null;
             this.selectorImg = null;
             this.alive = false;
         }

}
