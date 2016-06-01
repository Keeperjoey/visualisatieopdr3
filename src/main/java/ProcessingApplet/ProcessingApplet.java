/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessingApplet;

import static ProcessingApplet.TxtReader.schoolX;
import static ProcessingApplet.TxtReader.schoolY;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    final static int windowHeight = 1100;           //window height
    final static int windowWidth = 1100;            //window width
    static float waterLevel = -6f;
    boolean firstLoop = true;
    boolean pause = false;
    ControlP5 cp5;
    String myTime = "00:00";
    float waterLevelOld = Float.MIN_VALUE;
    static CallbackListener cb;

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

        // slider
        cp5 = new ControlP5(this);

        //Callbacklistener for slider if you click slider somewhere it well call reset
        cb = new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                switch (theEvent.getAction()) {
                    case (ControlP5.ACTION_CLICK):
                        reset();
                        break;

                }
            }
        };

        cp5.addSlider("ChangeForOtherDistance")
                .setPosition(100, 140)
                .setWidth(300)
                .setRange(500, 1000)
                .setNumberOfTickMarks(5)
                .onChange(cb)
                .setHeight(50)
                .setValue(500f);

        cp5.addButton("Reset")
                .setValue(0)
                .setPosition(100, 50)
                .setSize(150, 80)
                .setValueLabel("Reset/Redraw for distance");

        cp5.addButton("Pause/Play")
                .setValue(0)
                .setPosition(250, 50)
                .setSize(150, 80);

        cp5.addButton("Screenshot")
                .setValue(0)
                .setPosition(400, 50)
                .setSize(150, 80);

//         And From your main() method or any other method
    }

    public void controlEvent(ControlEvent theEvent) {
        if ("ChangeForOtherDistance".equals(theEvent.getController().getName())) {
            reset();
        }
        if ("Reset".equals(theEvent.getController().getName())) {
            reset();
        }
        if ("Pause/Play".equals(theEvent.getController().getName())) {
            pause();
        }
        if ("Screenshot".equals(theEvent.getController().getName())) {
            screenshot();

        }
    }

    public void reset() {
        firstLoop = true;
        waterLevel = TxtReader.minZ;
        myTime = "00:00";
    }

    public void pause() {
        if (pause == true) {
            pause = false;
        } else {
            pause = true;
        }
    }

    public void screenshot() {

        saveFrame("screenshot-######.png");

    }

    @Override
    public void draw() {

        fill(255, 0, 0);

        drawTheMap();
        //  intializeButtons();

    }

    public void drawTheMap() {
        float drawDistance = cp5.getController("ChangeForOtherDistance").getValue();
        float minX = schoolX - drawDistance;
        float maxX = schoolX + drawDistance;
        float minY = schoolY - drawDistance;
        float maxY = schoolY + drawDistance;
        float minZ = TxtReader.minZ;
        float maxZ = TxtReader.maxZ;

        if (firstLoop == true) {

            for (PVector results : parseResults) {
                float mappedX = map(results.x, minX, maxX, 0, windowWidth);
                float mappedY = map(results.y, minY, maxY, windowHeight, 0 );
                float mappedZ = map(results.z, minZ, maxZ, 0, 255);

                stroke(color(mappedZ, mappedZ, 20));
                fill(color(mappedZ, mappedZ, 20));
                firstLoop = false;

                rect(mappedX, mappedY, 5, 5);
            }
        } else {
            for (PVector results : parseResults) {

                float mappedX = map(results.x, minX, maxX, 0, windowWidth);
                float mappedY = map(results.y, minY, maxY, windowHeight, 0);
                //float mappedZ = map(results.z, minZ, maxZ, 0, 255);

                if (results.z < waterLevel && results.z < waterLevelOld) {
                    stroke(color(0, 0, 255));
                    fill(color(0, 0, 255));

                    rect(mappedX, mappedY, 5, 5);
                }
                waterLevelOld = waterLevel;

            }
            
            //remove later
            fill(color(255,0,0));
            //remove later red dot in middle
            rect(windowWidth/2, windowHeight/2, 30, 30);
            //
            
            if (pause == false ) {
                waterLevel = waterLevel + 0.15f;
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                Date d;
                try {
                    d = df.parse(myTime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, 9);
                    myTime = df.format(cal.getTime());
                    fill(color(0));
                    rect(960, 50, 200, 80);
                    fill(color(255));
                    text("Time: " + myTime, 1000, 80);
                    text("WaterLvl: ~ " + Math.round(waterLevel), 1000, 100);

                } catch (ParseException e) {

                }

            }
        }
    }

//    void intializeButtons() {
////        float x = 100;
////        float y = 50;
////        float w = 150;
////        float h = 80;
////        // for the click listener reset
////        fill(color(255, 5, 5));
////        
////        rect(x, y, w, h);
////        rect(250, 50, 150, 80);
////        
////        fill(color(255));
////        text("reset", 150, 60);
////        text("pauze", 300, 60);
////        if (mousePressed) {
////            if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h) {
////                println("The mouse is pressed and over the button");
////                fill(0);
////                firstLoop = true;
////                count = TxtReader.minZ;
////            } else {
////                firstLoop = false;
////            }
////
////            x = 250;
////            y = 50;
////            w = 150;
////            h = 80;
////
////            if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h) {
////                println("The mouse is pressed and over the button");
////                fill(0);
////                if(pause == false)
////                   pause = true; 
////                else 
////                    pause = false;
////                
////            } 
//
////        }
//    }
    // noLoop();
}
