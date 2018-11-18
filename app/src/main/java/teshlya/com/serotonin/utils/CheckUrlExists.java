package teshlya.com.serotonin.utils;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

import teshlya.com.serotonin.callback.ExistsUrlCallback;

public class CheckUrlExists extends AsyncTask<Void, Void, Boolean> {

    ExistsUrlCallback existsUrlCallback;
    String subreddit;

    public CheckUrlExists(ExistsUrlCallback existsUrlCallback, String subreddit) {
        this.existsUrlCallback = existsUrlCallback;
        this.subreddit = subreddit;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            HttpURLConnection.setFollowRedirects(false);
            String url = "https://www.reddit.com/r/" + subreddit + "/.json";
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            System.out.println(con.getResponseCode());
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        existsUrlCallback.sendResult(result, subreddit);
       /* boolean bResponse = result;
        if (bResponse==true)
        {
            Toast.makeText(MainActivity.this, "File exists!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(MainActivity.this, "File does not exist!", Toast.LENGTH_SHORT).show();
        }*/
    }
}

