package teshlya.com.reddit.parse;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import teshlya.com.reddit.callback.CallbackCommunity;
import teshlya.com.reddit.model.Subscription;

public class ParseCommunityDetails extends AsyncTask<Void, Void, String> {
    CallbackCommunity callbackCommunity;
    String content;
    Context context;

    public ParseCommunityDetails(String content, Context context, CallbackCommunity callbackCommunity) {
        this.content = content;
        this.context = context;
        this.callbackCommunity = callbackCommunity;
    }

    @Override
    protected String doInBackground(Void... params) {
        String resultJson = "";
        try {
            URL url = new URL("https://www.reddit.com/r/" + content + "/about.json");
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
            String iconUrl = null;
            String communityUrl = null;
            if (data.has("community_icon")) {
                iconUrl = data.getString("community_icon");
            }
            if (iconUrl == null || iconUrl.equals(""))
                if (data.has("icon_img"))
                    iconUrl = data.getString("icon_img");
            if (iconUrl == null || iconUrl.equals(""))
                iconUrl= "default";

            if (data.has("url")) {
                communityUrl = data.getString("url");
            }


            callbackCommunity.addCommunity(new Subscription(content, iconUrl, communityUrl));


        } catch (JSONException e) {
            Toast.makeText(context, "Error, load data!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
