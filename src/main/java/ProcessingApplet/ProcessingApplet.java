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
import controlP5.ControlP5Constants;
import controlP5.Knob;
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

    static ArrayList<PVector> parseResults;         //Arraylist of all the x,y,z 

    final static int windowHeight = 1100;           //window height
    final static int windowWidth = 1100;            //window width
    static float waterLevel = -100f;                //initial waterlevel
    boolean firstLoop = true;                       //to see what part has to be drawn again
    boolean pause = false;                          //see if pauze is pressed
    ControlP5 cp5;                                  //for the gui libary
    String timePassed = "01 00:00";                    //The time water started rising
    float waterLevelOld = Float.MIN_VALUE;          //to only draw the new dots ( speeds up things)
    static CallbackListener cb;                     // An onclick for the sliderbar 

    public static void main(String[] args) {

        try {
            //Calls TxtReader functions parseTxt
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
        //fill the map intialy with a brownish color
        reset();

        //initialize the controlP5 library
        cp5 = new ControlP5(this);

        //Callbacklistener for slider if you click slider somewhere it well call reset
        cb = new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                switch (theEvent.getAction()) {
                    case (ControlP5.ACTION_CLICK):
                        reset();
                        break;

                }
            }
        };

        //add slider with an onChange listeren
        cp5.addSlider("ChangeForOtherDistance")
                .setPosition(100, 140)
                .setWidth(300)
                .setRange(250, 500)
                .onChange(cb)
                .setHeight(50)
                .setValue(500f);

        // add reset button
        cp5.addButton("Reset")
                .setValue(0)
                .setPosition(100, 50)
                .setSize(150, 80)
                .setValueLabel("Reset/Redraw for distance");

        //add pauze/play button
        cp5.addButton("Pause/Play")
                .setValue(0)
                .setPosition(250, 50)
                .setSize(150, 80);
        //adds screenshot button
        cp5.addButton("Screenshot")
                .setValue(0)
                .setPosition(400, 50)
                .setSize(150, 80);

        //add wheel for the waterlevel
        cp5.addKnob("waterLevelWheel")
                .setValue(5)
                .setPosition(750, 50)
                .setSize(100, 100)
                .setRadius(50)
                .setRange(TxtReader.minZ, 20);

//        cp5.addTextfield("Time")
//                .setPosition(850, 50)
//                .setText("Time = " + timePassed);
//         And From your main() method or any other method
    }

    //Eventlistener ( if the event is passed to something )
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
        //draw brownish color over the map so it looks like it's first draw
        fill(color(0, 20, 200));
        rect(0, 0, 1100, 1100);
        //Sets firstLoop to true so it's starts drawing all again
        firstLoop = true;
        //Set waterLevel to the minimum Z value in the file
        waterLevel = TxtReader.minZ;
        //Set timePassed to 0 
        timePassed = "01 00:00";
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

        //fill(255, 0, 0);
        if (waterLevel < 20)
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
                float mappedY = map(results.y, minY, maxY, windowHeight, 0);
                float mappedZ = map(results.z, minZ, maxZ, 0, 255);

                stroke(color(mappedZ, mappedZ, 20));
                fill(color(mappedZ, mappedZ, 20));
                firstLoop = false;

                rect(mappedX, mappedY, 4, 4);
            }
        } else {
            for (PVector results : parseResults) {
                cp5.getController("waterLevelWheel").setValue(waterLevel);
                float mappedX = map(results.x, minX, maxX, 0, windowWidth);
                float mappedY = map(results.y, minY, maxY, windowHeight, 0);
                //float mappedZ = map(results.z, minZ, maxZ, 0, 255);

                if (results.z < waterLevel && results.z < waterLevelOld) {
                    stroke(color(0, 0, 255));
                    fill(color(0, 0, 255));

                    rect(mappedX, mappedY, 4, 4);
                }
                waterLevelOld = waterLevel;

            }

            if (pause == false) {
                waterLevel = waterLevel + 0.15f;
                timeAndDaysPassed();
            }

        }
    }

    void timeAndDaysPassed() {
        SimpleDateFormat df = new SimpleDateFormat("dd HH:mm");
        Date d;
        try {
            d = df.parse(timePassed);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, 9);
            timePassed = df.format(cal.getTime());
            fill(color(0));
            rect(960, 50, 200, 80);
            fill(color(255));
            text("Days: " + (cal.get(Calendar.DATE) - 1), 1000, 80);
            text("Time: " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE), 1000, 100);
            
        } catch (ParseException e) {

        }

    }
//to Prove we could do it without liberary but it's better now
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
