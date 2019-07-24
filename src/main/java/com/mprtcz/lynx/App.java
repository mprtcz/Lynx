package com.mprtcz.lynx;

import com.mprtcz.lynx.saving.PrintResultToConsole;
import com.mprtcz.lynx.saving.SaveResultAsFile;
import com.mprtcz.lynx.saving.SaveResultService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/** @author Michal_Partacz */
public class App {
  private static final Logger logger = LogManager.getLogger(App.class);
  private static final String URL_ARG_NAME = "--URL";
  private static final String OUT_ARG_NAME = "--OUT";

  public static void main(String[] args) {
    logger.trace("App started");

    var argsMapped = parseArgs(args);
    String url = argsMapped.get(URL_ARG_NAME);
    if (url == null) {
      throw new IllegalArgumentException("Url cannot be null");
    }

    PageOperator pageOperator =
        new PageOperator(new PageObtainService(), new JsoupService(), resolveDomainName(url));

    var linkToPageProcessor = new LinkToPageProcessor(pageOperator);
    var result = linkToPageProcessor.processUrl(url);

    SaveResultService saveResultService = new SaveResultAsFile();
    if ("CONSOLE".equals(argsMapped.get(OUT_ARG_NAME))) {
      saveResultService = new PrintResultToConsole();
    }
    saveResultService.saveResult(result.constructResultString(new StringBuilder(), "-", false));
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

  private static Map<String, String> parseArgs(String[] args) {
    var map = new HashMap<String, String>();
    Arrays.stream(args)
        .forEach(
            s -> {
              if (s.contains("=")) {
                String[] pair = s.split("=");
                map.put(pair[0], pair[1]);
              } else {
                map.put(s, null);
              }
            });
    return map;
  }
}
