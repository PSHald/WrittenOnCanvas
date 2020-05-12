package org.example;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GraphicalUserInterface extends Canvas3D {
    //Window options
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    Dimension _winSize = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    static {
        System.setProperty("sun.awt.noerasebackground", "true");
    }

    static private ArrayList<Icon> buttons = new ArrayList<>();

    public  enum ImageKeys{
        Brunsviger
    }


    public GraphicalUserInterface(GraphicsConfiguration graphicsConfiguration) {
        super(graphicsConfiguration);
        setPreferredSize(_winSize);
    }

    public GraphicalUserInterface(GraphicsConfiguration graphicsConfiguration, boolean offScreen) {
        super(graphicsConfiguration, offScreen);
    }


    private void initializeButton(String name, int x, int y) {
        Icon button = new Icon(name, x, y);
        if(!buttons.contains(button.Name)){
            buttons.add(button);
            if((getSelectedButton()==null))button.selected = false;
        }
    }

    //draw 2D items (text + icons)
    public void drawHUD(J3DGraphics2D graphics2d, Dimension dims) {

        this.drawIcons(graphics2d,dims);

        graphics2d.flush(false);

    }

    private void drawIcons(J3DGraphics2D g, Dimension canvasDim) {
        for(Icon button : buttons){
            button.draw(g, canvasDim);
        }
    }



    private Icon getSelectedButton() {
        Icon retVal=null;
        for(Icon button : buttons){
            if(button.selected)retVal=button;
        }
        return retVal;
    }



    public void initHUD() {
        ArrayList<String> nameList = new ArrayList<>(
                Arrays.asList("Brunsviger")
        );
        int step=100;
        int posHorizontal= 0;//10000-step*hudicons;//HUD at center

        int posVertical = getX(); // Max værdi er bunden, 0 er toppen
        for (String name : nameList) {
            if(name != ""){
                initializeButton(name, posHorizontal+=step, 100);
            }
            else posHorizontal+=step;
        }
    }

    public ImageKeys getHUDaction(int x, int y) {
        ImageKeys retVal=null;
        Icon prevSelected=getSelectedButton();
        for(Icon button : buttons){
            if(button.hitTest(x,y)){
                retVal=ImageKeys.valueOf(button.Name);
                if((button.selectable)){
                    if(prevSelected!=null)prevSelected.selected = false;
                    button.selected=true;
                }
            }
        }
        return retVal;
    }

    public boolean HitTest(int x, int y) {
        ImageKeys thisButton = getHUDaction(x,y);
        if (thisButton==null)return false;
        return true;
    }
}
