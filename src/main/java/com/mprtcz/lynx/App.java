package com.mprtcz.lynx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Michal_Partacz
 */
public class App {
  private static final Logger logger = LogManager.getLogger(App.class);
  private static final String ROOT_URL = "https://zaufanatrzeciastrona.pl/";

  public static void main(String[] args) {
    logger.trace("App started");

    assert(args.length > 0);
    String pageName = args[0];

    PageOperator pageOperator = new PageOperator(new PageObtainService(),
        resolveDomainName(ROOT_URL));

    UrlProcessor urlProcessor = new UrlProcessor(pageOperator , new LinkToPageProcessor(pageOperator));
    urlProcessor.processTheUrl(ROOT_URL);
    urlProcessor.getRootPage().printPretty("-", false);
  }

  private static String resolveDomainName(String url) {
    try {
      URI uri = new URI(url);
      String domain = uri.getHost();
      return domain.startsWith("www.") ? domain.substring(4) : domain;
    } catch (URISyntaxException e) {
      logger.error("Exception while resolving a domain", e);
    }
    return "";
  }
}
