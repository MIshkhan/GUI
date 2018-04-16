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

  public static ArrayList<Tuple<Integer, Integer>> readPoints() {
    return readPoints(FILE_PATH);
  }

  public static void writePoints(ArrayList<Tuple<Integer, Integer>> points) {
    writePoints(FILE_PATH, points);
  }

  public static void writePoints(Set<String> points) {
    writePoints(FILE_PATH, points);
  }
    
  public static void cleanFile() {
    cleanFile(FILE_PATH);
  }

  public static ArrayList<Integer> readWireLengths(String filePath) {
    ArrayList<Integer> wirelengths = new ArrayList<>();
    
    try {
      File f = new File(filePath);
      BufferedReader b = new BufferedReader(new FileReader(f));
      String readLine = "";
      
      while ((readLine = b.readLine()) != null) {
        String[] strArray = readLine.split(" ");
        wirelengths.add(Integer.parseInt(strArray[2]));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return wirelengths;
  }
  
  public static ArrayList<Tuple<Integer, Double>> readExecTimes(String filePath) {
    ArrayList<Tuple<Integer, Double>> execTimes = new ArrayList<>();
    
    try {
      File f = new File(filePath);
      BufferedReader b = new BufferedReader(new FileReader(f));
      String readLine = "";
      
      while ((readLine = b.readLine()) != null) {
        String[] strArray = readLine.split(" ");
        execTimes.add(new Tuple<Integer, Double>(Integer.parseInt(strArray[0]), Double.parseDouble(strArray[1])));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return execTimes;
  }
  
  public static ArrayList<Tuple<Integer, Integer>> readPoints(String filePath) {
    ArrayList<Tuple<Integer, Integer>> points = new ArrayList<>();
    
    try {
      File f = new File(filePath);
      BufferedReader b = new BufferedReader(new FileReader(f));
      String readLine = "";
      
      while ((readLine = b.readLine()) != null) {
        String[] strArray = readLine.split(" ");
        points.add(new Tuple<Integer, Integer>(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1])));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return points;
  }

  public static void writePoints(String filePath, ArrayList<Tuple<Integer, Integer>> points) {
    try (FileWriter fw = new FileWriter(filePath); BufferedWriter bw = new BufferedWriter(fw)) {
      for(Tuple<Integer, Integer> p: points)
        bw.write(p.k + " " + p.v + "\n");
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
