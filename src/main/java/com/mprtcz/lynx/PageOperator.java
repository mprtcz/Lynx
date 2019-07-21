package com.mprtcz.lynx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/** @author Michal_Partacz */
public final class PageOperator {
  private static final Logger logger = LogManager.getLogger(PageOperator.class);

  private Document page;
  private final String pageAddress;
  private final PageObtainService pageObtainService;
  private String domain = "";
  private Set<String> linksWithinDomain;
  private Set<String> externalLinks;
  private Set<String> staticLinks;

  public PageOperator(String pageAddress, PageObtainService pageObtainService) {
    this.pageAddress = pageAddress;
    this.pageObtainService = pageObtainService;
    this.saveDomainName(pageAddress);
  }

  public PageOperator processPageAngGetResults() {
    this.fetchAndParsePage();
    Set<Link> allLinks = this.extractLinks();
    this.linksWithinDomain =
        applyFiltersAndCollectUrls(allLinks, link -> link.linkType == LinkType.DOMAIN);
    this.externalLinks =
        applyFiltersAndCollectUrls(allLinks, link -> link.linkType == LinkType.EXTERNAL);
    this.staticLinks =
        applyFiltersAndCollectUrls(allLinks, link -> link.linkType == LinkType.STATIC);
    return this;
  }

  Set<String> applyFiltersAndCollectUrls(Set<Link> allLinks, Predicate<Link> linkTypePredicate) {
    return allLinks.stream().filter(linkTypePredicate).map(link -> link.url).collect(toSet());
  }

  Set<Link> extractLinks() {
    return Stream.concat(streamElementsOfTag("a"), streamElementsOfTag("url"))
        .map(Link::new)
        .collect(toSet());
  }

  Stream<Element> streamElementsOfTag(String tag) {
    return this.page.select(tag).stream();
  }

  private void fetchAndParsePage() {
    this.page = pageObtainService.getPageFromUrl(pageAddress);
  }

  private void saveDomainName(String url) {
    try {
      URI uri = new URI(url);
      String domain = uri.getHost();
      this.domain = domain.startsWith("www.") ? domain.substring(4) : domain;
    } catch (URISyntaxException e) {
      logger.error("Exception while resolving a domain", e);
    }
  }

  private class Link {
    String url;
    String type;
    LinkType linkType;

    Link(Element element) {
      this.url = element.attr("abs:href");
      this.type = element.attr("rel");
      this.linkType = determineType();
    }

    private LinkType determineType() {
      if (type.isEmpty() && url.contains(domain)) {
        return LinkType.DOMAIN;
      }
      if (!type.isEmpty() && url.contains(domain)) {
        return LinkType.STATIC;
      }
      if (!url.contains(domain)) {
        return LinkType.EXTERNAL;
      }
      return LinkType.UNKNOWN;
    }

    @Override
    public String toString() {
      return "Link{" + "url='" + url + '\'' + ", type='" + type + '\'' + '}';
    }
  }

  private enum LinkType {
    UNKNOWN,
    DOMAIN,
    EXTERNAL,
    STATIC
  }
}
