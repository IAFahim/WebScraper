import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.lang.Math.*;

public class WebsiteScraper implements Runnable {

    private String stringURL, htmlTag, htmlEndTag, Keyword;
    private boolean cacheMode;
    private int tagFound, KeywordFound;
    private URL url;
    private BufferedReader bufferedReader;

    public String getStringURL() {
        return stringURL;
    }

    public void setStringURL(String stringURL) {
        this.stringURL = stringURL;
        setUrl();
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl() {
        try {
            this.url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getHtmlTag() {
        return htmlTag;
    }

    public void setHtmlTag(String htmlTag) {
        this.htmlTag = '<' + htmlTag + '>';
        setHtmlEndTag("<\"" + htmlTag + '>');
    }

    private String getHtmlEndTag() {
        return htmlEndTag;
    }

    private void setHtmlEndTag(String htmlEndTag) {
        this.htmlEndTag = htmlEndTag;
    }

    public boolean isCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(boolean cacheMode) {
        this.cacheMode = cacheMode;
        dir = "cache";
    }

    public void build() throws MalformedURLException {
        processesReader();
    }

    private String dir;

    public void setDir(String dir) {
        this.dir = dir;
    }

    private void processesReader() throws MalformedURLException {
        int indexFromLast = url.getPath().lastIndexOf('/'), contentLength = url.getPath().length();
        String path = url.getHost() + url.getPath().substring(indexFromLast + max(contentLength - indexFromLast - 15, 0), contentLength), fullPath = dir + path;
        System.out.println(path);
        File file = new File(fullPath);
        if (cacheMode && indexFromLast > 0 && file.isFile()) {
            try {
                bufferedReader = new BufferedReader(new FileReader(fullPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                cacheInFile(path);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw e;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cacheInFile(String path) {
        new File(dir).mkdirs();
        try (FileWriter fileWriter = new FileWriter(dir + path)) {
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                fileWriter.write(bufferedReader.readLine() + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }

    public WebsiteScraper(String stringURL, String htmlTag) {
        setStringURL(stringURL);
        this.htmlTag = htmlTag;
    }

    public WebsiteScraper(String stringURL, String htmlTag, boolean cacheMode) {
        setStringURL(stringURL);
        this.htmlTag = htmlTag;
        setCacheMode(cacheMode);

    }


}