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
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;


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
    static final int OP_HELP = 3;        /*:D*/
    static final int OP_EXIT_APP = 4;
    
    private boolean midletPaused = false;

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    //</editor-fold>//GEN-END:|fields|0|

    private Display display;    
    /* opciones que tiene el menu */
    private String opciones[] = {"Iniciar Juego","Opciones", "Elejir Nivel", "Ayuda", "Salir"};
    /* respuesta que vamos a recibir desde el menu */
    private int[] response = {-1};
    private SSCanvas game;      /*canvas game*/
    private Thread tgame;
    private boolean alive = true;
    private String menuSelectorImg = "flecha1.png";
    private String menuBackImg = "portada1.png";
    private boolean isHelp = false;
    
    /**
     * The HelloMIDlet constructor.
     */
    public StartMIDlet() {
         display = Display.getDisplay(this);
         /* Creamos el Juego */
         game = new SSCanvas();
         game.setCommandListener(this);
         /* agregamos el boton de salida del juego */
         exitCommand = new Command("Volver",Command.SCREEN,2);
         game.addCommand(exitCommand);
         

         /* Mostrariamos nuestro logo de la empresa y presentacion, y despues
          * comenzamos con el juego */
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
        /* ejecutamos para mostrar el menu */
        //this.menuInitialize();
       
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user de here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        Presentacion cerink = new Presentacion("logo_trans.png");
        Thread tpres;


        tpres = new Thread(cerink);
        tpres.start();
        //menu.setAlive(true);
        Display.getDisplay(this).setCurrent(cerink);
        
        try { tpres.join();} catch (Exception e) {
            System.out.print("Error PRESENTACION\n");
        }
        tpres = null;        
        this.game();
        /* aca mostramos el logo de nuestra empresa */
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

    private void menuInitialize ()
    {     

         this.menuBackImg = null;
         this.menuSelectorImg = null;
         System.gc();
         menuSelectorImg = "flecha1.png";
         menuBackImg = "portada1.png";

         int op = startMenu();
         
         // write pre-initialize user code here
        

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
                    /* Configuramos la musica */
                    this.opciones = null;
                    System.gc();
                    this.opciones = new String[2];
                    this.opciones[0] = "Desactivar Sonido";
                    this.opciones[1] = "Activar Sonido";
                    op = this.startMenu();
                    if (op == 0)
                        game.setMusicOff();
                    else
                        game.setMusicOn();
                    break;

                case OP_CHOOSE_LEVEL:
                    this.opciones = null;
                    System.gc();
                    /* aca deberiamos determinar si es el comienzo del juego o no */
                    if (game.firstRun) {
                        this.opciones = new String[4];
                        this.opciones[0] = "Nivel 1";
                        this.opciones[1] = "Nivel 2";
                        this.opciones[2] = "Nivel 3";
                        this.opciones[3] = "Nivel 4";
                        op = this.startMenu();
                        /* seteamos el nivel seleccionado */
                        game.setLevel(op);
                    }
                    break;

                case OP_HELP:
                    this.menuBackImg = null;
                    this.menuSelectorImg = null;
                    this.opciones = null;
                    this.isHelp = true;
                    System.gc();
                    Form form = new Form("Creditos");
                    form.addCommand(exitCommand);
                    form.setCommandListener(this);
                    form.append("El juego consiste en apretar lo antes posible " +
                            "las combinaciones de flechas. Para lograr esto se pueden " +
                            "utilizar tanto el joystick del celular como asi las teclas " +
                            "4 (izquierda), 6 (derecha), 2 (arriba), 8 (abajo)") ;
                  
                    this.display.setCurrent(form);
                    /* ahora esperamos de forma cacasa que hayan salido de la ayuda
                     * por dios, que hdp! KA KA SO */
                    while (this.isHelp){
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {System.out.println(e.toString());}
                    }

                    break;

                case OP_EXIT_APP:
                    this.alive = false;
                    break;
            }

        }
        this.opciones = null;
        System.gc();
        this.opciones = new String[5];
        this.opciones[0] = "Iniciar Juego";
        this.opciones[1] = "Opciones";
        this.opciones[2] = "Elejir Nivel";
        this.opciones[3] = "Creditos";
        this.opciones[4] = "Salir";


        // write post-initialize user code here
    }


    
    public void commandAction(Command c, Displayable s) {
    	 if (c == exitCommand) {
             if (this.isHelp)
                 this.isHelp = false;
             else
                this.pauseApp();             
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
        game.GamePause(true);        
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
        Menu menu = new Menu(opciones, menuBackImg, menuSelectorImg ,0,0xFFFFFFFF, response);
        Thread tmenu;

        
        tmenu = new Thread(menu);
        tmenu.start();
        //menu.setAlive(true);        
        Display.getDisplay(this).setCurrent(menu);
        /* vamos a setear la respuesta en -1 */
        this.response[0] = -1;
        
        /* ahora vamos a esperar que se elija una opcion */
      
        try { tmenu.join();} catch (Exception e) {
            System.out.print("Error al esperar tmenu \n");
        }
        tmenu = null;
        
        return this.response[0];
    }


    private void game ()
    {        
        while (this.alive)
        {
            try {tgame.join();} catch (Exception e){}
            this.menuInitialize();            
        }
        this.game = null;
        System.gc();
        this.destroyApp(true);
    }

    private void startGame()
    {
        /* recolectamos basura */
        this.tgame = null;
        System.gc();
        game.GamePause(false);        
        tgame = new Thread (game);
        tgame.start();
        display.setCurrent(game);        
    }

}
