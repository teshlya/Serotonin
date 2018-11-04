package teshlya.com.reddit.callback;

import java.util.ArrayList;

import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.model.CommunityData;

public interface CallbackArticleLoaded {
    void addArticles(CommunityData data);
}
