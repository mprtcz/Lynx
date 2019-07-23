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
        .map(element -> new Link(element, domain))
        .collect(toSet());
  }

  private Stream<Element> streamElementsOfTag(Document document, String tag) {
    return document == null ? Stream.of() : document.select(tag).stream();
  }
}
