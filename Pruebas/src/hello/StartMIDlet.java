/*
 * QUE DEBEMOS HACER:
 * Terminar de completar el tema de las opciones del menu en la funcion
 * inicialize().
 * Terminar de revisar las marcas (lineas marcadas) y terminar de probar el
 * juego (SSCanvas game).
 * Fijarse en game.GamePause(false) que lo que hace, reanimar a todos los elementos
 * 
 */

package hello;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


/**
 * @author agustin
 */
public class StartMIDlet extends MIDlet implements CommandListener {

    /*Aca vamos a declarar las variables globales que corresponden cada una a
     * la posicion del arreglo opciones. TENER EN CUENTA CUANDO SE MODIFICA EL
     * ARREGLO "opciones", en la posicion n-1.
     */ 
    
    static final int OP_START_GAME = 0;     /*RESUME o START*/
    static final int OP_OPTIONS = 1;
    static final int OP_CHOOSE_LEVEL = 2;
    static final int OP_CREDITS = 3;        /*:D*/
    static final int OP_EXIT_APP = 4;
    
    private boolean midletPaused = false;

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    //</editor-fold>//GEN-END:|fields|0|

    private Display display;
    private Thread tmenu;
    private String opciones[] = {"Iniciar Juego","Opciones", "Elejir Nivel", "Creditos", "Salir"};
    private int[] response = {-1};
    private Menu menu;
    private SSCanvas game;      /*canvas game*/
    private Thread tgame;
    
    /**
     * The HelloMIDlet constructor.
     */
    public StartMIDlet() {
         display=Display.getDisplay(this);
         
	 exitCommand = new Command("Salir",Command.SCREEN,2);
         
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
        int op = startMenu();

        /*deberiamos tener en cuenta cuando borrar el menu*/
        if (op < 0)
            System.out.print("Error al iniciar el menu, error:"+op+"\n");
        else {
            /*trabajamos segun opcion seleccionada*/
            switch (op) {
                case OP_START_GAME:
                    /*debemos empezar el juego o en caso de que este pausado,
                     * resumirlo*/
                    startGame();
                    break;
                
                case OP_OPTIONS:
                    this.destroyApp(true);
                    break;
                
                case OP_CHOOSE_LEVEL:
                    this.destroyApp(true);
                    break;
                      
                case OP_CREDITS:
                    this.destroyApp(true);
                    break;
                    
                case OP_EXIT_APP:
                    this.destroyApp(true);
                    break;
            }
            
        }
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



    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return exitCommand;
    }
    //</editor-fold>//GEN-END:|18-getter|2|

    public void commandAction(Command c, Displayable s) {
    	 if (c == exitCommand) {             
	  menu.setAlive(false);
          System.out.print("TERMINO COMMAND = ExitCommand\n");
          this.destroyApp(true);
	 }    
    }
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
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
        this.notifyDestroyed();
    }


    /**************************************************************************/
    /*Esta funcion ya debe tener pre-establecido las opciones (array de strings)
     *del menu. Queda esperando hasta que una opcion sea elejida
     * RETURNS:
     *         >= 0 (opcion elejida del menu, correspondiente al array opciones)
     *         <  0 caso de error.
    */
    private int startMenu ()
    {
        /*creamos el menu*/
        this.menu = new Menu(opciones,"bien_ahi.png", "flecha1.png",0,0xFFFFFFFF, response);        
        tmenu = new Thread(menu);
        tmenu.start();
        display.setCurrent(menu);
        try {
            tmenu.join();
        } catch (Exception e){System.out.print("Error al esperar tmenu \n");}

        return this.response[0];

    }
    private void startGame()
    {
        /*chequeamos si esta en pausa o no*/
        if (this.midletPaused) {
            /*debemos hacer un resume*/
            if (game == null)
                System.out.print("Error, intentando resumir game\n");
            else {
                /*lo mostramos en pantalla al game*/
                display.setCurrent(game);
                game.GamePause(false);                
            }
        } else {
            System.out.print("Estamos creando el juego\n");
            game = new SSCanvas();
            game.setCommandListener(this);
            tgame = new Thread (game);
            tgame.start();
            /*lo mostramos en pantalla al game*/
            display.setCurrent(game);
        }
    }

}
