package teshlya.com.serotonin.model;

import java.util.ArrayList;

public class SearchResult {
    private ArrayList<String> communities;
    private String after;

    public ArrayList<String> getCommunities() {
        return communities;
    }

    public void setCommunities(ArrayList<String> communities) {
        this.communities = communities;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
