package teshlya.com.reddit.callback;

import java.util.ArrayList;

import teshlya.com.reddit.model.ArticleData;

public interface CallbackArticle {
    void addArticles(ArrayList<ArticleData> articles);
}
