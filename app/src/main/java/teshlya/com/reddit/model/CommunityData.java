package teshlya.com.reddit.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CommunityData implements Serializable {
    private ArrayList<ArticleData> articles;
    private String after;

    public ArrayList<ArticleData> getArticles() {
        if (articles == null)
            articles = new ArrayList<>();
        return articles;
    }

    public void setArticles(ArrayList<ArticleData> articles) {
        this.articles = articles;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
