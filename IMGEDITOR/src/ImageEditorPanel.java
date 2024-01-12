import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ImageEditorPanel extends JPanel implements KeyListener{

    Color[][] pixels;
    
    public ImageEditorPanel() {
        BufferedImage imageIn = null;
        try {
            // the image should be in the main project folder, not in \src or \bin
            imageIn = ImageIO.read(new File("flower.jpg"));
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        pixels = makeColorArray(imageIn);
        setPreferredSize(new Dimension(pixels[0].length, pixels.length));
        setBackground(Color.BLACK);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        // paints the array pixels onto the screen
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                g.setColor(pixels[row][col]);
                g.fillRect(col, row, 1, 1);
            }
        }
    }
    
    public Color[][] flipH (Color[][] arr){
        Color[][] newArr = new Color[arr.length][arr[0].length];
        for (int r = 0; r<newArr.length;r++){
            for (int c = 0; c<newArr[r].length; c++){
                newArr[r][(arr[0].length-1)-c] = arr[r][c];
            }
        }
        return newArr;
    }

    public Color[][] flipV (Color[][] arr){
        Color[][] newArr = new Color[arr.length][arr[0].length];
        for (int r = 0; r<newArr.length;r++){
            for (int c = 0; c<newArr[r].length; c++){
                newArr[(arr.length-1)-r][c] = arr[r][c];
            }
        }
        return newArr;
    }

    public Color[][] gray (Color[][] arr){
        Color[][] newArr = new Color[arr.length][arr[0].length];
        for (int r = 0; r<newArr.length;r++){
            for (int c = 0; c<newArr[r].length; c++){
               Color myColor = arr[r][c];
               int red = myColor.getRed();
               int green = myColor.getGreen();
               int blue = myColor.getBlue();
               int avg = (red+green+blue)/3;
               newArr[r][c] = new Color(avg,avg,avg);
            }
        }
        return newArr;
    }

    public Color[][] brighten (Color[][]arr){
        final double SCALE = 1.1;
        Color[][] newArr = new Color[arr.length][arr[0].length];
        for (int r = 0; r<newArr.length;r++){
            for (int c = 0; c<newArr[r].length; c++){
                Color myColor = arr[r][c];
                int red = myColor.getRed();
                int green = myColor.getGreen();
                int blue = myColor.getBlue();
                if ((int)(red*SCALE)>255){
                        red = 255;
                } else{
                    red = (int)(red*SCALE);
                }
                if ((int)(green*SCALE)>255){
                        green = 255;
                } else{
                    green = (int)(green*SCALE);
                }
                if ((int)(blue*SCALE)>255){
                        blue = 255;
                } else{
                    blue = (int)(blue*SCALE);
                }
                newArr[r][c] = new Color(red,green,blue);
            }
        }
        return newArr;
    }

    public Color[][] contrast (Color[][]arr){
        final int DIVIDER = 127;
        final double DARK_SCALE = 1.5;
        final double LIGHT_SCALE = 0.5;
        Color[][] newArr = new Color[arr.length][arr[0].length];
        for (int r = 0; r<newArr.length;r++){
            for (int c = 0; c<newArr[r].length; c++){
                Color myColor = arr[r][c];
                int red = myColor.getRed();
                int green = myColor.getGreen();
                int blue = myColor.getBlue();
                if (red>DIVIDER){
                    if ((int)(red*DARK_SCALE)>255){
                        red = 255;
                    } else{
                        red = (int)(red*DARK_SCALE);
                    }
                } else {
                    red = (int)(red*LIGHT_SCALE);
                }
                if (green>DIVIDER){
                    if ((int)(green*DARK_SCALE)>255){
                        green = 255;
                    } else{
                        green = (int)(green*DARK_SCALE);
                    }
                } else {
                    green = (int)(green*LIGHT_SCALE);
                }
                if (blue>DIVIDER){
                    if ((int)(blue*DARK_SCALE)>255){
                        blue = 255;
                    } else{
                        blue = (int)(blue*DARK_SCALE);
                    }
                } else {
                    blue = (int)(blue*LIGHT_SCALE);
                }
                newArr[r][c] = new Color(red,green,blue);
            }
        }
        return newArr;
    }

    public Color[][] vintage (Color[][]arr){
        final double SCALE = 1.3;
        Color[][] newArr = new Color[arr.length][arr[0].length];
        for (int r = 0; r<newArr.length;r++){
            for (int c = 0; c<newArr[r].length; c++){
                Color myColor = arr[r][c];
                int red = myColor.getRed();
                int green = myColor.getGreen();
                int blue = myColor.getBlue();
                if ((int)(red*SCALE)>255){
                        red = 255;
                } else{
                    red = (int)(red*SCALE);
                }
                if ((int)(green*SCALE)>255){
                        green = 255;
                } else{
                    green = (int)(green*SCALE);
                }
                newArr[r][c] = new Color(red,green,blue);
            }
        }
        return newArr;
    }

    public Color[][] blur (Color[][] arr){
        Color[][] newArr = new Color[arr.length][arr[0].length];
        for (int r = 0; r<newArr.length;r++){
            for (int c = 0; c<newArr[r].length; c++){
                final int RADIUS = 3;
                int red = 0;
                int green = 0;
                int blue = 0;
                int count = 0;
                for (int i = r-RADIUS; i<r+RADIUS;i++){
                    for (int j = c-RADIUS; j<c+RADIUS;j++){
                        if (onScreen(i, j, newArr)){
                            Color myColor = arr[i][j];
                            red += myColor.getRed();
                            green += myColor.getGreen();
                            blue += myColor.getBlue();
                            count++;
                        }
                    }
                }
                newArr[r][c] = new Color(red/count,green/count,blue/count);
            }
        }
        return newArr;
    }

    public static boolean onScreen (int i, int j, Color[][]arr){
        if (i>0 && i<arr.length && j>0 && j<arr[0].length){
            return true;
        } else{
            return false;
        }
    }

    public Color[][] makeColorArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Color[][] result = new Color[height][width];
        
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(image.getRGB(col, row), true);
                result[row][col] = c;
            }
        }
        // System.out.println("Loaded image: width: " +width + " height: " + height);
        return result;
    }
    @Override
    public void keyTyped(KeyEvent e){
        //contrast
        if (e.getKeyChar() == 'c'){
            pixels = contrast(pixels);
        }
        //grayscale
        if (e.getKeyChar() == 'g'){
            pixels = gray(pixels);
        }
        //vintage
        if (e.getKeyChar() == 'v'){
            pixels = vintage(pixels);
        }
        //brighten
        if (e.getKeyChar() == 'b'){
            pixels = brighten(pixels);
        }
        //make blurry
        if (e.getKeyChar() == 'm'){
            pixels = blur(pixels);
        }
        repaint();
    }
    @Override
    public void keyPressed(KeyEvent e){
        //unused
    }
    @Override
    public void keyReleased(KeyEvent e){
        //unused
    }

}
