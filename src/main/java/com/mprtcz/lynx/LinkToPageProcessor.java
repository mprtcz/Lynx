package com.mprtcz.lynx;

import java.util.concurrent.ConcurrentHashMap;

/** @author Michal_Partacz */
public class LinkToPageProcessor {
  private PageOperator pageOperator;
  private ConcurrentHashMap<String, Object> processedLinks = new ConcurrentHashMap<>();
  private static final Object DUMMY = new Object();

  public LinkToPageProcessor(PageOperator pageOperator) {
    this.pageOperator = pageOperator;
  }

  ScrappedPage processPageDomainLinks(ScrappedPage scrappedPage) {
    var domainLinks = scrappedPage.getLinksWithinDomain();
    domainLinks
        .parallelStream()
        .filter((s) -> !processedLinks.containsKey(s))
        .forEach(
            s -> {
              var result = pageOperator.processPageAngGetResults(s);
              processedLinks.put(s, DUMMY);
              scrappedPage.getPagesLinkedFromThis().add(processPageDomainLinks(result));
            });
    return scrappedPage;
  }
}
