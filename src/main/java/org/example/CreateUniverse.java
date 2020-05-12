package org.example;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class CreateUniverse extends JFrame {
    static {
        System.setProperty("sun.awt.noerasebackground", "true");
    }
    GraphicalUserInterface canvas;

    private static Point3d GazePoint = new Point3d(0,1,2);
    private static Point3d ViewerLocation = new Point3d(1,1,1);

    TransformGroup transformGroup = new TransformGroup();
    Transform3D transform = new Transform3D();

    private static final float ZNEAR = 0.001f;                      // Near Clipping plane distance [meters]
    private static final float ZFAR = 10.0f;                        // Far Clipping plane distance [meters]
    private static final float FOV = (float) (Math.PI/4.0);

    public CreateUniverse(){
//1. Create a Canvas3D object and add it to the Applet panel.
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new GraphicalUserInterface(config)
        {
            public void postRender(){
                canvas.drawHUD(this.getGraphics2D(), this.getSize());
            }
        };
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        canvas.initHUD();

        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                Point pt= arg0.getPoint();
                if(canvas.HitTest(pt.x, pt.y)){
                    doHUDaction(canvas.getHUDaction(pt.x, pt.y));
                }

            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    });
        add(canvas, BorderLayout.CENTER);

//2. Create a BranchGroup as the root of the scene branch graph.
        BranchGroup sceneRoot = createScene();



//5. Call the universe builder utility function to do the following:
        SimpleUniverse universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().getMultiTransformGroup().getTransformGroup(0);
        transform.lookAt(GazePoint, ViewerLocation, new Vector3d(2, 1, 0 ));
        transformGroup = universe.getViewingPlatform().getMultiTransformGroup().getTransformGroup(0);
        universe.getViewingPlatform().getViewers()[0].getView().setFrontClipDistance(ZNEAR);
        universe.getViewingPlatform().getViewers()[0].getView().setBackClipDistance(ZFAR);
        universe.getViewingPlatform().getViewers()[0].getView().setFieldOfView(FOV);
        //Bare så den ikke hoper ved rotation
        transform.invert();
        transformGroup.setTransform(transform);
//6. Insert the scene branch graph into the universe builder's Locale.
        universe.addBranchGraph(sceneRoot);
        setSize(600, 400);
        setTitle("Image Test");
        setVisible(true);
    }

    //2. Create a BranchGroup as the root of the scene branch graph.
    public BranchGroup createScene(){
        BranchGroup branchGroup = new BranchGroup();

        Background background = new Background(new Color3f(Color.black));
        BoundingSphere boundingSphere = new BoundingSphere(new Point3d(0,0,0), 1000);
        background.setApplicationBounds(boundingSphere);
        branchGroup.addChild(background);
        createShape(branchGroup);
        return branchGroup;
    }

    //3. Construct a Shape3D node with a TransformGroup node above it.
    public void createShape(BranchGroup branchGroup){
        Appearance appearance = new Appearance();
        ColoringAttributes coloringAttributes = new ColoringAttributes();
        coloringAttributes.setColor(new Color3f(Color.red));
        appearance.setColoringAttributes(coloringAttributes);
        Sphere sphere = new Sphere();
        sphere.setAppearance(appearance);

        //transformGroup.addChild(sphere);
        //4. Attach a RotationInterpolator behavior to the TransformGroup.
        Transform3D yAxis = new Transform3D();
        Alpha timing = new Alpha(0, 0);
        RotationInterpolator nodeRotator = new RotationInterpolator(timing, transformGroup, yAxis, 0.0f, 0.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0),0);


        branchGroup.addChild(transformGroup);
    }

    protected void doHUDaction(GraphicalUserInterface.ImageKeys hitTest) {
        switch (hitTest) {
            case Brunsviger:
                System.out.println("Så er der kage");
                break;
            default:
                break;
        }
    }

}
