package com.mprtcz.lynx;

/** @author Michal_Partacz */
public class UrlProcessor {

  private ScrappedPage rootPage;
  private PageOperator pageOperator;
  private LinkToPageProcessor processingScheduler;

  public UrlProcessor(PageOperator pageOperator, LinkToPageProcessor processingScheduler) {
    this.pageOperator = pageOperator;
    this.processingScheduler = processingScheduler;
  }

  void processTheUrl(String url) {
    this.rootPage = this.pageOperator.processPageAngGetResults(url);
    processingScheduler.processPageDomainLinks(this.rootPage);
  }

  public ScrappedPage getRootPage() {
    return rootPage;
  }
}
