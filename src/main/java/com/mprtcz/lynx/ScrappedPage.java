package com.mprtcz.lynx;

import lombok.Getter;
import lombok.Setter;
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
public class ScrappedPage {
  private static final Logger logger = LogManager.getLogger(ScrappedPage.class);

  private Document page;
  private String pageAddress;
  private Set<String> linksWithinDomain = new HashSet<>();
  private List<ScrappedPage> pagesLinkedFromThis = new ArrayList<>();
  private Set<String> externalLinks = new HashSet<>();
  private Set<String> staticLinks = new HashSet<>();

  void printPretty(String indent, boolean isLast) {
    System.out.print(indent);
    if (isLast) {
      System.out.print("\\-");
      indent += "  ";
    } else {
      System.out.print("|-");
      indent += "| ";
    }
    System.out.println(pageAddress);
    if (!staticLinks.isEmpty()) {
      String finalIndent = indent;
      staticLinks.forEach((link) -> System.out.println(finalIndent + "| > " + link));
    }
    if (!externalLinks.isEmpty()) {
      String finalIndent = indent;
      externalLinks.forEach((link) -> System.out.println(finalIndent + "| + " + link));
    }

    for (int i = 0; i < pagesLinkedFromThis.size(); i++)
      pagesLinkedFromThis.get(i).printPretty(indent, i == pagesLinkedFromThis.size() - 1);
  }
}
