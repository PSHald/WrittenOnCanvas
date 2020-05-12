package org.example;

import javax.imageio.ImageIO;
import javax.media.j3d.J3DGraphics2D;
import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import java.util.Random;

public class Icon {
    public String Name;
    public Image image;
    public Image selectedImage;
    public boolean isShowing;
    public boolean selectable=false;
    public boolean selected=false;

    public Rectangle dim,hitdim;



    public Icon(String name, int x, int y) {
        Name = name;
        isShowing = true;
        addImage();
        this.image=Resize(setTransparency(image, Color.WHITE),200);
        this.selectedImage=Resize(recolorColor(image, Color.WHITE),200);
        this.dim=new Rectangle(-1000,-1000,200,200);
        this.hitdim=new Rectangle(-1000,-1000,200,200);
        init(x, y, true);
    }
    public boolean hitTest(int x,int y)
    {
        boolean retVal=true;
        if ((x<hitdim.x)||(x>hitdim.x+hitdim.width))retVal= false;
        if ((y<hitdim.y)||(y>hitdim.y+hitdim.height))retVal= false;

        if(retVal);

        return retVal;
    }

    private void addImage(){
        String fileName=Name+".png";
        try {
            System.out.println(fileName);
            BufferedImage buffImage= ImageIO.read(getClass().getResource("/icons/"+fileName));

            image = buffImage;
        }catch (Exception ex) {ex.printStackTrace();}
    }

    public void draw(J3DGraphics2D g, Dimension canvasDim) {
        if(!isShowing) return;

        int x=this.dim.x;
        int y=this.dim.y;
        int w=this.dim.width;
        int h=this.dim.height;
        if (x<0) x=canvasDim.width+x-w;
        if (y<0) y=canvasDim.height+y-h;
        if (x>=9000)x=canvasDim.width/2 + x-10000;
        if (y>=9000)y=canvasDim.height/2 + x-10000;
        this.hitdim.x=x;
        this.hitdim.y=y;
        g.drawImage(selected?selectedImage :image,  x,  y, null);

    }

    private void init(int x, int y, boolean selectable) {
        this.selectable=selectable;
        dim.x=x;
        dim.y=y;
    }

    public Image Resize(Image orig, int sizeWidth) {
        Image ret=orig.getScaledInstance(sizeWidth, -1, Image.SCALE_SMOOTH);
        return ret;
    }

    public Image setTransparency(Image im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }


    public Image recolorColor(Image im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return Color.lightGray.getRGB();
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}
