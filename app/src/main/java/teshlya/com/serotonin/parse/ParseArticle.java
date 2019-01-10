package teshlya.com.serotonin.parse;

import android.content.Context;
import android.os.AsyncTask;
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
import java.util.HashMap;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import teshlya.com.serotonin.adapter.ArticleAdapter;
import teshlya.com.serotonin.bean.CommentBean;
import teshlya.com.serotonin.model.ArticleDataWithComment;
import teshlya.com.serotonin.model.CommentData;
import teshlya.com.serotonin.utils.TimeAgo;

public class ParseArticle extends AsyncTask<Void, Void, ArrayList<CommentData>> {
    private String url;
    private Context context;
    private ArticleAdapter adapter;
    private List<TreeNode> nodes;
    public static HashMap<String, ArrayList<CommentData>> hmap = new HashMap<String, ArrayList<CommentData>>();

    public ParseArticle(String url, Context context, ArticleAdapter adapter) {
        this.url = url;
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<CommentData> doInBackground(Void... params) {
        if (hmap.containsKey(url))
            return hmap.get(url);
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

        ArticleDataWithComment articleDataWithComment = new ArticleDataWithComment();

        try {
            JSONArray dataJsonObj = null;
            dataJsonObj = new JSONArray(resultJson);
            articleDataWithComment.setCommentData(parseComment(dataJsonObj.getJSONObject(1)));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        hmap.put(url, articleDataWithComment.getCommentData());
        return articleDataWithComment.getCommentData();
    }

    @Override
    protected void onPostExecute(ArrayList<CommentData> list) {
        super.onPostExecute(list);
        bind(list);
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
                if (data2.has("body_html")) {
                    comment.setBody(stringToHtml(data2.getString("body_html")));
                    if (data2.has("author"))
                        comment.setAuthor(data2.getString("author"));
                    if (data2.has("score"))
                        comment.setScore(processScore(data2.getInt("score")));
                    if (data2.has("created_utc"))
                        comment.setDate(processDate(data2.getLong("created_utc")));

                    if (data2.has("replies"))
                        if (!data2.getString("replies").equals("")) {
                            JSONObject replies = data2.getJSONObject("replies");
                            if (replies != null){
                                comment.setReplies(parseComment(replies));
                            }
                        }
                    commentData.add(comment);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commentData;
    }

    private String stringToHtml(String str) {
        str = str.replace("&lt;", "<");
        str = str.replace("&gt;", ">");
        str = str.replace("&amp;", "&");
        str = str.replace("\\n", "");
        return str;
    }

    private String processScore(int paramInt)
    {
        if (paramInt > 1000)
        {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append(new DecimalFormat("#.#").format(paramInt / 1000.0F));
            localStringBuilder.append("k");
            return localStringBuilder.toString();
        }
        return Integer.toString(paramInt);
    }

    private String processDate(Long milliseconds) {
        return new TimeAgo().timeAgo((milliseconds) * 1000);
    }

    public void bind(ArrayList<CommentData> list) {
        if (list == null) {
            Toast.makeText(context, "Error, load comment!", Toast.LENGTH_SHORT).show();
            return;
        }
        initData(list);
    }

    private void initData(ArrayList<CommentData> list) {
        nodes = new ArrayList<>();
        ArrayList<CommentData> commentData;
        commentData = new ArrayList<>();
        commentData.addAll(list);
        if (commentData != null)
            for (CommentData comment : commentData) {
                TreeNode<CommentBean> tempComment = new TreeNode<>(new CommentBean(comment.getComment()));
                if (comment.getReplies() != null && !comment.getReplies().isEmpty()){
                    tempComment.setChildList(setData(comment.getReplies()));
                }
                tempComment.expandAll();
                nodes.add(tempComment);
            }

        adapter.findDisplayNodes(nodes);
        adapter.notifyDataSetChanged();
        adapter.hideProgress();
    }



    private List<TreeNode> setData(ArrayList<CommentData> commentData) {
        List<TreeNode> nodes = new ArrayList<>();
        if (commentData != null) {
            for (CommentData comment : commentData) {
                TreeNode<CommentBean> tempComment = new TreeNode<>(new CommentBean(comment.getComment()));
                if (comment.getReplies() != null && !comment.getReplies().isEmpty())
                    tempComment.setChildList(setData(comment.getReplies()));
                tempComment.expandAll();
                nodes.add(tempComment);
            }
        }
        return nodes;
    }
}
