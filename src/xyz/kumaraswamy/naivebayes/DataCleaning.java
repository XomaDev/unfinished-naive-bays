package xyz.kumaraswamy.naivebayes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

public class DataCleaning {

  public DataCleaning(File file) throws IOException {
    File parent = file.getParentFile();
    List<String> lemmartizationDataSet = Files.readAllLines(
            new File(parent, "lemmatization-en.txt").toPath()
    );
    HashMap<String, String> loadedLemmDataMap = new HashMap<>(lemmartizationDataSet.size());
    for (String s : lemmartizationDataSet) {
      String[] data = s
              .replace(" ", "\t")
              .split("\t");
      loadedLemmDataMap.put(data[1], data[0]);
    }

    // not in utf-8 format, so we read it this way
    String[] lines = new String(
            Files.readAllBytes(file.toPath()))
            .split("\n");

    FileOutputStream hamStream = new FileOutputStream(new File(parent, "ham.txt"));
    FileOutputStream spamStream = new FileOutputStream(new File(parent, "spam.txt"));

    for (int i = 1; i < lines.length; i++) {
      String line = lines[i];
      // remove 'ham, ' or 'spam, '
      boolean isHam = line.startsWith("ham");

      line = line.substring(isHam ? 4 : 5);
      line = line.toLowerCase()
              // replace everything other than letters,
              // numbers and spaces with spaces, this way for more quality
              .replaceAll("[^a-z0-1 ]", " ")
              // remove repeated spaces with replace with single
              .replaceAll(" +", " ");
      if (line.length() < 20)
        continue;
      String[] words = line.split(" ");
      StringJoiner rejoined = new StringJoiner(" ");

      for (String word : words) {
        int len = word.length();
        if (len < 2) // dont add individual letters
          continue;
        // silly repeated words like 'ffffff'
        if (len > 5) {
          StringBuilder runLength = new StringBuilder();
          runLength.append(String.valueOf(word.charAt(0))
                  .repeat(len));
          if (runLength.toString().equals(word))
            continue;
        }

        if (word.substring(1).repeat(len).equals(word))
          continue;
        rejoined.add(loadedLemmDataMap.getOrDefault(word, word));
      }
      String text = rejoined.toString().replaceAll("[^a-z0-1 ]", "");
      byte[] write = (text + "\n").getBytes();
      if (isHam)
        hamStream.write(write);
      else spamStream.write(write);
    }
    hamStream.close();
    spamStream.close();
  }
}
