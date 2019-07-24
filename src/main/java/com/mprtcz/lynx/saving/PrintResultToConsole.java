package com.mprtcz.lynx.saving;

/**
 * @author Michal_Partacz
 */
public class PrintResultToConsole implements SaveResultService {
  @Override
  public void saveResult(String result) {
    System.out.println(result);
  }
}
