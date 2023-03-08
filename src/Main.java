import xyz.kumaraswamy.naivebayes.DataCleaning;

import java.io.File;
import java.io.IOException;

public class Main {

  private static final File filesDir = new File(System.getProperty("user.dir"), "/files/");

  public static void main(String[] args) throws IOException {
    File uncleanedSpamDataSet = new File(filesDir, "uncleaned-spam.csv");

    boolean alreadyCleaned = new File(filesDir, "ham.txt").exists();
    if (!alreadyCleaned)
      // this will clean the data and produce
      // new files
      new DataCleaning(uncleanedSpamDataSet);

  }
}