package com.mprtcz.lynx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/** @author Michal_Partacz */
public class PageObtainService {
  private static final Logger logger = LogManager.getLogger(PageObtainService.class);
  private ConcurrentHashMap<String, Document> processedLinks = new ConcurrentHashMap<>();

  Document getPageFromUrl(String url) {
    logger.debug("Fetching a page for address " + url);
    Document doc = processedLinks.get(url);
    if (doc != null) {
      logger.debug("Returning cached doc for " + url);
      return doc;
    }
    try {
      doc = Jsoup.connect(url).get();
      processedLinks.put(url, doc);
    } catch (IOException e) {
      logger.error("Exception while fetching a page ", e);
    }
    logger.debug("Fetched the page from " + url);
    return doc;
  }
}
