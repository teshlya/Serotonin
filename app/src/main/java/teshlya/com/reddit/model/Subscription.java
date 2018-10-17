package teshlya.com.reddit.model;

public class Subscription {
    String content;
    String iconUrl;
    String communityUrl;
    int iconResource;

    public Subscription(String content, String iconUrl, String communityUrl) {
        this.content = content;
        this.iconUrl = iconUrl;
        this.communityUrl = communityUrl;
    }

    public Subscription(String content, String communityUrl, int iconResource) {
        this.content = content;
        this.communityUrl = communityUrl;
        this.iconResource = iconResource;
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

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }
}
