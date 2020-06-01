import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.*;
import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SpiderBot {

  private final Set<URL> links;
  private final long startTime;
  private static final int MAX_DEPTH = 2;

  private SpiderBot(final URL startURL) {
    this.links = new HashSet<>();
    this.startTime = System.currentTimeMillis();
    crawl(initURLS(startURL));
  }

  private void crawl(final Set<URL> urls) {
    urls.removeAll(this.links);
    if(!urls.isEmpty()) {
      final Set<URL> newURLS = new HashSet<>();
      try {
        this.links.addAll(urls);
        for(final URL url : urls) {
          System.out.println("Time to connect: "
              +(System.currentTimeMillis() - this.startTime)+ " Depth: " + MAX_DEPTH + " Connected to host: " + url);
          final Document document = Jsoup.connect(url.toString()).get();
          final Elements linksOnPage = document.select("a[href]");
          for(final Element element : linksOnPage) {
            final String urlText = element.attr("abs:href");
            final URL discoveredURL = new URL(urlText);
            newURLS.add(discoveredURL);
          }
        }
      } catch(final Exception | Error ignored) {
      }
      crawl(newURLS);
    }
  }

  private Set<URL> initURLS(final URL startURL) {
    return Collections.singleton(startURL);
  }

  public static void main(String[] args) throws IOException {

    final SpiderBot spider = new SpiderBot(new URL("http://www.hawaii.edu/calendar"));

  }

}
