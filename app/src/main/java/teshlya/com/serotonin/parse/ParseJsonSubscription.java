package teshlya.com.serotonin.parse;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import teshlya.com.serotonin.R;
import teshlya.com.serotonin.model.SubscriptionsGroup;

public class ParseJsonSubscription {

    private Context context;

    public ParseJsonSubscription(Context context) {
        this.context = context;
    }

    public ArrayList<SubscriptionsGroup> getSubscription() {
        String json = readJson();
        if (json != null)
            return parse(json);
        return null;
    }

    private String readJson() {
        InputStream is = context.getResources().openRawResource(R.raw.data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        return writer.toString();
    }

    private ArrayList<SubscriptionsGroup> parse(String json) {
        ArrayList<SubscriptionsGroup> subscriptionsGroups = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONArray data = jsonObj.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                SubscriptionsGroup group = new SubscriptionsGroup();
                JSONObject groupJson = data.getJSONObject(i);
                group.title = groupJson.getString("cat");
                group.icon = groupJson.getString("icon");
                group.subscriptions = new ArrayList<>();
                JSONArray sub = groupJson.getJSONArray("sub");
                for (int j = 0; j < sub.length(); j++) {
                    group.subscriptions.add(sub.getString(j));
                }
                subscriptionsGroups.add(group);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subscriptionsGroups;
    }
}
