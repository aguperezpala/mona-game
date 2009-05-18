/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import javax.microedition.lcdui.*;
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
    private final int levelMark[][] = {{15,15},{30,20},{100,7},{60,50}};

    LevelSelector (String imgName)
    {
        this.imgName = imgName;
    }

    /* Esta funcion va a cargar todas las imagenes, ya que despues en el hide
     * las vamos a liberar (para no tener memoria al dope */
    public void loadLevel (int level)
    {
        System.gc();
        /* Aca deberiamos hacer un switch para determinar que nivel estamos cargando */
        try {
            //cargamos la imagen
            this.map = Image.createImage(getClass().getResourceAsStream(this.imgName));
            //this.map = Resizer.resizeImage(map, 100);
        } catch (Exception e){System.out.print("Error creando sprite "+imgName+"\n");}
    }

    /* vamos a liberar todas las imagenes y llamar al garbage collector */
    public void hide ()
    {
        this.map = null;
        /* Debemos completar con todas las demas imagenes/sprites */

        System.gc();
    }


    public void paint (Graphics g)
    {
        /* dibujamos todo */
        g.drawImage(map, 0, 0, Graphics.TOP | Graphics.LEFT);
    }



}
