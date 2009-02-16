/*
 * Personaje.
 * ! Faltaria hacer las secuencias de animaciones y la cantidad de animaciones !
 * Funciones:
 *  void set_pos (int x, int y);
 *  int get_xPos ();
 *  int get_yPos ();
 * 
 *  void set_pixRefPos (int x, int y);
 *  void set_pixRef (int x, int y);
 *  Sprite get_sprite(void);
 *  int add_sequence(int s[]);  //retorna el numero de animacion
 *
 *  void set_transformation (int transform);
 *  void set_visible (boolean b);
 *  boolean is_visible();
 *  void set_alive (boolean b);
 *  boolean is_alive();
 *
 */

package hello;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
/**
 *
 * @author agustin
 */
public class Personaje implements Runnable {

    private Sprite sprite;
    private Image image;
    private boolean live = true;
    private int scale = 100;      //esto es segun el tamaño de la imagen Y PANTALLA
      
    private Thread tpers;

    
    
    public Personaje (String nomImg, int px, int py,int width, int height, int scale)
    {
        int nx, ny; //numero de cuadros en X y en Y
        this.scale = scale;
      
        try {
            //cargamos la imagen
            image = Image.createImage(getClass().getResourceAsStream(nomImg));
            //image = Image.createImage("/hello/tira_mona_paso1.png");
       
            /*System.out.print("fw: "+ ((width * scale)/100)*5);
            System.out.print("  fh: "+ ((height * scale)/100) );
            System.out.print("  iw: "+ ((image.getWidth() * scale)/100) );
            System.out.print("  ih: "+ ((image.getHeight() * scale)/100) + "\n" );
            */
            if (scale != 100) {  //entonces hay que modificar la imagen
                int ix, iy; 
                //calculamos cantidad de cuadros en filas y columnas
                nx = image.getWidth() / width;
                ny = image.getHeight() / height;
                
                ix = ((scale * px)/100)*nx;
                iy = ((scale * py)/100)*ny;
                //MODIFICAR cuanto se amplia en y con respecto a X (proporcionalmente)



 /*   //Need an array (for RGB, with the size of original image)
   int rgb[] = new int[image.getWidth()*image.getHeight()];
   //Get the RGB array of image into "rgb"
   image.getRGB(rgb,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
  //Call to our function and obtain RGB2
  
   int rgb2[] = reescalaArray(rgb,image.getWidth(),image.getHeight(),ix,iy);
  rgb = null;
  //Create an image with that RGB array
  image = Image.createRGBImage(rgb2,ix,iy,true);  

*/

               image = this.resizeImage(image, ix, iy);
               
                
            }
            //this.sprite = new Sprite(image, 50, 75);

            //creamos el sprite con las nuevas medidas
            this.sprite = new Sprite(image, (px * scale)/100, (py * scale)/100);
            //GameDesign p = new GameDesign();
            //this.sprite = p.getPas1();

            //seteamos el pixel de referencia justo en el medio de la imagen
            this.set_pixRef(px * scale / 200, py * scale / 200);
            
            this.tpers=new Thread(this);
            tpers.start();

        } catch (Exception e){System.out.print("Error creando sprite");}
        
    }

    public void set_alive (boolean b)
    {
        if (b == true) {
            if (!this.live) {
                this.tpers=new Thread(this);
                tpers.start();  //chequear esto
                this.live = true;
            }
        } else {
            this.live = false;
        }
    }
    public boolean is_alive ()
    {
        return this.live;
    }

    public void set_visible (boolean b)
    {
        this.sprite.setVisible(b);
    }
    public boolean is_visible ()
    {
        return this.sprite.isVisible();
    }

    public Sprite getSprite()
    {
        return this.sprite;
    }
    
    public void set_pixRefPos (int x, int y)
    {
        this.sprite.setRefPixelPosition(x, y);
    }
    
    public void set_pixRef (int x, int y)
    {
        this.sprite.defineReferencePixel(x, y);
    }

    public void set_pos (int x, int y)
    {
        //this.sprite.setPosition(x, y);
         this.sprite.setRefPixelPosition(x, y);
    }
    
    public int get_xPos ()
    {
        return this.sprite.getX();
    }
    
    public int get_yPos ()
    {
        return this.sprite.getY();
    }

    //para esta funcion usar get_sprite().TIPO_TRANSF
    public void set_transformation (int t)
    {
        this.sprite.setTransform(t);
    }
    
    
    
    
    
    public void run () {       
        while (this.live) {
            this.sprite.nextFrame();            
            
            try {
                Thread.sleep(70);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
  
    /*****************   ADICONALES   ****************************************/
    
     private Image resizeImage(Image src,int width,int height) {
      int srcWidth = src.getWidth();
      int srcHeight = src.getHeight();
      Image tmp = Image.createImage(width, srcHeight);
      Graphics g = tmp.getGraphics();
      int ratio = (srcWidth << 16) / width;
      int pos = ratio/2;

      //Horizontal Resize
       g.setColor(0, 0, 0);
       g.fillRect(0,0,width,srcHeight);
       

      for (int x = 0; x < width; x++) {
          g.setClip(x, 0, 1, srcHeight);
          g.drawImage(src, x - (pos >> 16), 0, Graphics.LEFT | Graphics.TOP);
          pos += ratio;
      }

      Image resizedImage = Image.createImage(width, height);
      g = resizedImage.getGraphics();
      ratio = (srcHeight << 16) / height;
      pos = ratio/2;

      //Vertical resize
       g.setColor(0, 0, 0);
       g.fillRect(0,0,width,height);

      for (int y = 0; y < height; y++) {
          g.setClip(0, y, width, 1);
          g.drawImage(tmp, 0, y - (pos >> 16), Graphics.LEFT | Graphics.TOP);
          pos += ratio;
      }
      return resizedImage;

  }//resize image

         
  
    private int[] reescalaArray(int[] ini, int x, int y, int x2, int y2) {
    int out[] = new int[x2*y2];
    int dy, dx;
    for (int yy = 0; yy < y2; yy++) {
    dy = yy * y / y2;
    for (int xx = 0; xx < x2; xx++) {
    dx = xx * x / x2;
    out[(x2*yy)+xx]=ini[(x*dy)+dx];
    }
    }
        return out;  
    }

     private int resizeScale (int s) //obtenemos una verdadera escala
     {
         return 3;
     }
    
}
