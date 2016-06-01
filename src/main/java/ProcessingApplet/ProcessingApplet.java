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

    static ArrayList<PVector> parseResults;

    final static int windowHeight = 1100;
    final static int windowWidth = 1100;
    float minX = TxtReader.minX;
    float maxX = TxtReader.maxX;
    float minY = TxtReader.minY;
    float maxY = TxtReader.maxY;
    float minZ = TxtReader.minZ;
    float maxZ = TxtReader.maxZ;

    public static void main(String[] args) {

        try {
            parseResults = TxtReader.parseTxt();

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
        text(Float.toString(minX), 50, 50);
        text(Float.toString(maxX), 50, 70);
        text(Float.toString(minY), 50, 90);
        text(Float.toString(maxY), 50, 110);
        text(Float.toString(minZ), 50, 130);
        text(Float.toString(maxZ), 50, 150);
        
    }
}
