package graph;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

import java.util.Set;
import java.util.ArrayList;

public class FileIO {
  private static String FILE_PATH = "./vertices.txt";

  public static ArrayList<Point> readPoints() {
    return readPoints(FILE_PATH);
  }

  public static void writePoints(ArrayList<Point> points) {
    writePoints(FILE_PATH, points);
  }

  public static void writePoints(Set<String> points) {
    writePoints(FILE_PATH, points);
  }
    
  public static void cleanFile() {
    cleanFile(FILE_PATH);
  }
  
  public static ArrayList<Point> readPoints(String filePath) {
    ArrayList<Point> points = new ArrayList<>();
    
    try {
      File f = new File(filePath);
      BufferedReader b = new BufferedReader(new FileReader(f));
      String readLine = "";
      
      while ((readLine = b.readLine()) != null) {
        String[] strArray = readLine.split(" ");
        points.add(new Point(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1])));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return points;
  }

  public static void writePoints(String filePath, ArrayList<Point> points) {
    try (FileWriter fw = new FileWriter(filePath); BufferedWriter bw = new BufferedWriter(fw)) {
      for(Point p: points)
        bw.write(p.x + " " + p.y + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }

  public static void writePoints(String filePath, Set<String> points) {
    try (FileWriter fw = new FileWriter(filePath); BufferedWriter bw = new BufferedWriter(fw)) {
      for(String p: points)
        bw.write(p + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }

  public static void cleanFile(String filePath) {
    try (FileWriter fw = new FileWriter(filePath); BufferedWriter bw = new BufferedWriter(fw)) {
      bw.write("");
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
}
