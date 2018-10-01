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
    private static int hour = 60 *60;
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

                String urlImage = "";
                if (data2.has("thumbnail"))
                    urlImage = data2.getString("thumbnail");
                article.setUrlImage(urlImage);

                Long date = null;
                if (data2.has("created_utc"))
                    date = data2.getLong("created_utc");
                article.setDate(processDate(date));

                articleData.add(article);
            }
            callbackArticle.addArticles(articleData);
        } catch (JSONException e) {
            Toast.makeText(context, "Error, load data!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String cleanString(String str) {
        return str
                .replace("[deleted]", "Comment deleted")
                .replace("&amp;#x200B;", "")
                .replace("&gt;", "");
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
        return new TimeAgo().timeAgo((milliseconds + hour * offsetFromUtc) * 1000);
    }
}
