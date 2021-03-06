package com.mprtcz.lynx;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** @author Michal_Partacz */
@Getter
@Setter
@ToString
public class ScrappedPage {
  private static final Logger logger = LogManager.getLogger(ScrappedPage.class);

  private Document page;
  private String pageAddress;
  private Set<String> linksWithinDomain = new HashSet<>();
  private List<ScrappedPage> pagesLinkedFromThis = new ArrayList<>();
  private Set<String> externalLinks = new HashSet<>();
  private Set<String> staticLinks = new HashSet<>();

  String constructResultString(StringBuilder result, String indent, boolean isLast) {
    result.append(indent);
    if (isLast) {
      result.append("\\-");
      indent += "  ";
    } else {
      result.append("|-");
      indent += "| ";
    }
    result.append(pageAddress).append("\n");
    if (!staticLinks.isEmpty()) {
      String finalIndent = indent;
      staticLinks.forEach(
          (link) -> result.append(finalIndent).append("| > ").append(link).append("\n"));
    }
    if (!externalLinks.isEmpty()) {
      String finalIndent = indent;
      externalLinks.forEach(
          (link) -> result.append(finalIndent).append("| + ").append(link).append("\n"));
    }

    for (int i = 0; i < pagesLinkedFromThis.size(); i++)
      pagesLinkedFromThis
          .get(i)
          .constructResultString(result, indent, i == pagesLinkedFromThis.size() - 1);

    return result.toString();
  }
}
