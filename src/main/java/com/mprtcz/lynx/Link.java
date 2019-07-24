package com.mprtcz.lynx;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jsoup.nodes.Element;

/** @author Michal_Partacz */
@Getter
@ToString
@EqualsAndHashCode
public class Link {
  String url;
  String rel;
  LinkType linkType;

  static Link getInstance(Element element, String domain) {
    Link link = new Link();
    link.url = element.attr("abs:href");
    link.rel = element.attr("rel");
    link.linkType = link.determineLinkType(domain);
    return link;
  }

  private LinkType determineLinkType(String domain) {
    if (rel.isEmpty() && isWithinDomain(url, domain)) {
      return LinkType.DOMAIN;
    }
    if (!rel.isEmpty() && isWithinDomain(url, domain)) {
      return LinkType.STATIC;
    }
    if (!isWithinDomain(url, domain)) {
      return LinkType.EXTERNAL;
    }
    return LinkType.UNKNOWN;
  }

  private boolean isWithinDomain(String url, String domain) {
    return url.startsWith(domain)
        || url.startsWith("https://" + domain)
        || url.startsWith("http://" + domain);
  }

  boolean isOfType(LinkType linkType) {
    return this.linkType == linkType;
  }

  enum LinkType {
    UNKNOWN,
    DOMAIN,
    EXTERNAL,
    STATIC
  }
}
