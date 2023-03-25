
package org.yourorghere;

import javax.media.opengl.GL;

public class Ellipse {
    private float x, y;
    private float a, b;

    public Ellipse(float x, float y, float a, float b) {
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
    }

    public void draw(GL gl) {
        gl.glBegin(GL.GL_POINTS);
        gl.glPointSize(4);
        for (double angle = 0; angle < 2*Math.PI; angle += 0.01) {
            float x = (float) (this.x + a * Math.cos(angle));
            float y = (float) (this.y + b * Math.sin(angle));
            gl.glVertex2f(x, y);
        }
        gl.glEnd();
    }
    public void draw2(GL gl) {
        gl.glBegin(GL.GL_POLYGON);
        for (double angle = 0; angle < Math.PI; angle += 0.01) {
            float x = (float) (this.x + a * Math.cos(angle));
            float y = (float) (this.y + b * Math.sin(angle));
            gl.glVertex2f(x, y);
        }
        gl.glEnd();
    }
     void draw3(GL gl) {
        gl.glBegin(GL.GL_POINTS);
        gl.glColor3f(0.4f, 1, 0.4f);
        gl.glPointSize(4);
        for (double angle = 0.5; angle < 1.8 * Math.PI; angle += 0.01) {
            float x = (float) (this.x + a * Math.cos(angle));
            float y = (float) (this.y + b * Math.sin(angle));
            gl.glVertex2f(x, y);
        }
        gl.glEnd();
    }
}