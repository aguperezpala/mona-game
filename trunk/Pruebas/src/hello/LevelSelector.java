/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
/**
 *
 * @author agustin
 */
public class LevelSelector {
    public static final int showLevelTime = 5000;   /* mostramos 5 segundos */

    private String imgName;
    private Image map;          /* imagen del mapa */
    public int actualLevel = 0; /* nivel actual */    
    /* los nombres que vamos a mostrar de los lugares */
    private final String levelStr[] = {"Lugar 1", "lugar 2", "lugar 3", "lugar 4"};
    /* almacenamos las coordenadas de las pociciones de los lugares */
    private int levelMark[][] = {{83,98},{107,94},{115,60},{110,125}};
    /* kakaso, ya murio el hecho de "hacer bien las cosas" */
    private Sprite galpon = null;
    private Image caraMona = null;
    private int animSpeed = 0;
    private int caraX = 0, caraY = 0;
    private boolean initialized;

    LevelSelector (String imgName)
    {
        this.imgName = imgName;
        this.initialized = false;
        
    }

    /* Esta funcion va a cargar todas las imagenes, ya que despues en el hide
     * las vamos a liberar (para no tener memoria al dope */
    public void loadLevel (int level, int heigth)
    {
        
        System.gc();
        /* Aca deberiamos hacer un switch para determinar que nivel estamos cargando */
        try {
            //cargamos el mapa
            this.map = Image.createImage(getClass().getResourceAsStream(this.imgName));
            float factor = Resizer.getFactor(heigth, this.map.getHeight(), 100);
            this.map = Resizer.resizeImage(map, factor);
            System.gc();
            /* actualizamos las coordenadas de las marcas */
            if (!this.initialized) {
                for (int i = 0; i < this.levelMark.length; i++) {
                    this.levelMark[i][0] = (int) (this.levelMark[i][0] * factor);
                    this.levelMark[i][1] = (int) (this.levelMark[i][1] * factor);
                }
                this.initialized = true;
            } 
        } catch (Exception e){System.out.print("Error creando sprite "+imgName+"\n");}
        try {
            //cargamos la cara de la mona
            this.caraMona = Image.createImage(getClass().getResourceAsStream("cara_mona.png"));
        } catch (Exception e){System.out.print("Error creando sprite cara de la mona\n");}
        try {
            //cargamos los galpones            
            Image img =  Image.createImage(getClass().getResourceAsStream("galpon.png"));
            this.galpon = new Sprite(img, img.getWidth()/4,img.getHeight());
            this.galpon.defineReferencePixel(this.galpon.getWidth() / 2,
                    this.galpon.getHeight() / 2);
        } catch (Exception e){System.out.print("Error creando sprite cara de la mona\n");}

        /* seteamos la posicion de donde deberia ir la cara de la mona */
        caraX = this.levelMark[level][0] - this.caraMona.getWidth() -
                this.galpon.getWidth()/2;
        caraY = this.levelMark[level][1] - this.galpon.getHeight()/2;

    }

    /* vamos a liberar todas las imagenes y llamar al garbage collector */
    public void hide ()
    {
        this.map = null;
        this.caraMona = null;
        this.galpon = null;
        System.gc();
    }


    public void paint (Graphics g)
    {
        /* dibujamos todo */
        g.drawImage(map, 0, 0, Graphics.TOP | Graphics.LEFT);
        /* ahora dibujamos los galpones y esas cosas */
        this.animSpeed = (this.animSpeed + 1) % 40;
         if (this.animSpeed == 0)
                this.galpon.nextFrame();
        for (int i = 0; i < this.levelMark.length; i++) {           
            this.galpon.setRefPixelPosition(this.levelMark[i][0], this.levelMark[i][1]);
            this.galpon.paint(g);
        }
        /* ahora vamos a dibujar la cara de la mona en el lugar preciso */
        g.drawImage(this.caraMona, caraX, caraY, 0);

    }



}
