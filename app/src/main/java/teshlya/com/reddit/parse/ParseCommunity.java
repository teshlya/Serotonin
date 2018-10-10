package teshlya.com.reddit.parse;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import teshlya.com.reddit.TimeAgo;
import teshlya.com.reddit.callback.CallbackArticle;
import teshlya.com.reddit.callback.CallbackComment;
import teshlya.com.reddit.model.ArticleData;

public class ParseCommunity extends AsyncTask<Void, Void, String> {
    private static int offsetFromUtc = TimeZone.getDefault().getOffset(new Date().getTime()) / 3600000;
    private static int hour = 60 * 60;
    private CallbackArticle callbackArticle;
    private String url;
    private Context context;

    public ParseCommunity(CallbackArticle callbackArticle, String url, Context context) {
        this.callbackArticle = callbackArticle;
        this.url = url;
        this.context = context;
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
            JSONObject dataJsonObj = null;
            dataJsonObj = new JSONObject(strJson);
            JSONObject data = dataJsonObj.getJSONObject("data");
            JSONArray children = data.getJSONArray("children");

            ArrayList<ArticleData> articleData = new ArrayList<>();

            for (int i = 0; i < children.length(); i++) {
                ArticleData article = new ArticleData();
                JSONObject child = children.getJSONObject(i);
                JSONObject data2 = child.getJSONObject("data");

                String title = "";
                if (data2.has("title"))
                    title = data2.getString("title");
                article.setTitle(title);

                String author = "";
                if (data2.has("author"))
                    author = data2.getString("author");
                article.setAuthor(author);

                String text = "";
                if (data2.has("selftext_html") && !data2.isNull("selftext_html"))
                    text = data2.getString("selftext_html");

                article.setText(stringToHtml(text));

                String urlImage = "";

                JSONObject preview;
                if (data2.has("preview") && !data2.isNull("preview")) {
                    preview = data2.getJSONObject("preview");
                    JSONArray images = preview.getJSONArray("images");
                    JSONObject image = images.getJSONObject(0);
                    JSONObject source = image.getJSONObject("source");
                    urlImage = source.getString("url");
                }

                if (urlImage == null || urlImage.equals(""))
                    if (data2.has("thumbnail"))
                        urlImage = data2.getString("thumbnail");
                if (urlImage == null || urlImage.equals(""))
                    if (data2.has("media") && !data2.isNull("media")) {
                        JSONObject media;
                        media = data2.getJSONObject("media");
                        if (media.has("oembed")) {
                            JSONObject oembed;
                            oembed = media.getJSONObject("oembed");
                            if (oembed.has("thumbnail_url"))
                                urlImage = oembed.getString("thumbnail_url");
                        }
                    }

                article.setUrlImage(urlImage);


                Long date = null;
                if (data2.has("created_utc"))
                    date = data2.getLong("created_utc");
                article.setDate(processDate(date));

                Long commentCount = null;
                if (data2.has("num_comments"))
                    commentCount = data2.getLong("num_comments");
                article.setCommentCount(processComments(commentCount));

                int score = 0;
                if (data2.has("score"))
                    score = data2.getInt("score");
                article.setScore(processScore(score));

                Boolean withoutImage = true;
                if (data2.has("is_self"))
                    withoutImage = data2.getBoolean("is_self");
                article.setWithoutImage(withoutImage);

                String url = "";
                if (data2.has("permalink"))
                    url = data2.getString("permalink");
                article.setUrl(url);

                articleData.add(article);
            }
            callbackArticle.addArticles(articleData);
        } catch (JSONException e) {
            Toast.makeText(context, "Error, load data!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String stringToHtml(String str){
        str = str.replace("&lt;", "<");
        str = str.replace("&gt;", ">");
        str = str.replace("&amp;", "&");
        str = str.replace("\\n", "");
        return str;
    }


    private static String processScore(int score) {
        String point = " points";
        String result = Integer.toString(score);
        if (score > 1000)
            result = new DecimalFormat("#.#").format((float) score / 1000) + "k";

        if (score == 1)
            point = " point";
        return result + point;
    }

    private static String processDate(Long milliseconds) {
        return new TimeAgo().timeAgo((milliseconds) * 1000);
    }

    private static String processComments(Long commentsCount) {
        String result = Long.toString(commentsCount);
        if (commentsCount <= 1)
            result = result + " comment_icon";
        else
            result = result + " comments";

        return result;
    }
}
