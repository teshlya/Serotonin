package teshlya.com.serotonin.screen;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import teshlya.com.serotonin.R;

public class QuestionFragment extends Fragment {

    public QuestionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        initGithub(view);
        initReddit(view);
        initTelegram(view);
    }

    private void initGithub(View view) {
        view.findViewById(R.id.github).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL("https://github.com/reddit");
            }
        });
    }

    private void initReddit(View view) {
        view.findViewById(R.id.reddit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL("https://www.reddit.com/");
            }
        });
    }

    private void initTelegram(View view) {
        view.findViewById(R.id.telegram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appName = "org.telegram.messenger";
                boolean isAppInstalled = isAppAvailable(getContext(), appName);
                if (isAppInstalled) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=reddittop"));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Telegram not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void openWebURL(String inURL) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
        startActivity(browse);
    }


}
