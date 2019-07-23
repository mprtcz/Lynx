package com.mprtcz.lynx;

import lombok.Getter;
import org.jsoup.nodes.Element;

/** @author Michal_Partacz */
@Getter
public class Link {
  private String url;
  private String type;
  private LinkType linkType;

  Link(Element element, String domain) {
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

  boolean isOfType(LinkType linkType) {
    return this.linkType == linkType;
  }

  @Override
  public String toString() {
    return "Link{" + "url='" + url + '\'' + ", type='" + type + '\'' + '}';
  }

  enum LinkType {
    UNKNOWN,
    DOMAIN,
    EXTERNAL,
    STATIC
  }
}
