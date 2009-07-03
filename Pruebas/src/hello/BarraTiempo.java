/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author agustin
 */
public class BarraTiempo {
    private int fromCoord[] = {0,0};
    private int spaceSize[] = {0,0};
    
    /* tiempo total de la barra */
    public int totalTime = 1;
    /* tiempo actual, esto es para poder hacer el calculo del tamaño de la barra */
    public int actualTime = 1;

    public BarraTiempo (int fromX, int fromY, int toX, int toY)
    {
        this.fromCoord[0] = fromX;
        this.fromCoord[1] = fromY;
        this.spaceSize[0] = toX - fromX;
        this.spaceSize[1] = toY - fromY;
        
    }

    public void paint (Graphics g)
    {
        int factor = (int) (actualTime * spaceSize[1] / totalTime);
        int posY = spaceSize[1] - factor + fromCoord[1];
        int color = g.getColor();

        //g.setColor(255, 0, 0);
        g.fillRect(fromCoord[0], posY, spaceSize[0], factor);
        g.setColor(0x00FFFFFF);
        g.drawRect(fromCoord[0]-1, posY - 1, spaceSize[0] + 2, factor+2);
        g.setColor(color);
    }

    

}
