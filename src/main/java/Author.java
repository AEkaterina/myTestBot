import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Author {
    private org.jsoup.nodes.Document document;
    private Document booksDoc;
    private String nameAuthor = "";
    private int valuesLikesBooks;
    private int valuesViewsBooks;
    private int valuesCommentsBooks;

    public Author(String name) {
        nameAuthor = name;
        connect();
    }

    private void connect() {
        try {
            document = Jsoup.connect("https://www.surgebook.com/" + nameAuthor).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        Elements namePerson = document.getElementsByClass("author-name bold");
        return namePerson.text();
    }

    public String getBio() {
        Elements bioPerson = document.getElementsByClass("author-bio");
        return bioPerson.text();
    }

    public String getImg() {
        Elements elements = document.getElementsByClass("user-avatar");
        String url = elements.attr("style");
        url = url.replace("background-image: url('", "");
        url = url.replace("');", "");
        return url;
    }

    public String getInfoPerson(){
        String info = "";
        info += "Имя: " + getName() + "\n";
        info += "Статус: " + getBio() + "\n";

        Elements names = document.getElementsByClass("info-stats-name");
        Elements values = document.getElementsByClass("info-stats-num");

        for(int i = 0; i < names.size(); i++)
            info += names.get(i).text() + ": " + values.get(i).text() + "\n";

        info += getBooks();
        return info;
    }

    public String getBooks() {
        try {
            booksDoc = Jsoup.connect("https://surgebook.com/TaSamayaRys/books/all").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = "\nСписок книг: \n";
        ArrayList<String> textUrlBooks = new ArrayList<>();

        Elements books = booksDoc.getElementsByClass("book_view_mv1v2_title");
        Elements booksUrl = booksDoc.getElementsByClass("book_view_mv1v2_cover");

        for (int i = 0; i < books.size(); i++) {
            text += books.get(i).text() + "\n";
            textUrlBooks.add(booksUrl.get(i).attr("href"));
        }

        HashMap<String, Integer> info = getStats(textUrlBooks);

        text += "\n\nКоличество лайков на книгах: " + valuesLikesBooks + "\n";
        text += "Количество просмотров на книгах: " + valuesViewsBooks + "\n";
        text += "Количество комментариев на книгах: " + valuesCommentsBooks + "\n";

        return text;
    }

    private HashMap<String, Integer> getStats(ArrayList<String> list) {
        HashMap<String, Integer> info = new HashMap<String, Integer>();
        info.put("Лайки", 0);
        info.put("Комментарии", 1);
        info.put("Просмотры", 2);

        for (String url: list) {
            try {
                booksDoc = Jsoup.connect(url).get();

                Elements elements = document.getElementsByClass("font-size-14 color-white ml-5");
                info.put("Лайки", info.get("Лайки") + Integer.parseInt(elements.get(0).text()));
                info.put("Комментарии", info.get("Комментарии") + Integer.parseInt(elements.get(1).text()));
                info.put("Просмотры", info.get("Просмотры") + Integer.parseInt(elements.get(2).text()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return info;
    }
}
