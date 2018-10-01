package teshlya.com.reddit.model;

public class Subscription {
    String content;
    String iconUrl;

    public Subscription(String content, String iconUrl) {
        this.content = content;
        this.iconUrl = iconUrl;
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
}
