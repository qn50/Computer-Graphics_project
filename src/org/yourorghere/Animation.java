
package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Animation implements GLEventListener{

    private Ellipse ellipse1;

    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new Animation());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
// Rotation
    float angle = 5;
    float num = 0.5f;
    float limit = 0.5f;

//heart rate effect variables.
    double s = 1;
    boolean inc = true;
    boolean ch = true;

    // Translation and Scalling 
    float position = 0;
    float speed = 0.008f;
    float direction = 1;

    boolean fly = true;

    float rotateY = 0f;
    float translatex = 0.001f;
    float translatez = 0.001f;
    float translatex_RectR = 0f;
    float translatex_RectL = 0f;
    float translatey_Armour = 0f;
    float scaley = 1.2f;
    float s2 = 0.8f;

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        if(num == 12){
           
        }
        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();

        // Move the "drawing cursor" around
        gl.glTranslatef(0.0f, 0.0f, -20.0f);
        gl.glScalef(s2, s2, 0);
//        //---------------------------------------------------------------------
        gl.glPushMatrix();
        gl.glRotated(angle, 0, 1, 0);
        angle += num;
        // num to increse the rotate speed.
        num += 0.15;
        //fly.
        if (fly) {
            if (num >= 130) {
                translatex -= 0.009;
                if (translatex <= -1.5) {
                    fly = false;
                }
            } else if (num >= 50) {
                translatex += 0.009;
            }
        }
        gl.glTranslated(0, translatex, 0);
        UprSideLinesR(gl);
        UprSideLinesL(gl);
        ExtrmUprTilted(gl);
        UprEllipse(gl, ellipse1);
        gl.glPopMatrix();
//        //---------------------------------------------------------------------

//        //---------------------------------------------------------------------
        gl.glPushMatrix();
        //if flying move right Rectangles.
        if (num >= 60 && translatex_RectR < 1.75) {
            translatex_RectR += 0.02;
        }
        gl.glTranslated(translatex_RectR, 0, 0);
//------------------------------------------------------------------------
//heart rate effect - Right rectangles
        if (num > 105) {
            hartEffect();
            gl.glScaled(1, s, 1);
        }
        SideLinesR(gl);
        RectanglesRight(gl);
        gl.glPopMatrix();
//------------------------------------------------------------------------        
        gl.glPushMatrix();
        //if flying move left Rectangles.
        if (num >= 60 && translatex_RectL > -1.75) {
            translatex_RectL -= 0.02;
        }
        gl.glTranslated(translatex_RectL, 0, 0);
//heart rate effect - Left rectangles
        if (num > 105) {
            hartEffect();
            gl.glScaled(1, s, 1);
        }
        SideLinesL(gl);
        RectanglesLeft(gl);
        gl.glPopMatrix();

//        InnerEllipse(gl, ellipse1);
        gl.glPushMatrix();
        if (translatex_RectL <= -1.75 && translatey_Armour > -6) {
            translatey_Armour -= 0.035;
        }
        gl.glTranslated(0, translatey_Armour, 0);
        UprTilted(gl);
//        //---------------------------------------------------------------------
        SideTilted(gl);
        Inner_Armour(gl);
        gl.glTranslated(-4.7, -3.3, 0);
        InnerEllipse(gl, ellipse1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        //----------------------------------------------------------------------
        gl.glTranslated(-1, 1.5, 0);
        //Draw FCIT
        gl.glScaled(1.6, 1.6, 0);
        gl.glTranslated(-0.75, -0.2, 0);
        gl.glColor3f(1, 1, 1);
        if (num > 105) {
            DrawF(gl);
        }
        if (num > 123.5) {
            gl.glTranslated(0.9, 0, 0);
            DrawC(gl, ellipse1);
        }
        if (num > 142) {
            gl.glTranslated(0.4, 0, 0);
            DrawI(gl);
        }
        if (num > 160.5) {
            DrawT(gl);
        }
        gl.glPopMatrix();
        //----------------------------------------------------------------------
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    static void CreatR2(GL gl) {

        //E21
        gl.glVertex3f(3.6f, 0.9f, 0.0f);
        gl.glVertex3f(3.6f, 1.9f, 0.0f);
        //E22
        gl.glVertex3f(5.5f, 0.9f, 0.0f);
        gl.glVertex3f(5.5f, 1.9f, 0.0f);
        //E23
        gl.glVertex3f(3.7f, 2f, 0.0f);
        gl.glVertex3f(5.4f, 2f, 0.0f);
        //E24
        gl.glVertex3f(3.7f, 0.8f, 0.0f);
        gl.glVertex3f(5.4f, 0.8f, 0.0f);
        //E21->E23
        gl.glVertex3f(3.6f, 1.9f, 0.0f);
        gl.glVertex3f(3.7f, 2f, 0.0f);
        gl.glVertex3f(3.7f, 2f, 0.0f);
        gl.glVertex3f(3.7f, 2f, 0.0f);
        //E23->E22
        gl.glVertex3f(5.4f, 2f, 0.0f);
        gl.glVertex3f(5.5f, 1.9f, 0.0f);
        gl.glVertex3f(5.5f, 1.9f, 0.0f);
        gl.glVertex3f(5.5f, 1.9f, 0.0f);
        //E22->E24
        gl.glVertex3f(5.5f, 0.9f, 0.0f);
        gl.glVertex3f(5.4f, 0.8f, 0.0f);
        gl.glVertex3f(5.4f, 0.8f, 0.0f);
        gl.glVertex3f(5.4f, 0.8f, 0.0f);
        //E24->E21
        gl.glVertex3f(3.7f, 0.8f, 0.0f);
        gl.glVertex3f(3.6f, 0.9f, 0.0f);
        gl.glVertex3f(3.6f, 0.9f, 0.0f);
        gl.glVertex3f(3.6f, 0.9f, 0.0f);
        //--------------------------------

    }

    static void CreatR13(GL gl) {
        //E21
        gl.glVertex3f(-3.6f, 0.9f, 0.0f);
        gl.glVertex3f(-3.6f, 1.9f, 0.0f);
        //E22
        gl.glVertex3f(-5.5f, 0.9f, 0.0f);
        gl.glVertex3f(-5.5f, 1.9f, 0.0f);
        //E23
        gl.glVertex3f(-3.7f, 2f, 0.0f);
        gl.glVertex3f(-5.4f, 2f, 0.0f);
        //E24
        gl.glVertex3f(-3.7f, 0.8f, 0.0f);
        gl.glVertex3f(-5.4f, 0.8f, 0.0f);
        //E21->E23
        gl.glVertex3f(-3.6f, 1.9f, 0.0f);
        gl.glVertex3f(-3.7f, 2f, 0.0f);
        gl.glVertex3f(-3.7f, 2f, 0.0f);
        gl.glVertex3f(-3.7f, 2f, 0.0f);
        //E23->E22
        gl.glVertex3f(-5.4f, 2f, 0.0f);
        gl.glVertex3f(-5.5f, 1.9f, 0.0f);
        gl.glVertex3f(-5.5f, 1.9f, 0.0f);
        gl.glVertex3f(-5.5f, 1.9f, 0.0f);
        //E22->E24
        gl.glVertex3f(-5.5f, 0.9f, 0.0f);
        gl.glVertex3f(-5.4f, 0.8f, 0.0f);
        gl.glVertex3f(-5.4f, 0.8f, 0.0f);
        gl.glVertex3f(-5.4f, 0.8f, 0.0f);
        //E24->E21
        gl.glVertex3f(-3.7f, 0.8f, 0.0f);
        gl.glVertex3f(-3.6f, 0.9f, 0.0f);
        gl.glVertex3f(-3.6f, 0.9f, 0.0f);
        gl.glVertex3f(-3.6f, 0.9f, 0.0f);
    }

    static void transRightSide(GL gl) {
        //translate (r1) TO (r2,r3,r4,r5)
        int flag = 0;
        for (int i = 0; i < 4; i++) {
            gl.glTranslatef(0.0f, -1.75f, 0.0f);
            if (flag == 0) {
                gl.glColor3f(0.0f, 0.6f, 0.0f);
                flag = 1;
            } else {
                gl.glColor3f(0.4f, 1.0f, 0.4f);
                flag = 0;
            }
            gl.glBegin(GL.GL_POLYGON);
            CreatR2(gl);
            gl.glEnd();
        }

        //translate (r5) TO (r6)
        gl.glTranslatef(2.2f, 0.0f, 0.0f);
        gl.glColor3f(0.0f, 0.6f, 0.0f);
        gl.glBegin(GL.GL_POLYGON);
        CreatR2(gl);
        gl.glEnd();

        //translate (r6) TO (r7,r8,r9)
        for (int i = 0; i < 3; i++) {
            gl.glTranslatef(0.0f, 1.75f, 0.0f);
            if (flag == 1) {
                gl.glColor3f(0.0f, 0.6f, 0.0f);
                flag = 0;
            } else {
                gl.glColor3f(0.4f, 1.0f, 0.4f);
                flag = 1;
            }
            gl.glBegin(GL.GL_POLYGON);
            CreatR2(gl);
            gl.glEnd();
        }

        //translate (r9) TO (r10)
        gl.glTranslatef(2.2f, -1.75f, 0.0f);
        gl.glBegin(GL.GL_POLYGON);
        CreatR2(gl);
        gl.glEnd();

        //translate (r10) TO (r11)
        gl.glTranslatef(0.0f, -1.75f, 0.0f);
        gl.glColor3f(0.0f, 0.6f, 0.0f);
        gl.glBegin(GL.GL_POLYGON);
        CreatR2(gl);
        gl.glEnd();
        gl.glColor3f(0.4f, 1.0f, 0.4f);
    }

    static void transLeftSide(GL gl) {
        int flag = 0;
        //translate (r12) TO (r13,r14,r15,r16)
        for (int i = 0; i < 4; i++) {
            gl.glTranslatef(0.0f, -1.75f, 0.0f);
            if (flag == 0) {
                gl.glColor3f(0.0f, 0.3f, 0.0f);
                flag = 1;
            } else {
                gl.glColor3f(0.4f, 1.0f, 0.4f);
                flag = 0;
            }
            gl.glBegin(GL.GL_POLYGON);
            CreatR13(gl);
            gl.glEnd();
        }

        //translate (r16) TO (r17)
        gl.glTranslatef(-2.2f, 0.0f, 0.0f);
        gl.glColor3f(0.0f, 0.3f, 0.0f);
        gl.glBegin(GL.GL_POLYGON);
        CreatR13(gl);
        gl.glEnd();

        //translate (r17) TO (r18,r19,r20)
        flag = 1;
        for (int i = 0; i < 3; i++) {
            gl.glTranslatef(0.0f, 1.75f, 0.0f);
            if (flag == 0) {
                gl.glColor3f(0.0f, 0.3f, 0.0f);
                flag = 1;
            } else {
                gl.glColor3f(0.4f, 1.0f, 0.4f);
                flag = 0;
            }
            gl.glBegin(GL.GL_POLYGON);
            CreatR13(gl);
            gl.glEnd();
        }

        //translate (r20) TO (r21)
        gl.glTranslatef(-2.2f, -1.75f, 0.0f);
        gl.glColor3f(0.4f, 1.0f, 0.4f);
        gl.glBegin(GL.GL_POLYGON);
        CreatR13(gl);
        gl.glEnd();

        //translate (r21) TO (r22)
        gl.glTranslatef(0.0f, -1.75f, 0.0f);
        gl.glColor3f(0.0f, 0.3f, 0.0f);
        gl.glBegin(GL.GL_POLYGON);
        CreatR13(gl);
        gl.glEnd();
    }

    static void Inner_Armour(GL gl) {

        gl.glColor3f(0.0f, 0.65f, 0.0f);
        //----------------------------i1---------------------------------------

        gl.glBegin(GL.GL_POLYGON);
        //E1
        gl.glVertex3f(0.1f, -6f, 0.0f);
        gl.glVertex3f(3.25f, -4.2f, 0.0f);
        //E3
        gl.glVertex3f(3.25f, -4.2f, 0.0f);
        gl.glVertex3f(3.35f, 1.9f, 0.0f);
        //E3-->E5
        gl.glVertex3f(3.35f, 1.90f, 0.0f);
        gl.glVertex3f(3.31f, 2f, 0.0f);
        gl.glVertex3f(3.27f, 2.1f, 0.0f);
        gl.glVertex3f(3.24f, 2.2f, 0.0f);
        gl.glVertex3f(3.20f, 2.31f, 0.0f);
        gl.glVertex3f(3.15f, 2.4f, 0.0f);
        gl.glVertex3f(3.10f, 2.45f, 0.0f);
        gl.glVertex3f(2.90f, 2.55f, 0.0f);
        //E5
        gl.glVertex3f(2.9f, 2.55f, 0.0f);
        gl.glVertex3f(0.1f, 3.8f, 0.0f);
        //E7
        gl.glVertex3f(0.1f, -6f, 0.0f);
        gl.glVertex3f(0.1f, 3.8f, 0.0f);
        gl.glEnd();
        //---------------------------------------------------------------------
        gl.glColor3f(0.0f, 0.55f, 0.0f);

        //----------------------------i2---------------------------------------
        gl.glBegin(GL.GL_POLYGON);
        //E2
        gl.glVertex3f(-0.1f, -6f, 0.0f);
        gl.glVertex3f(-3.25f, -4.20f, 0.0f);
        //E4
        gl.glVertex3f(-3.25f, -4.2f, 0.0f);
        gl.glVertex3f(-3.35f, 1.9f, 0.0f);
        //E4-->E6
        gl.glVertex3f(-3.35f, 1.90f, 0.0f);
        gl.glVertex3f(-3.31f, 2f, 0.0f);
        gl.glVertex3f(-3.27f, 2.1f, 0.0f);
        gl.glVertex3f(-3.24f, 2.2f, 0.0f);
        gl.glVertex3f(-3.20f, 2.31f, 0.0f);
        gl.glVertex3f(-3.15f, 2.4f, 0.0f);
        gl.glVertex3f(-3.10f, 2.45f, 0.0f);
        gl.glVertex3f(-2.90f, 2.55f, 0.0f);
        //E6
        gl.glVertex3f(-2.9f, 2.55f, 0.0f);
        gl.glVertex3f(-0.1f, 3.8f, 0.0f);
        //E8
        gl.glVertex3f(-0.1f, -6f, 0.0f);
        gl.glVertex3f(-0.1f, 3.8f, 0.0f);
        gl.glEnd();

        // inner windows
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        //----------------------------i4---------------------------------------
        gl.glBegin(GL.GL_POLYGON);
        //E13   
        gl.glVertex3f(2.45f, 1.2f, 0.0f);
        gl.glVertex3f(2.45f, 2.3f, 0.0f);
        //E14
        gl.glVertex3f(1.8f, 2.4f, 0.0f);
        gl.glVertex3f(1.8f, 1.3f, 0.0f);
        //E15
        gl.glVertex3f(2.45f, 1.2f, 0.0f);
        gl.glVertex3f(1.8f, 1.3f, 0.0f);
        //E16
        gl.glVertex3f(2.45f, 2.3f, 0.0f);
        gl.glVertex3f(1.8f, 2.4f, 0.0f);
        gl.glEnd();
        //---------------------------------------------------------------------

        //----------------------------i4 mirror--------------------------------
        gl.glBegin(GL.GL_POLYGON);
        //m E13   
        gl.glVertex3f(-2.45f, 1.2f, 0.0f);
        gl.glVertex3f(-2.45f, 2.3f, 0.0f);
        //m E14
        gl.glVertex3f(-1.8f, 2.4f, 0.0f);
        gl.glVertex3f(-1.8f, 1.3f, 0.0f);
        //m E15
        gl.glVertex3f(-2.45f, 1.2f, 0.0f);
        gl.glVertex3f(-1.8f, 1.3f, 0.0f);
        //m E16
        gl.glVertex3f(-2.45f, 2.3f, 0.0f);
        gl.glVertex3f(-1.8f, 2.4f, 0.0f);
        gl.glEnd();
        //---------------------------------------------------------------------

        //----------------------------i5---------------------------------------
        gl.glBegin(GL.GL_POLYGON);
        //E17
        gl.glVertex3f(0.55f, 1.9f, 0.0f);
        gl.glVertex3f(0.55f, 3f, 0.0f);
        //E18
        gl.glVertex3f(1.2f, 1.8f, 0.0f);
        gl.glVertex3f(1.2f, 2.9f, 0.0f);
        //E19
        gl.glVertex3f(0.55f, 3f, 0.0f);
        gl.glVertex3f(1.2f, 2.9f, 0.0f);
        //E20
        gl.glVertex3f(0.55f, 1.9f, 0.0f);
        gl.glVertex3f(1.2f, 1.8f, 0.0f);
        gl.glEnd();
        //---------------------------------------------------------------------

        //----------------------------i5 mirror--------------------------------
        gl.glBegin(GL.GL_POLYGON);
        //m E17
        gl.glVertex3f(-0.55f, 1.9f, 0.0f);
        gl.glVertex3f(-0.55f, 3f, 0.0f);
        //m E18
        gl.glVertex3f(-1.2f, 1.8f, 0.0f);
        gl.glVertex3f(-1.2f, 2.9f, 0.0f);
        //m E19
        gl.glVertex3f(-0.55f, 3f, 0.0f);
        gl.glVertex3f(-1.2f, 2.9f, 0.0f);
        //m E20
        gl.glVertex3f(-0.55f, 1.9f, 0.0f);
        gl.glVertex3f(-1.2f, 1.8f, 0.0f);
        gl.glEnd();

    }

    static void UprTilted(GL gl) {

        gl.glColor3f(0.0f, 0.4f, 0.0f);
        //----------------------------i3---------------------------------------
        gl.glBegin(GL.GL_POLYGON);
        //E9
        gl.glVertex3f(0.15f, 4f, 0.0f);
        gl.glVertex3f(0.15f, 5f, 0.0f);
        //E10
        gl.glVertex3f(0.15f, 5f, 0.0f);
        gl.glVertex3f(1.3f, 4.6f, 0.0f);
        //E11
        gl.glVertex3f(1.3f, 3.6f, 0.0f);
        gl.glVertex3f(1.3f, 4.6f, 0.0f);
        //E12
        gl.glVertex3f(0.15f, 4f, 0.0f);
        gl.glVertex3f(1.3f, 3.6f, 0.0f);
        gl.glEnd();
        //---------------------------------------------------------------------
        //----------------------------i3 mirror--------------------------------
        gl.glBegin(GL.GL_POLYGON);
        //m E9
        gl.glVertex3f(-0.15f, 4f, 0.0f);
        gl.glVertex3f(-0.15f, 5f, 0.0f);
        //m E10
        gl.glVertex3f(-0.15f, 5f, 0.0f);
        gl.glVertex3f(-1.3f, 4.6f, 0.0f);
        //m E11
        gl.glVertex3f(-1.3f, 3.6f, 0.0f);
        gl.glVertex3f(-1.3f, 4.6f, 0.0f);
        //m E12
        gl.glVertex3f(-0.15f, 4f, 0.0f);
        gl.glVertex3f(-1.3f, 3.6f, 0.0f);
        gl.glEnd();
    }

    static void SideTilted(GL gl) {
        gl.glColor3f(0.4f, 1.0f, 0.4f);
        //----------------------------i6---------------------------------------
        gl.glBegin(GL.GL_POLYGON);
        // e30
        gl.glVertex3f(1.5f, 3.8f, 0.0f);
        gl.glVertex3f(3.0f, 3.0f, 0.0f);
        // e28
        gl.glVertex3f(1.5f, 4.8f, 0.0f);
        gl.glVertex3f(3.0f, 4.0f, 0.0f);
        // e27 
        gl.glVertex3f(1.5f, 3.8f, 0.0f);
        gl.glVertex3f(1.5f, 4.8f, 0.0f);
        //e29 
        gl.glVertex3f(3.0f, 3.0f, 0.0f);
        gl.glVertex3f(3.0f, 4.0f, 0.0f);
        gl.glEnd();
        //---------------------------------------------------------------------
        //----------------------------i6 mirror--------------------------------
        gl.glBegin(GL.GL_POLYGON);
        // e30
        gl.glVertex3f(-1.5f, 3.8f, 0.0f);
        gl.glVertex3f(-3.0f, 3.0f, 0.0f);
        // e28
        gl.glVertex3f(-1.5f, 4.8f, 0.0f);
        gl.glVertex3f(-3.0f, 4.0f, 0.0f);
        // e27 
        gl.glVertex3f(-1.5f, 3.8f, 0.0f);
        gl.glVertex3f(-1.5f, 4.8f, 0.0f);
        //e29 
        gl.glVertex3f(-3.0f, 3.0f, 0.0f);
        gl.glVertex3f(-3.0f, 4.0f, 0.0f);
        gl.glEnd();
    }

    static void UprEllipse(GL gl, Ellipse ellipse1) {
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        ellipse1 = new Ellipse(0.0f, 6.6f, 0.6f, 1.1f);
        ellipse1.draw2(gl);
        gl.glColor3f(0.0f, 0.0f, 0.0f);

        ellipse1 = new Ellipse(0.0f, 6.6f, 0.5f, 1f);
        ellipse1.draw2(gl);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
    }

    static void ExtrmUprTilted(GL gl) {
//----------------------------i7---------------------------------------
        gl.glBegin(GL.GL_POLYGON);
        // e31
        gl.glVertex3f(0.1f, 7.0f, 0.0f);
        gl.glVertex3f(0.1f, 5.2f, 0.0f);
        // e32
        gl.glVertex3f(1.2f, 6.0f, 0.0f);
        gl.glVertex3f(1.2f, 4.87f, 0.0f);
        // e33
        gl.glVertex3f(1.2f, 6.0f, 0.0f);
        gl.glVertex3f(0.1f, 7.0f, 0.0f);
        // e34
        gl.glVertex3f(0.1f, 5.2f, 0.0f);
        gl.glVertex3f(1.2f, 4.87f, 0.0f);
        gl.glEnd();
        //---------------------------------------------------------------------

        //----------------------------i7 mirror--------------------------------
        gl.glBegin(GL.GL_POLYGON);
        // e31
        gl.glVertex3f(-0.1f, 7.0f, 0.0f);
        gl.glVertex3f(-0.1f, 5.2f, 0.0f);
        // e32
        gl.glVertex3f(-1.2f, 6.0f, 0.0f);
        gl.glVertex3f(-1.2f, 4.87f, 0.0f);
        // e33
        gl.glVertex3f(-1.2f, 6.0f, 0.0f);
        gl.glVertex3f(-0.1f, 7.0f, 0.0f);
        // e34
        gl.glVertex3f(-0.1f, 5.2f, 0.0f);
        gl.glVertex3f(-1.2f, 4.87f, 0.0f);
        gl.glEnd();
    }

    //UprSideLinesRight
    static void UprSideLinesR(GL gl) {
        //----------------------------i8---------------------------------------
        gl.glBegin(GL.GL_POLYGON);

        // e35
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(1.3f, 5.4f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(4.5f, 5.4f, 0.0f);
        // e36
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(1.3f, 5.6f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(4.5f, 5.6f, 0.0f);
        // e37
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(1.3f, 5.4f, 0.0f);
        gl.glVertex3f(1.3f, 5.6f, 0.0f);
        // e38
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(4.5f, 5.4f, 0.0f);
        gl.glVertex3f(4.5f, 5.6f, 0.0f);
        gl.glEnd();

        //---------------------------------------------------------------------
        //----------------------------i9---------------------------------------
        gl.glBegin(GL.GL_POLYGON);

        // e39
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(1.3f, 5.8f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(3.5f, 6.1f, 0.0f);
        // e41
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(1.3f, 6.0f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(3.5f, 6.3f, 0.0f);
        // e40
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(1.3f, 5.8f, 0.0f);
        gl.glVertex3f(1.3f, 6.0f, 0.0f);
        // e42
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(3.5f, 6.1f, 0.0f);
        gl.glVertex3f(3.5f, 6.3f, 0.0f);
        gl.glEnd();

        //---------------------------------------------------------------------
        //----------------------------i10--------------------------------------
        gl.glBegin(GL.GL_POLYGON);

        // e43
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(1.3f, 5.2f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(3.5f, 4.9f, 0.0f);
        // e44
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(1.3f, 5.2f, 0.0f);
        gl.glVertex3f(1.3f, 5.0f, 0.0f);
        // e45
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(1.3f, 5.0f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(3.5f, 4.7f, 0.0f);
        // e46
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(3.5f, 4.9f, 0.0f);
        gl.glVertex3f(3.5f, 4.7f, 0.0f);
        gl.glEnd();

    }

    //UprSideLinesLeft
    static void UprSideLinesL(GL gl) {
        //----------------------------i8 mirror--------------------------------
        gl.glBegin(GL.GL_POLYGON);

        // e35
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(-1.3f, 5.4f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(-4.5f, 5.4f, 0.0f);
        // e36
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(-1.3f, 5.6f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(-4.5f, 5.6f, 0.0f);
        // e37
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(-1.3f, 5.4f, 0.0f);
        gl.glVertex3f(-1.3f, 5.6f, 0.0f);
        // e38
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(-4.5f, 5.4f, 0.0f);
        gl.glVertex3f(-4.5f, 5.6f, 0.0f);
        gl.glEnd();

        //---------------------------------------------------------------------
        //----------------------------i9 mirror--------------------------------
        gl.glBegin(GL.GL_POLYGON);

        // e39
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(-1.3f, 5.8f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(-3.5f, 6.1f, 0.0f);
        // e40
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(-1.3f, 5.8f, 0.0f);
        gl.glVertex3f(-1.3f, 6.0f, 0.0f);
        // e41
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(-1.3f, 6.0f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(-3.5f, 6.3f, 0.0f);
        // e42
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(-3.5f, 6.1f, 0.0f);
        gl.glVertex3f(-3.5f, 6.3f, 0.0f);
        gl.glEnd();

        //---------------------------------------------------------------------
        //----------------------------i10 mirror-------------------------------
        gl.glBegin(GL.GL_POLYGON);

        // e43
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(-1.3f, 5.2f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(-3.5f, 4.9f, 0.0f);
        // e44
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(-1.3f, 5.2f, 0.0f);
        gl.glVertex3f(-1.3f, 5.0f, 0.0f);
        // e45
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glVertex3f(-1.3f, 5.0f, 0.0f);
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(-3.5f, 4.7f, 0.0f);
        // e46
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glVertex3f(-3.5f, 4.9f, 0.0f);
        gl.glVertex3f(-3.5f, 4.7f, 0.0f);
        gl.glEnd();
    }

    static void SideLinesR(GL gl) {
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        //RIGT LINES
        gl.glColor3f(0.0f, 0.6f, 0.0f);
        gl.glBegin(GL.GL_POLYGON);

        gl.glVertex3f(5.1f, 1.05f, 0.0f);
        gl.glVertex3f(5.8f, 0.35f, 0.0f);

        gl.glVertex3f(6.2f, 0.50f, 0.0f);
        gl.glVertex3f(5.5f, 1.2f, 0.0f);
        gl.glEnd();

        gl.glBegin(GL.GL_POLYGON);

        gl.glVertex3f(4.9f, -1.4f, 0.0f);
        gl.glVertex3f(5.8f, -0.5f, 0.0f);

        gl.glVertex3f(6.1f, -0.65f, 0.0f);
        gl.glVertex3f(5.4f, -1.35f, 0.0f);

        gl.glEnd();

        gl.glBegin(GL.GL_POLYGON);

        gl.glVertex3f(7.5f, -3.0f, 0.0f);
        gl.glVertex3f(8.1f, -2.4f, 0.0f);

        gl.glVertex3f(8.45f, -2.45f, 0.0f);
        gl.glVertex3f(7.7f, -3.2f, 0.0f);

        gl.glEnd();
    }

    static void SideLinesL(GL gl) {
        gl.glColor3f(0.0f, 0.3f, 0.0f);

        //LEFT LIENS
        gl.glBegin(GL.GL_POLYGON);

        gl.glVertex3f(-5.1f, 1.05f, 0.0f);
        gl.glVertex3f(-5.8f, 0.35f, 0.0f);

        gl.glVertex3f(-6.2f, 0.50f, 0.0f);
        gl.glVertex3f(-5.5f, 1.2f, 0.0f);
        gl.glEnd();

        gl.glBegin(GL.GL_POLYGON);

        gl.glVertex3f(-4.9f, -1.4f, 0.0f);
        gl.glVertex3f(-5.8f, -0.5f, 0.0f);

        gl.glVertex3f(-6.1f, -0.65f, 0.0f);
        gl.glVertex3f(-5.4f, -1.35f, 0.0f);

        gl.glEnd();

        gl.glBegin(GL.GL_POLYGON);

        gl.glVertex3f(-7.5f, -3.0f, 0.0f);
        gl.glVertex3f(-8.1f, -2.4f, 0.0f);

        gl.glVertex3f(-8.45f, -2.45f, 0.0f);
        gl.glVertex3f(-7.7f, -3.2f, 0.0f);

        gl.glEnd();
    }

    static void InnerEllipse(GL gl, Ellipse ellipse1) {

        gl.glPointSize(4);
        gl.glTranslatef(4.7f, 0.3f, 0.0f);
        gl.glColor3f(0.0f, 0.0f, 0.0f);

        ellipse1 = new Ellipse(0.0f, 0.5f, 1f, 3.5f);
        ellipse1.draw(gl);

    }

    //Right side Rectangles
    static void RectanglesRight(GL gl) {
        //------------------------------------ Right side Rectangles -------------------------------------------
        //first Rectangle (r2)
        gl.glTranslatef(0.0f, 2f, 0.0f);
        gl.glColor3f(0.4f, 1.0f, 0.4f);
        gl.glBegin(GL.GL_POLYGON);
        CreatR2(gl);
        gl.glEnd();

        //translate r2 for all right side
        transRightSide(gl);

    }

    //Left side Rectangles
    static void RectanglesLeft(GL gl) {
        //------------------------------------ Left side Rectangles -------------------------------------------
        //first Rectangle (r13)
        gl.glTranslatef(0.0f, 2f, 0.0f);
        gl.glBegin(GL.GL_POLYGON);
        CreatR13(gl);
        gl.glEnd();

        //translate r13 for all right side
        transLeftSide(gl);
    }

    //heart rate effect
    public void hartEffect() {

        if (inc) {
            s += 0.01;
        } else {
            if (s >= 1.75 && ch) {
                s = 1.4;
                inc = true;
                ch = false;
            } else {
                s -= 0.0035;
            }
        }
        if (s >= 1.75) {
            inc = false;
        } else if (s <= 1) {
            inc = true;
            ch = true;
        }
    }

    static void DrawF(GL gl) {
        gl.glColor3f(0.0f, 0.6f, 0.0f);

        // Draw F
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(-0.8f, 0.8f);
        gl.glVertex2f(-0.8f, -0.7f);
        gl.glVertex2f(-0.8f, 0.8f);
        gl.glVertex2f(-0.1f, 0.8f);
        gl.glVertex2f(-0.1f, 0.8f);
        gl.glVertex2f(-0.1f, 0.55f);
        gl.glVertex2f(-0.1f, 0.55f);
        gl.glVertex2f(-0.5f, 0.55f);
        gl.glVertex2f(-0.5f, 0.55f);
        gl.glVertex2f(-0.5f, 0.2f);
        gl.glVertex2f(-0.5f, 0.2f);
        gl.glVertex2f(-0.1f, 0.2f);
        gl.glVertex2f(-0.1f, 0.2f);
        gl.glVertex2f(-0.1f, -0.05f);
        gl.glVertex2f(-0.1f, -0.05f);
        gl.glVertex2f(-0.5f, -0.05f);
        gl.glVertex2f(-0.5f, -0.05f);
        gl.glVertex2f(-0.5f, -0.7f);
        gl.glVertex2f(-0.5f, -0.7f);
        gl.glVertex2f(-0.8f, -0.7f);
        gl.glEnd();

    }

    static void DrawC(GL gl, Ellipse ellipse1) {
        gl.glPointSize(4);
        ellipse1 = new Ellipse(0.0f, 0.0f, 0.7f, 0.7f);
        ellipse1.draw3(gl);
    }

    static void DrawI(GL gl) {
        gl.glColor3f(0.0f, 0.65f, 0.0f);

        // Draw I
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(0.5f, 0.8f);
        gl.glVertex2f(0.5f, -0.7f);
        gl.glVertex2f(0.8f, 0.8f);
        gl.glVertex2f(0.8f, -0.7f);
        gl.glVertex2f(0.5f, 0.8f);
        gl.glVertex2f(0.8f, 0.8f);
        gl.glVertex2f(0.5f, -0.7f);
        gl.glVertex2f(0.8f, -0.7f);
        gl.glEnd();
    }

    static void DrawT(GL gl) {
        // Draw T
        gl.glTranslated(1, 0, 0);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(0.5f, 0.55f);
        gl.glVertex2f(0.5f, -0.7f);
        gl.glVertex2f(0.8f, 0.55f);
        gl.glVertex2f(0.8f, -0.7f);
        gl.glVertex2f(0.8f, 0.55f);
        gl.glVertex2f(1.3f, 0.55f);
        gl.glVertex2f(0.5f, 0.55f);
        gl.glVertex2f(0f, 0.55f);
        gl.glVertex2f(0f, 0.55f);
        gl.glVertex2f(0f, 0.8f);
        gl.glVertex2f(1.3f, 0.55f);
        gl.glVertex2f(1.3f, 0.8f);
        gl.glVertex2f(1.3f, 0.8f);
        gl.glVertex2f(0f, 0.8f);
        gl.glVertex2f(0.5f, -0.7f);
        gl.glVertex2f(0.8f, -0.7f);
        gl.glEnd();
    }
}