package teshlya.com.serotonin.callback;

import java.util.ArrayList;

import teshlya.com.serotonin.model.CommunityData;
import teshlya.com.serotonin.model.SearchResult;

public interface CallbackSubredditFound {
    void addSubredditsToListMenu(SearchResult searchResult, String str);
}
