package teshlya.com.reddit.model;

public class Subscription {
    String content;
    String iconUrl;
    String communityUrl;

    public Subscription(String content, String iconUrl, String communityUrl) {
        this.content = content;
        this.iconUrl = iconUrl;
        this.communityUrl = communityUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCommunityUrl() {
        return communityUrl;
    }

    public void setCommunityUrl(String communityUrl) {
        this.communityUrl = communityUrl;
    }
}
