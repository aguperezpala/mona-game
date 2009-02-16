package hello;


import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.*;

public class Resizer {

    
     static Image resizeImage(Image src, float factor) {
      return resizeImage(src, (int) (src.getWidth() * factor), (int) (src.getHeight() * factor));
      }//resize image

     static Image resizeImage(Image src,int width,int height) {
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
     
     //Esta funcion toma o el ancho o el alto tanto de la pantalla como la imagen
    static float getFactor (int screenSize, int imageSize, int scale)
    {        
        return (float) ((float)(scale * screenSize)/(float)(100 * imageSize));        
    }
    
  

}
