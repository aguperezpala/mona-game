/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import javax.microedition.media.*;
import javax.microedition.media.control.VolumeControl;

/**
 *
 * @author agustin
 */
public class SoundPlayer {
   
    private  Player p;
    private VolumeControl vc;
    
    public SoundPlayer(String fname)
    {
       	 try {
             
             
             p=Manager.createPlayer(this.getClass().getResourceAsStream(fname), "audio/midi");
	     //p = Manager.createPlayer("file://pattern.mid");
	     p.realize();
            vc=(VolumeControl) p.getControl("VolumeControl");
            vc.setLevel(100); 
	   
	 } catch (Exception ioe) {System.out.print("ERROR********************************\n");
         System.out.print(ioe.toString());
         }
	
     }
    
    public void startMusic()
    {
        try {p.start();}catch(Exception e){};
    }
    
    public void stop()
    {
        try {p.stop();}catch(Exception e){};
    }

    public void setVolume (int v)
    {
        this.vc.setLevel(v);
    }
    
   

}
