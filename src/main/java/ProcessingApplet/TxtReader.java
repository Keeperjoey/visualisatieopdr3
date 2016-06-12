/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessingApplet;

/**
 *
 * @author Joey
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import processing.core.PVector;

public class TxtReader {

    public static ArrayList coordinate;

    public static float minZ = Float.MAX_VALUE;
    public static float maxZ = Float.MIN_VALUE;
    public static float schoolX = 92865f;
    public static float schoolY = 436920f;
    

    public static ArrayList<PVector> parseTxt() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("rotterdamopendata_hoogtebestandtotaal_oost.csv")));

        br.readLine(); // skip the first line of the file.
        br.readLine(); // skip the first line of the file.

        String txtLine;

        coordinate = new ArrayList<>();

        while ((txtLine = br.readLine()) != null) {
            String[] splitLine = txtLine.split(",");

            float zCoordinate = Float.parseFloat(splitLine[2]);

            PVector holdCoordinate = new PVector(Float.parseFloat(splitLine[0]), Float.parseFloat(splitLine[1]), zCoordinate);
            
            float sumX = Math.abs(schoolX - holdCoordinate.x);
            float sumY = Math.abs(schoolY - holdCoordinate.y);

            if (sumX < 500 && sumY < 500){
            coordinate.add(holdCoordinate);

            if (minZ > zCoordinate) {
                minZ = zCoordinate;
            }

            if (maxZ < zCoordinate) {
                maxZ = zCoordinate;
            }
          }
        }
        br.close();

        return coordinate;
    }
}
