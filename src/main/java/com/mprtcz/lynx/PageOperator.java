package com.mprtcz.lynx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.function.Predicate;

import static com.mprtcz.lynx.JsoupService.sanitizeUrl;
import static java.util.stream.Collectors.toSet;

/** @author Michal_Partacz */
public final class PageOperator {
  private static final Logger logger = LogManager.getLogger(PageOperator.class);

  private final String domain;
  private final PageObtainService pageObtainService;
  private final JsoupService jsoupService;

  public PageOperator(PageObtainService pageObtainService,
      JsoupService jsoupService, String domain) {
    this.pageObtainService = pageObtainService;
    this.jsoupService = jsoupService;
    this.domain = domain;
  }

  ScrappedPage processPageAngGetResults(String pageUrl) {
    pageUrl = sanitizeUrl(pageUrl);
    logger.debug("Processing " + pageUrl);
    Document fetchedDocument = pageObtainService.getPageFromUrl(pageUrl);

    ScrappedPage scrappedPage = new ScrappedPage();
    scrappedPage.setPageAddress(pageUrl);
    Set<Link> allLinks =
        this.jsoupService.extractLinks(fetchedDocument, this.domain);
    scrappedPage.setLinksWithinDomain(
        applyFiltersAndCollectUrls(allLinks, link -> link.isOfType(Link.LinkType.DOMAIN)));
    scrappedPage.setExternalLinks(
        applyFiltersAndCollectUrls(
            allLinks, link -> link.isOfType(Link.LinkType.EXTERNAL)));
    scrappedPage.setStaticLinks(
        applyFiltersAndCollectUrls(allLinks, link -> link.isOfType(Link.LinkType.STATIC)));
    return scrappedPage;
  }

  private Set<String> applyFiltersAndCollectUrls(
      Set<Link> allLinks, Predicate<Link> linkTypePredicate) {
    return allLinks.stream().filter(linkTypePredicate).map(Link::getUrl).collect(toSet());
  }

}
