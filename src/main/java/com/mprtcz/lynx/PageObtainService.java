package com.mprtcz.lynx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author Michal_Partacz
 */
public class PageObtainService {
  private static final Logger logger = LogManager.getLogger(PageObtainService.class);

  Document getPageFromUrl(String url) {
    logger.debug("Fetching a page for address" + url);
    Document doc = null;
    try {
      doc = Jsoup.connect(url).get();
    } catch (IOException e) {
      logger.error("Exception while fetching a page ", e);
    }
    return doc;
  }}
