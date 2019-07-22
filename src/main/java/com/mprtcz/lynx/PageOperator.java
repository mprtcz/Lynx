package com.mprtcz.lynx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/** @author Michal_Partacz */
public final class PageOperator {
  private static final Logger logger = LogManager.getLogger(PageOperator.class);

  private final String domain;
  private final PageObtainService pageObtainService;

  public PageOperator(PageObtainService pageObtainService, String domain) {
    this.pageObtainService = pageObtainService;
    this.domain = domain;
  }

  ScrappedPage processPageAngGetResults(String pageUrl) {
    pageUrl = sanitizeUrl(pageUrl);
    logger.debug("Processing " + pageUrl);
    Document fetchedDocument = fetchPage(pageUrl);

    ScrappedPage scrappedPage = new ScrappedPage();
    scrappedPage.setPageAddress(pageUrl);
    Set<Link> allLinks = this.extractLinks(fetchedDocument);
    scrappedPage.setLinksWithinDomain(
        applyFiltersAndCollectUrls(allLinks, link -> link.linkType == LinkType.DOMAIN));
    scrappedPage.setExternalLinks(
        applyFiltersAndCollectUrls(allLinks, link -> link.linkType == LinkType.EXTERNAL));
    scrappedPage.setStaticLinks(
        applyFiltersAndCollectUrls(allLinks, link -> link.linkType == LinkType.STATIC));
    return scrappedPage;
  }

  private String sanitizeUrl(String pageUrl) {
    if (pageUrl.contains("#")) {
      pageUrl = pageUrl.split("#")[0];
    }
    if (pageUrl.contains("?")) {
      pageUrl = pageUrl.split("\\?")[0];
    }
    return pageUrl;
  }

  private Set<String> applyFiltersAndCollectUrls(
      Set<Link> allLinks, Predicate<Link> linkTypePredicate) {
    return allLinks.stream().filter(linkTypePredicate).map(link -> link.url).collect(toSet());
  }

  private Set<Link> extractLinks(Document document) {
    return Stream.concat(streamElementsOfTag(document, "a"), streamElementsOfTag(document, "link"))
        .map(Link::new)
        .collect(toSet());
  }

  private Stream<Element> streamElementsOfTag(Document document, String tag) {
    return document == null ? Stream.of() : document.select(tag).stream();
  }

  private Document fetchPage(String pageUrl) {
    return pageObtainService.getPageFromUrl(pageUrl);
  }

  private class Link {
    String url;
    String type;
    LinkType linkType;

    Link(Element element) {
      this.url = element.attr("abs:href");
      this.type = element.attr("rel");
      this.linkType = determineLinkType(domain);
    }

    private LinkType determineLinkType(String domain) {
      if (type.isEmpty() && isWithinDomain(url, domain)) {
        return LinkType.DOMAIN;
      }
      if (!type.isEmpty() && isWithinDomain(url, domain)) {
        return LinkType.STATIC;
      }
      if (!isWithinDomain(url, domain)) {
        return LinkType.EXTERNAL;
      }
      return LinkType.UNKNOWN;
    }

    private boolean isWithinDomain(String url, String domain) {
      return url.startsWith("https://" + domain) || url.startsWith("http://" + domain);
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
