package com.mprtcz.lynx.saving;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Michal_Partacz
 */
public class SaveResultAsFile implements SaveResultService {
  private static final Logger logger = LogManager.getLogger(SaveResultAsFile.class);

  @Override
  public void saveResult(String result) {
    try {
      Files.write(Paths.get(new File("result.txt").getCanonicalPath()),
          result.getBytes());
    } catch (IOException e) {
      logger.error("Error during results file saving", e);
    }
  }
}
