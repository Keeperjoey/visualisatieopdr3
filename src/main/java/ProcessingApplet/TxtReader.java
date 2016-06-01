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

    public static float minX = Float.MAX_VALUE;
    public static float maxX = Float.MAX_VALUE;
    public static float minY = Float.MAX_VALUE;
    public static float maxY = Float.MAX_VALUE;
    public static float minZ = Float.MAX_VALUE;
    public static float maxZ = Float.MAX_VALUE;

    public static ArrayList<PVector> parseTxt() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("rotterdamopendata_hoogtebestandtotaal_oost.csv")));

        br.readLine(); // skip the first line of the file.
        br.readLine(); // skip the first line of the file.

        String txtLine;

        coordinate = new ArrayList<>();

        while ((txtLine = br.readLine()) != null) {
            String[] splitLine = txtLine.split(",");

            float xCoordinate = Float.parseFloat(splitLine[0]);
            float yCoordinate = Float.parseFloat(splitLine[1]);
            float zCoordinate = Float.parseFloat(splitLine[2]);

            PVector holdCoordinate = new PVector(xCoordinate, yCoordinate, zCoordinate);

            coordinate.add(holdCoordinate);

            if (minX > xCoordinate) {
                minX = xCoordinate;
            }

            if (minY > yCoordinate) {
                minY = yCoordinate;
            }

            if (minZ > zCoordinate) {
                minZ = zCoordinate;
            }

            if (maxX < xCoordinate) {
                maxX = xCoordinate;
            }

            if (maxY < yCoordinate) {
                maxY = yCoordinate;
            }

            if (maxZ < zCoordinate) {
                maxZ = zCoordinate;
            }

        }

        return coordinate;
    }

    public static String commasToPeriods(String stringWithCommas) {
        return stringWithCommas.replaceAll(",", ".");
    }

}
