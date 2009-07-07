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
    private int volume;
    
    public SoundPlayer(String fname)
    {
        try {
            p = Manager.createPlayer(this.getClass().getResourceAsStream(fname), "audio/midi");
	        p.realize();
            vc = (VolumeControl) p.getControl("VolumeControl");
            vc.setLevel(100);
        } catch (Exception ioe) {
            System.out.print(ioe.toString());
        }

    }

    public void setMusic (String fname)
    {
        /* si hay uno lo frenamos */
        if (p != null) {
            this.stop();
            p.close();
            p = null;
            System.gc();
        }
        try {
            p = Manager.createPlayer(this.getClass().getResourceAsStream(fname), "audio/midi");
	        p.realize();
            vc = (VolumeControl) p.getControl("VolumeControl");
            vc.setLevel(this.volume);
        } catch (Exception ioe) {
            System.out.print(ioe.toString());
        }
    }
    public long getDuration ()
    {
        return this.p.getDuration();
    }
    public long getActualTime ()
    {
        return this.p.getMediaTime();
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
        this.volume = v;
        this.vc.setLevel(v);
    }
    
   

}
