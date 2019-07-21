package com.mprtcz.lynx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Michal_Partacz
 */
public class App {
  private static final Logger logger = LogManager.getLogger(App.class);

  public static void main(String[] args) {
    logger.trace("App started");

    assert(args.length > 0);
    String pageName = args[0];

    PageOperator pageOperator = new PageOperator("http://github.com", new PageObtainService());
  }
}
