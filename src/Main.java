import java.net.MalformedURLException;

public class Main {

    public static void main(String[] args) {

        if (true) {
            WebsiteScraper websiteScraper = new WebsiteScraper("https://bn.wikipedia.org/wiki/%E0%A6%AC%E0%A6%BE%E0%A6%82%E0%A6%B2%E0%A6%BE_%E0%A6%89%E0%A6%87%E0%A6%95%E0%A6%BF%E0%A6%AA%E0%A6%BF%E0%A6%A1%E0%A6%BF%E0%A6%AF%E0%A6%BC%E0%A6%BE", "p");
            websiteScraper.setCacheMode(true);
            try {
                websiteScraper.build();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
        }

//        websiteScraper.run();
    }
}
