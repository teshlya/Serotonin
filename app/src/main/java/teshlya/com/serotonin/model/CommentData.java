package teshlya.com.serotonin.model;

import java.util.ArrayList;

public class CommentData extends Comment{
    ArrayList<CommentData> replies;

    public ArrayList<CommentData> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<CommentData> replies) {
        this.replies = replies;
    }

}
