package teshlya.com.serotonin.model;

public class DataMainMenu {
    private String title;
    private String detail;
    private int isHeader;

    public DataMainMenu(String title, String detail, int isHeader) {
        this.title = title;
        this.detail = detail;
        this.isHeader = isHeader;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int isHeader() {
        return isHeader;
    }

    public void setHeader(int header) {
        isHeader = header;
    }
}
