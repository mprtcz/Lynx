package com.mprtcz.lynx;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/** @author Michal_Partacz */
public class LinkTest {

  @Test
  public void getLinkInstance_staticLink() {
    Element element = constructLinkElement("http://testurl.com/resource",
        "test");
    var link = Link.getInstance(element, "testurl.com");

    assertEquals("http://testurl.com/resource", link.getUrl());
    assertEquals("test", link.getRel());
    assertEquals(Link.LinkType.STATIC, link.getLinkType());
  }
  @Test
  public void getLinkInstance_domainLink() {
    Element element = constructLinkElement("http://testurl.com/resource", "");
    var link = Link.getInstance(element, "testurl.com");

    assertEquals("http://testurl.com/resource", link.getUrl());
    assertEquals("", link.getRel());
    assertEquals(Link.LinkType.DOMAIN, link.getLinkType());
  }
  @Test
  public void getLinkInstance_externalLink() {
    Element element = constructLinkElement("http://facebook.com/resource",
        "");
    var link = Link.getInstance(element, "testurl.com");

    assertEquals("http://facebook.com/resource", link.getUrl());
    assertEquals("", link.getRel());
    assertEquals(Link.LinkType.EXTERNAL, link.getLinkType());
  }

  private Element constructLinkElement(String url, String rel) {
    Tag tag = Tag.valueOf("a");
    Attributes attrs = new Attributes();
    attrs.put("rel", rel);
    attrs.put("href", url);
    return new Element(tag, "", attrs);
  }
}
