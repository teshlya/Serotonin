package teshlya.com.serotonin.parse;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import teshlya.com.serotonin.callback.CallbackArticleLoaded;
import teshlya.com.serotonin.model.ArticleData;
import teshlya.com.serotonin.model.CommunityData;
import teshlya.com.serotonin.model.Media;
import teshlya.com.serotonin.model.MediaType;
import teshlya.com.serotonin.utils.TimeAgo;

public class ParseCommunity extends AsyncTask<Void, Void, String> {
    private CallbackArticleLoaded callbackArticleLoaded;
    private String url;

    public ParseCommunity(CallbackArticleLoaded callbackArticleLoaded, String url) {
        this.callbackArticleLoaded = callbackArticleLoaded;
        this.url = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        String resultJson = "";
        try {
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            java.io.InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            StringBuffer buffer = new StringBuffer();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);

        try {
            CommunityData communityData = new CommunityData();
            JSONObject dataJsonObj = new JSONObject(strJson);
            JSONObject data = dataJsonObj.getJSONObject("data");
            communityData.setAfter(getAfter(data));
            communityData.setSubreddit(getSubreddit(data));
            JSONArray children = data.getJSONArray("children");
            communityData.setArticles(parseChildren(children));
            callbackArticleLoaded.addArticles(communityData);
        } catch (JSONException e) {
            callbackArticleLoaded.addArticles(null);
            e.printStackTrace();
        }
    }

    private String getAfter(JSONObject data) throws JSONException {
        String after = null;
        if (data.has("after") && !data.isNull("after")) {
            after = data.getString("after");
        }
        return after;
    }

    private String getSubreddit(JSONObject data) {
        String subreddit = null;
        try {
            subreddit = data.getJSONArray("children").
                    getJSONObject(0).
                    getJSONObject("data").getString("subreddit");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subreddit;
    }

    private ArrayList<ArticleData> parseChildren(JSONArray children) throws JSONException {
        ArrayList<ArticleData> articleData = new ArrayList<>();
        for (int i = 0; i < children.length(); i++) {
            ArticleData article = new ArticleData();
            JSONObject child = children.getJSONObject(i);
            JSONObject data = child.getJSONObject("data");

            article.setTitle(htmlToString(getTitle(data)));
            article.setAuthor(getAuthor(data));
            article.setText((htmlToString(getText(data))).trim());
            article.setDate(processDate(getDate(data)));
            article.setCommentCount(processComments(getCommentCount(data)) + " comments");
            article.setScore(processScore(getScore(data)));
            article.setUrl(getUrl(data));
            article.setMediaType(getMediaType(data));
            article.setSubreddit(getSubredditTitle(data));
            switch (article.getMediaType()) {
                case IMAGE:
                    article.setMediaImage(getImageMedia(data));
                    break;
                case MPD:
                    article.setMediaVideo(getMpdMedia(data));
                    article.setMediaImage(getImageMedia(data));
                    break;
                case GIF:
                    article.setMediaVideo(getGifMedia(data));
                    article.setMediaImage(getImageMedia(data));
                    break;
                case NONE:
                    break;
            }
            if (article.getMediaImage() == null && article.getMediaVideo() == null)
                article.setMediaType(MediaType.NONE);

            articleData.add(article);
        }
        return articleData;
    }

    private String getTitle(JSONObject data) throws JSONException {
        String title = "";
        if (data.has("title"))
            title = data.getString("title");
        return title;
    }

    private String getAuthor(JSONObject data) throws JSONException {
        String author = "";
        if (data.has("author"))
            author = data.getString("author");
        return author;
    }

    private String getText(JSONObject data) throws JSONException {
        String text = "";
        if (data.has("selftext_html") && !data.isNull("selftext_html"))
            text = data.getString("selftext_html");
        if (text == null || text.isEmpty())
            if (data.has("post_hint") &&
                    !data.isNull("post_hint") &&
                    data.getString("post_hint").equals("link"))
                if (data.has("url") && !data.isNull("url"))
                    text = "<a href=\"" + data.getString("url") + "\">" + data.getString("url") + "</a>";

        return text;
    }

    private Long getDate(JSONObject data) throws JSONException {
        Long date = null;
        if (data.has("created_utc"))
            date = data.getLong("created_utc");
        return date;
    }

    private Long getCommentCount(JSONObject data) throws JSONException {
        Long commentCount = null;
        if (data.has("num_comments"))
            commentCount = data.getLong("num_comments");
        return commentCount;
    }

    private int getScore(JSONObject data) throws JSONException {
        int score = 0;
        if (data.has("score"))
            score = data.getInt("score");
        return score;
    }

    private String getUrl(JSONObject data) throws JSONException {
        String url = "";
        if (data.has("permalink"))
            url = data.getString("permalink");
        return url;
    }


    private String getSubredditTitle(JSONObject paramJSONObject)
            throws JSONException
    {
        String str = "";
        if (paramJSONObject.has("subreddit")) {
            str = paramJSONObject.getString("subreddit");
        }
        return str;
    }

    private MediaType getMediaType(JSONObject data) throws JSONException {
        if (data.has("is_video"))
            if (data.getBoolean("is_video"))
                return MediaType.MPD;

        if (data.has("preview") && !data.isNull("preview")) {
            JSONObject preview = data.getJSONObject("preview");
            if (preview.has("reddit_video_preview") && !preview.isNull("reddit_video_preview")) {
                JSONObject redditVideoPreview = preview.getJSONObject("reddit_video_preview");

                if (redditVideoPreview.has("is_gif") &&
                        !redditVideoPreview.isNull("is_gif") &&
                        redditVideoPreview.getBoolean("is_gif")) {
                    return MediaType.GIF;
                }
            }
        }

        Boolean isSelf = true;
        if (data.has("is_self"))
            isSelf = data.getBoolean("is_self");
        if (isSelf) return MediaType.NONE;
        else
            return MediaType.IMAGE;
    }

    private Media getImageMedia(JSONObject data) throws JSONException {
        Media media;
        if (data.has("preview") && !data.isNull("preview")) {
            JSONObject preview;
            preview = data.getJSONObject("preview");
            JSONArray images = preview.getJSONArray("images");
            JSONObject image = images.getJSONObject(0);

            media = getImage3(image);
            if (isCorrect(media))
                return media;

            media = getImage(image);
            if (isCorrect(media))
                return media;
        }

        media = getThumbnail(data);
        if (isCorrect(media))
            return media;
        else
            return null;
    }

    private Media getImage3(JSONObject image) throws JSONException {
        Media media = new Media();
        if (image.has("resolutions") && !image.isNull("resolutions")) {
            JSONArray resolutions = image.getJSONArray("resolutions");
            if (resolutions.length() > 3) {
                JSONObject resolution = resolutions.getJSONObject(3);
                if (resolution.has("url"))
                    media.setUrl(htmlToString(resolution.getString("url")));
                if (resolution.has("width") && resolution.has("height")) {
                    media.setWidth(resolution.getInt("width"));
                    media.setHeight(resolution.getInt("height"));
                }
            }
        }
        return media;
    }

    private Media getImage(JSONObject image) throws JSONException {
        Media media = new Media();
        JSONObject source = image.getJSONObject("source");
        media.setUrl(htmlToString(source.getString("url")));
        media.setWidth(source.getInt("width"));
        media.setHeight(source.getInt("height"));
        return media;
    }

    private Media getThumbnail(JSONObject data) throws JSONException {
        Media media = new Media();
        if (data.has("thumbnail"))
            media.setUrl(htmlToString(data.getString("thumbnail")));
        if (data.has("thumbnail_width") && !data.isNull("thumbnail_width"))
            media.setWidth(data.getInt("thumbnail_width"));
        if (data.has("thumbnail_height") && !data.isNull("thumbnail_height"))
            media.setHeight(data.getInt("thumbnail_height"));
        return media;
    }

    private Media getMpdMedia(JSONObject data) throws JSONException {
        Media media = new Media();
        if (data.has("media")) {
            JSONObject mediaJson = data.getJSONObject("media");
            if (mediaJson.has("reddit_video")) {
                JSONObject redditVideo = mediaJson.getJSONObject("reddit_video");
                if (redditVideo.has("dash_url"))
                    media.setUrl(redditVideo.getString("dash_url"));
                if (redditVideo.has("width"))
                    media.setWidth(redditVideo.getInt("width"));
                if (redditVideo.has("height"))
                    media.setHeight(redditVideo.getInt("height"));
            }
        }
        return media;
    }

    private Media getGifMedia(JSONObject data) throws JSONException {
        Media media = new Media();
        if (data.has("preview")) {
            JSONObject mediaJson = data.getJSONObject("preview");
            if (mediaJson.has("reddit_video_preview")) {
                JSONObject redditVideo = mediaJson.getJSONObject("reddit_video_preview");
                if (redditVideo.has("dash_url"))
                    media.setUrl(redditVideo.getString("dash_url"));
                if (redditVideo.has("width"))
                    media.setWidth(redditVideo.getInt("width"));
                if (redditVideo.has("height"))
                    media.setHeight(redditVideo.getInt("height"));
            }
        }
        return media;
    }


    private boolean isCorrect(Media media) {
        if (media != null &&
                media.getUrl() != null &&
                !media.getUrl().isEmpty() &&
                !media.getUrl().equals("self") &&
                !media.getUrl().equals("default") &&
                media.getWidth() > 0 &&
                media.getHeight() > 0)
            return true;
        else
            return false;
    }

    private String htmlToString(String str) {
        str = str.replace("&lt;", "<");
        str = str.replace("&gt;", ">");
        str = str.replace("&amp;", "&");
        str = str.replace("\\n", "");
        return str;
    }

    private static String processScore(int paramInt)
    {
        if (paramInt < 1000) {
            return Integer.toString(paramInt);
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(new DecimalFormat("#.#").format(paramInt / 1000.0F));
        localStringBuilder.append("k");
        return localStringBuilder.toString();
    }

    private static String processDate(Long milliseconds) {
        return new TimeAgo().timeAgo((milliseconds) * 1000);
    }

    private static String processComments(Long paramLong)
    {
        if (paramLong.longValue() < 1000L) {
            return Long.toString(paramLong.longValue());
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(new DecimalFormat("#.#").format((float)paramLong.longValue() / 1000.0F));
        localStringBuilder.append("k");
        return localStringBuilder.toString();
    }
}
