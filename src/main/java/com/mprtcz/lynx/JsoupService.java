package com.mprtcz.lynx;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/** @author Michal_Partacz */
public class JsoupService {

  Set<Link> extractLinks(Document document, String domain) {
    return Stream.concat(streamElementsOfTag(document, "a"), streamElementsOfTag(document, "link"))
        .map(element -> Link.getInstance(element, domain))
        .filter(
            link -> link.url.equals(sanitizeUrl(link.url)))
        .collect(toSet());
  }

  private Stream<Element> streamElementsOfTag(Document document, String tag) {
    return document == null ? Stream.of() : document.select(tag).stream();
  }

  static String sanitizeUrl(String pageUrl) {
    if (pageUrl.contains("?")) {
      pageUrl = pageUrl.split("\\?")[0];
    }
    if (pageUrl.contains("#")) {
      pageUrl = pageUrl.split("#")[0];
    }
    if (pageUrl.contains("&")) {
      pageUrl = pageUrl.split("&")[0];
    }
    return pageUrl;
  }
}
