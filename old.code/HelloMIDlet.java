/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author agustin
 */
public class HelloMIDlet extends MIDlet implements CommandListener {

  private boolean midletPaused = false;

  //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
  private Command backCommand;
  private Image image;
  //</editor-fold>//GEN-END:|fields|0|
    private Command exitCommand;
    private Form form;
    private StringItem stringItem;
    private SSCanvas screen;
    private Display display;
    /**
     * The HekkoMIDlet constructor.
     */
    public HelloMIDlet() {
         display=Display.getDisplay(this);
         
	 exitCommand = new Command("Salir",Command.SCREEN,2);
         //Connection aguc;//=Connector.open("file:/root1/Readme.txt", Connector.READ);
	 screen=new SSCanvas();
	 screen.addCommand(exitCommand);
         screen.setCommandListener(this);
	 
         
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
        display.setCurrent(screen);
        new Thread(screen).start();
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
        display.setCurrent(screen);
        new Thread(screen).start();
        System.out.print("SE RESUMIO\n");
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|







    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|16-getter|0|16-preInit
    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|16-getter|0|16-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|16-getter|1|16-postInit
            // write post-init user code here
        }//GEN-BEGIN:|16-getter|2|
        return backCommand;
    }
    //</editor-fold>//GEN-END:|16-getter|2|



public void commandAction(Command c, Displayable s) {
    	 if (c == exitCommand) {             
	  destroyApp(false);
	  notifyDestroyed();
	 }
         
	 
}

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: image ">//GEN-BEGIN:|27-getter|0|27-preInit
/**
 * Returns an initiliazed instance of image component.
 * @return the initialized component instance
 */
public Image getImage() {
    if (image == null) {//GEN-END:|27-getter|0|27-preInit
            // write pre-init user code here
        try {//GEN-BEGIN:|27-getter|1|27-@java.io.IOException
            image = Image.createImage("/hello/golpe.png");
        } catch (java.io.IOException e) {//GEN-END:|27-getter|1|27-@java.io.IOException
                e.printStackTrace();
        }//GEN-LINE:|27-getter|2|27-postInit
            // write post-init user code here
    }//GEN-BEGIN:|27-getter|3|
    return image;
}
//</editor-fold>//GEN-END:|27-getter|3|



    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay () {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable (null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet ();
        } else {
            initialize ();
            startMIDlet ();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
        screen.GamePause(true);
        System.out.print("SE PUSO EN PAUSA\n");
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

}
