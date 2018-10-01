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

import teshlya.com.reddit.callback.CallbackComment;
import teshlya.com.reddit.model.ArticleDataWithComment;
import teshlya.com.reddit.model.CommentData;

public class ParseArticle extends AsyncTask<Void, Void, String> {
    CallbackComment callbackComment;
    String url;
    Context context;

    public ParseArticle(CallbackComment callbackComment, String url, Context context) {
        this.callbackComment = callbackComment;
        this.url = url;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String resultJson = "";
        try {
            URL url = new URL(this.url);
            Log.d("qwerty", this.url);

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
            JSONArray dataJsonObj = null;
            dataJsonObj = new JSONArray(strJson);
            JSONObject data1 = dataJsonObj.getJSONObject(0);
            JSONObject data2 = data1.getJSONObject("data");
            JSONArray children = data2.getJSONArray("children");
            JSONObject child = children.getJSONObject(0);
            JSONObject child3 = child.getJSONObject("data");

            String title="";
            if (child3.has("title"))
            title = child3.getString("title");

            String imageUrl = "";
            if (child3.has("url"))
            imageUrl = child3.getString("url");

            ArticleDataWithComment articleDataWithComment = new ArticleDataWithComment();
            articleDataWithComment.setTitle(title);
            articleDataWithComment.setImageUrl(imageUrl);

            articleDataWithComment.setCommentData(parseComment(dataJsonObj.getJSONObject(1)));
            callbackComment.showDataOnScreen(articleDataWithComment);

        } catch (JSONException e) {
            Toast.makeText(context, "Error, load data!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private ArrayList<CommentData> parseComment(JSONObject jsonObject) {
        ArrayList<CommentData> commentData = new ArrayList<>();
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray children = data.getJSONArray("children");
            for (int i = 0; i < children.length(); i++) {
                JSONObject child = children.getJSONObject(i);
                JSONObject data2 = child.getJSONObject("data");
                CommentData comment = new CommentData();
                if (data2.has("body")) {
                    comment.setBody(cleanString(data2.getString("body")));
                    if (data2.has("author"))
                        comment.setAuthor(data2.getString("author"));
                    if (data2.has("score"))
                        comment.setScore(processScore(data2.getInt("score")));
                    if (data2.has("created"))
                        comment.setDate(processDate(data2.getLong("created")));

                    if (data2.has("replies"))
                        if (!data2.getString("replies").equals("")) {
                            JSONObject replies = data2.getJSONObject("replies");
                            if (replies != null)
                                comment.setReplies(parseComment(replies));
                        }
                    commentData.add(comment);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commentData;
    }

    private String cleanString(String str) {
        return str
                .replace("[deleted]", "Comment deleted")
                .replace("&amp;#x200B;", "")
                .replace("&gt;", "");
    }

    private String processScore(int score) {
        String point = " points";
        String result = Integer.toString(score);
        if (score > 1000)
            result = new DecimalFormat("#.#").format((float) score / 1000) + "k";

        if (score == 1)
            point = " point";
        return result + point;
    }

    private String processDate(Long milliseconds) {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(milliseconds * 1000);
        String result = "";

        int diff;
        if ((diff = getDateDiff(cl, Calendar.YEAR)) > 0)
            result = diff + ((diff == 1) ? " year" : " years");
        else if ((diff = getDateDiff(cl, Calendar.MONTH)) > 0)
            result = diff + ((diff == 1) ? " month" : " month");
        else if ((diff = getDateDiff(cl, Calendar.DAY_OF_MONTH)) > 0)
            result = diff + ((diff == 1) ? " day" : " days");
        else if ((diff = getDateDiff(cl, Calendar.HOUR)) > 0)
            result = diff + ((diff == 1) ? " hour" : " hours");
        else if ((diff = getDateDiff(cl, Calendar.MINUTE)) > 0)
            result = diff + ((diff == 1) ? " minute" : " minutes");
        else if ((diff = getDateDiff(cl, Calendar.SECOND)) > 0)
            result = diff + ((diff == 1) ? " second" : " seconds");

        return result + " ago";
    }

    private static int getDateDiff(Calendar cl, int period) {
        return Calendar.getInstance().get(period) - cl.get(period);
    }
}
