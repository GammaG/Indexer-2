package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

/**
 * @author admin DateiScanner für verzeichnisse
 */
public class Scanner {

  private final ArrayList<String> liste = new ArrayList<String>();
  private String name;

  /**
   * Main
   * 
   * @param args
   */
  public static void main(final String[] args) {

    Scanner scanner = new Scanner();
    scanner.dateinHolen();
    scanner.saveIt();

  }

  /**
   * Konstruktor
   */
  public Scanner() {

  }

  /**
   * Holt alle dateinnamen aus einem Ordner
   */
  public void dateinHolen() {

    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    int returnVal = chooser.showOpenDialog(chooser);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File f = chooser.getSelectedFile();
      name = f.getName();
      String[] array = f.list();
      for (String string : array) {
        liste.add(string);
      }
    }

  }

  /**
   * Speichert den Index in einer txt;
   */
  public void saveIt() {
    final int c = liste.size();

    if (c == 0) {
      System.exit(0);
    }
    String endung = " Items.txt";
    if (c == 1) {
      endung = " Item.txt";
    }
    File file = new File("Index of " + name + " " + c + endung);
    BufferedWriter writer = null;

    try {
      writer = new BufferedWriter(new FileWriter(file));

      for (String s : liste) {
        writer.write(s);
        writer.newLine();
      }

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (writer != null) {

        try {
          writer.flush();
          writer.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      }

    }

  }
}
