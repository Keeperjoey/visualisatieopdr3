/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessingApplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;
import processing.core.PVector;

/**
 *
 * @author Joey
 */
public class ProcessingApplet extends PApplet {

    float x = 100;
    float y = 50;
    float w = 150;
    float h = 80;

    static ArrayList<PVector> parseResults;

    final static int windowHeight = 1100;
    final static int windowWidth = 1100;
    static float count = -6f;
    boolean firstLoop = true;

    public static void main(String[] args) {

        try {
            parseResults = TxtReader.parseTxt();
            //   count = TxtReader.minZ;

        } catch (IOException ex) {
            Logger.getLogger(ProcessingApplet.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        PApplet.main(new String[]{ProcessingApplet.class.getName()});
    }

    @Override
    public void setup() {
        size(windowWidth, windowHeight);
    }

    @Override
    public void draw() {

        //
        float minX = TxtReader.minX;
        float maxX = TxtReader.maxX;
        float minY = TxtReader.minY;
        float maxY = TxtReader.maxY;
        float minZ = TxtReader.minZ;
        float maxZ = TxtReader.maxZ;
        fill(255, 0, 0);
        text(Float.toString(minX), 50, 50);
        text(Float.toString(maxX), 50, 70);
        text(Float.toString(minY), 50, 90);
        text(Float.toString(maxY), 50, 110);
        text(Float.toString(minZ), 50, 130);
        text(Float.toString(maxZ), 50, 150);

        if (firstLoop == true) {

            for (PVector results : parseResults) {
                float mappedX = map(results.x, minX, maxX, 0, windowWidth);
                float mappedY = map(results.y, minY, maxY, 0, windowHeight);
                float mappedZ = map(results.z, minZ, maxZ, 0, 255);

                stroke(color(mappedZ, mappedZ, 20));
                fill(color(mappedZ, mappedZ, 20));
                firstLoop = false;

                rect(mappedX, mappedY, 5, 5);
            }
            System.out.println("done");
        } else {
            for (PVector results : parseResults) {

                float mappedX = map(results.x, minX, maxX, 0, windowWidth);
                float mappedY = map(results.y, minY, maxY, 0, windowHeight);
                //float mappedZ = map(results.z, minZ, maxZ, 0, 255);

                if (results.z < count) {
                    stroke(color(0, 0, 255));
                    fill(color(0, 0, 255));

                    rect(mappedX, mappedY, 5, 5);
                }

            }
            count++;
            System.out.println("done");
            System.out.println(count + "");
        }

        // for the click listener reset
        
        fill(color(255,5,5));
        rect(x, y, w, h);
        if (mousePressed) {
            if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h) {
                println("The mouse is pressed and over the button");
                fill(0);
                firstLoop = true;
                count = TxtReader.minZ;
            } else {
                firstLoop = false;
            }
        }
    }

    // noLoop();
}
