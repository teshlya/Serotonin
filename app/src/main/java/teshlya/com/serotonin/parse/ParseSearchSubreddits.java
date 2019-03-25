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

import teshlya.com.serotonin.callback.CallbackSubredditFound;
import teshlya.com.serotonin.model.Media;
import teshlya.com.serotonin.model.MediaType;
import teshlya.com.serotonin.model.SearchResult;
import teshlya.com.serotonin.utils.TimeAgo;

public class ParseSearchSubreddits extends AsyncTask<Void, Void, String> {
    private CallbackSubredditFound callbackSubredditFound;
    private String url;
    private String str;

    public ParseSearchSubreddits(CallbackSubredditFound callbackSubredditFound, String url, String str) {
        this.callbackSubredditFound = callbackSubredditFound;
        this.url = url;
        this.str = str;
        Log.d("qwerty",url);
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
            SearchResult searchResult = new SearchResult();
            JSONObject dataJsonObj = new JSONObject(strJson);
            JSONObject data = dataJsonObj.getJSONObject("data");
            searchResult.setAfter(getAfter(data));
            JSONArray children = data.getJSONArray("children");
            searchResult.setCommunities(parseChildren(children));
            callbackSubredditFound.addSubredditsToListMenu(searchResult, str);
        } catch (JSONException e) {
            callbackSubredditFound.addSubredditsToListMenu(null, str);
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

    private ArrayList<String> parseChildren(JSONArray children) throws JSONException {
        ArrayList<String> communities = new ArrayList<>();
        for (int i = 0; i < children.length(); i++) {
            JSONObject child = children.getJSONObject(i);
            JSONObject data = child.getJSONObject("data");
            if (data.has("display_name") && !data.isNull("display_name")) {
                String communitie = "";
                communitie = data.getString("display_name");
                communities.add(communitie);
            }
        }
        return communities;
    }
}
