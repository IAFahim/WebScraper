import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import static java.lang.Math.*;

public class WebsiteScraper implements Runnable {

    private String stringURL;
    private String htmlTag;
    private String htmlEndTag;
    private boolean cacheMode;
    private URL url;
    private BufferedReader bufferedReader;
    private final Stack<String> cacheStack = new Stack<>();
    private String dir = "cache";

    public WebsiteScraper(String stringURL, String htmlTag) {
        setStringURL(stringURL);
        this.htmlTag = htmlTag;
    }

    public WebsiteScraper(String stringURL, String htmlTag, boolean cacheMode) {
        setStringURL(stringURL);
        this.htmlTag = htmlTag;
        setCacheMode(cacheMode);
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
                cacheStack.push(path);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw e;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cacheInFile(String path, BufferedReader bufferedReader) {
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

    public void build() throws MalformedURLException {
        processesReader();
    }

    @Override
    public void run() {
        if (cacheMode) {
            while (!cacheStack.isEmpty()) {
                cacheInFile(cacheStack.pop(), this.bufferedReader);
            }
        }
    }

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

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setUrl() {
        try {
            setUrl(new URL(stringURL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setHtmlEndTag(String htmlEndTag) {
        this.htmlEndTag =  htmlEndTag;
    }

    public boolean isCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(boolean cacheMode) {
        this.cacheMode = cacheMode;
        setDir(dir);
    }

    public void setDir(String dir) {
        this.dir = dir + '\\';
    }

    public String getHtmlEndTag() {
        return htmlEndTag;
    }

    public void setHtmlTag(Boolean hasEndTag, String htmlTag) {
        this.htmlTag = '<' + htmlTag + '>';
        if (hasEndTag)
            setHtmlEndTag("<\"" + htmlTag + '>');
    }

    public String getHtmlTag() {
        return htmlTag;
    }

}