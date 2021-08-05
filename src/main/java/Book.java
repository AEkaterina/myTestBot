import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Book {
    private Document document;
    private String url;

    public Book(String url) {
        this.url = url;
        connect();
    }

    private void connect() {
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return document.title();
    }

    public String getLikes() {
        Element element = document.getElementById("likes");
        return element.text();
    }

    public String getDescription() {
        Element element = document.getElementById("description");
        return element.text();
    }

    public String getGeneres() {
        Elements element = document.getElementsByClass("genres d-block");
        return element.text();
    }

    public String getCommentList() {
        Elements element = document.getElementsByClass("comment_mv1_wrapper_block");

        String comment = element.text();
        comment=comment.replaceAll("Ответить", "\n\n");
        comment=comment.replaceAll("Нравится", "");
        comment=comment.replaceAll("\\d{4}-\\d{2}-\\d{2}", "");
        comment=comment.replaceAll("\\d{4}-\\d{2}-\\d{2}", "");
        return  comment;
    }

    public String getImg() {
        Elements elements = document.getElementsByClass("cover-book");
        String url = elements.attr("style");
        url = url.replace("background-image: url('", "");
        url = url.replace("');", "");
        return url;
    }

    public String getAuthorName() {
        Elements elements = document.getElementsByClass("text-decoration-none column-author-name bold max-w-140 text-overflow-ellipsis");
        return elements.text();
    }
}
