package xyz.kumaraswamy.naivebayes;

import java.io.File;

public class Trainer {

  private final File hamDataSet;
  private final File spamDataSet;

  public Trainer(File workspace) {
    hamDataSet = new File(workspace, "ham.txt");
    spamDataSet = new File(workspace, "spam.txt");
  }
}
