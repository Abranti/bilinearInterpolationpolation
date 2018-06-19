import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class main {
		
	public static void main(String[] args) throws IOException {
		//File image = new File("C:\\Users\\Cosmos\\Desktop\\java.png");
		File image = new File("C:\\Users\\Cosmos\\Desktop\\teste_2.png");
		BufferedImage bufferedImage = ImageIO.read(image);
		
		int ctrl = 0;
		int newWidth = bufferedImage.getWidth() * 5;
		int newHeight = bufferedImage.getHeight() * 5;
		
		int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
		
		for(int x=0; x < bufferedImage.getWidth(); x++) {
			for(int y=0; y < bufferedImage.getHeight(); y++) {
				pixels[ctrl] = bufferedImage.getRGB(x, y);
				ctrl++;
			}
		}
		
		int[] resizedpixels = resizeBilinear(pixels, bufferedImage.getWidth(), bufferedImage.getHeight(), newWidth, newHeight);
		
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		ctrl = 0;
		
		for(int x=0; x < newWidth; x++) {
			for(int y=0; y < newHeight; y++) {
				resizedImage.setRGB(x, y, resizedpixels[ctrl]);
				ctrl++;
			}
		}
		
		//ImageIO.write(resizedImage, "png", new File("C:\\Users\\Cosmos\\Desktop\\java_redimencionado.png"));
		ImageIO.write(resizedImage, "png", new File("C:\\Users\\Cosmos\\Desktop\\java_redimencionado.png"));
		
	}

	/**
	 * Bilinear resize ARGB image.
	 * pixels is an array of size w * h.
	 * Target dimension is w2 * h2.
	 * w2 * h2 cannot be zero.
	 * 
	 * @param int[] Pixels da imagem. Array com 50.400 inteiros representando os pixels em RGB
	 * @param width Largura imagem.
	 * @param height Altura da imagem.
	 * @param newWidth Nova largura da imagem.
	 * @param newHeight Nova altura da imagem.
	 * @return Novo array com tamanho w2 * h2 contendo os pixels interpolados.
	 */
	public static int[] resizeBilinear(int[] pixels, int width, int height, int newWidth, int newHeight) {
		// Array temporário para guardar o resultado da interpolação
		// Tamanho 50.400
	    int[] temp = new int[newWidth*newHeight];
	    
	    // Variáveis 
	    int a, b, c, d, x, y, index;
	    float x_diff, y_diff, blue, red, green;
	    // offset inicializado com 0
	    int offset = 0;
	    // x_ratio iniciado com valor da largura -1 dividido pelo novo valor da largura
	    // x_ratio = (300 -1) / 600 = 0.4983333...;
	    float x_ratio = ((float)(width-1))/newWidth;
	    // y_ratio iniciado com valor da altura -1 dividido pelo novo valor da altura
	    // x_ratio = (168 -1) / 336 = 0.49702380952;
	    float y_ratio = ((float)(height-1))/newHeight;
	    // Iterador de altura
	    for (int i=0;i<newHeight;i++) {
	    	// Iterador de largura
	        for (int j=0;j<newWidth;j++) {
	            x = (int)(x_ratio * j);
	            y = (int)(y_ratio * i);
	            x_diff = (x_ratio * j) - x;
	            y_diff = (y_ratio * i) - y;
	            index = (y*width+x);                
	            a = pixels[index];
	            b = pixels[index+1];
	            c = pixels[index+width];
	            d = pixels[index+width+1];

	            // Elemnto azul do pixel
	            // Yb = Ab(1-w)(1-h) + Bb(w)(1-h) + Cb(h)(1-w) + Db(wh)
	            blue = (a&0xff)*(1-x_diff)*(1-y_diff) + (b&0xff)*(x_diff)*(1-y_diff) +
	                   (c&0xff)*(y_diff)*(1-x_diff)   + (d&0xff)*(x_diff*y_diff);

	            // Elemento verde do pixel
	            // Yg = Ag(1-w)(1-h) + Bg(w)(1-h) + Cg(h)(1-w) + Dg(wh)
	            green = ((a>>8)&0xff)*(1-x_diff)*(1-y_diff) + ((b>>8)&0xff)*(x_diff)*(1-y_diff) +
	                    ((c>>8)&0xff)*(y_diff)*(1-x_diff)   + ((d>>8)&0xff)*(x_diff*y_diff);
	            

	            // red element
	            // Yr = Ar(1-w)(1-h) + Br(w)(1-h) + Cr(h)(1-w) + Dr(wh)
	            red = ((a>>16)&0xff)*(1-x_diff)*(1-y_diff) + ((b>>16)&0xff)*(x_diff)*(1-y_diff) +
	                  ((c>>16)&0xff)*(y_diff)*(1-x_diff)   + ((d>>16)&0xff)*(x_diff*y_diff);

	            //Insere os pixels interpolados e os originais em uma nova lista de pixels
	            temp[offset++] = 0xff000000 | // hardcode alpha
				                ((((int)red)<<16)&0xff0000) |
				                ((((int)green)<<8)&0xff00) |
				                ((int)blue);
	                    
	        }
	    }
	    return temp;
	}
}