package com.mprtcz.lynx;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/** @author Michal_Partacz */
@RunWith(MockitoJUnitRunner.class)
public class PageOperatorTest {
  String domain = "testurl.com";
  PageOperator pageOperator;
  @Mock PageObtainService pageObtainService;
  @Mock JsoupService jsoupService;

  @Before
  public void setUp() {
    this.pageOperator = new PageOperator(pageObtainService, jsoupService, this.domain);
  }

  @Test
  public void processPageAngGetResults() {
    String url = "testurl.com/resource";
    Document document = new Document(url);
    when(pageObtainService.getPageFromUrl(url)).thenReturn(document);
    when(jsoupService.extractLinks(document, domain)).thenReturn(getLinks());
    ScrappedPage result = this.pageOperator.processPageAngGetResults(url);
    assertEquals(Set.of("http://www.testurl.com/path1/path2"), result.getLinksWithinDomain());
    assertEquals(
        Set.of("http://www.otheraddress.com", "http://www.twitter.com"), result.getExternalLinks());
    assertEquals(Set.of("http://www.testurl.com/img.png"), result.getStaticLinks());
  }

  @Test
  public void processPageAngGetResults_withUrlSanitization() {
    String url = "testurl.com/resource#test";
    String sanitizedUrl = "testurl.com/resource";
    Document document = new Document(sanitizedUrl);
    when(pageObtainService.getPageFromUrl(sanitizedUrl)).thenReturn(document);
    when(jsoupService.extractLinks(document, domain)).thenReturn(getLinks());
    ScrappedPage result = this.pageOperator.processPageAngGetResults(url);
    assertEquals(Set.of("http://www.testurl.com/path1/path2"), result.getLinksWithinDomain());
    assertEquals(
        Set.of("http://www.otheraddress.com", "http://www.twitter.com"), result.getExternalLinks());
    assertEquals(Set.of("http://www.testurl.com/img.png"), result.getStaticLinks());
  }

  @Test
  public void processPageAngGetResults_withEmptyLinks() {
    String url = "testurl.com/resource#test";
    String sanitizedUrl = "testurl.com/resource";
    Document document = new Document(sanitizedUrl);
    when(pageObtainService.getPageFromUrl(sanitizedUrl)).thenReturn(document);
    when(jsoupService.extractLinks(document, domain)).thenReturn(Set.of());
    ScrappedPage result = this.pageOperator.processPageAngGetResults(url);
    assertTrue(result.getLinksWithinDomain().isEmpty());
    assertTrue(result.getExternalLinks().isEmpty());
    assertTrue(result.getStaticLinks().isEmpty());
  }

  private Set<Link> getLinks() {
    Set<Link> links = new HashSet<>();
    links.add(
        constructLinkElement("http://www.testurl.com/path1/path2", "test", Link.LinkType.DOMAIN));
    links.add(constructLinkElement("http://www.otheraddress.com", "test", Link.LinkType.EXTERNAL));
    links.add(constructLinkElement("http://www.testurl.com/img.png", "test", Link.LinkType.STATIC));
    links.add(constructLinkElement("http://www.twitter.com", "test", Link.LinkType.EXTERNAL));
    return links;
  }

  private Link constructLinkElement(String url, String rel, Link.LinkType linkType) {
    Link link = new Link();
    link.url = url;
    link.rel = rel;
    link.linkType = linkType;
    return link;
  }
}
