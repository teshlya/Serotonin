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

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import androidx.fragment.app.Fragment;
import teshlya.com.serotonin.R;

public class QuestionFragment extends Fragment implements BillingProcessor.IBillingHandler {

    private static final String PRODUCT_ID_1 = "product_1";
    private static final String PRODUCT_ID_2 = "product_2";
    private static final String PRODUCT_ID_3 = "product_3";
    private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhI1UriB5hYcvcqpytPrU4qYleSQ5JN1JGr3tXPWgsbGAdangtvvq1OOhCVNwqBH4VInPA6cR99XbtLpg/MeBN9fupDb16FpZ5JdMqTxa8KMVPxsSFSn6doKYBj4uCFrhnFW0OSPLxhFwcIZQ2lA/ZCYLvtRNN78iqOZuFsT4uc4jRRs/4FehBYjrJtZxDbOfNQV7741dFdVyZMwp79nb2hKma2YkR+IcefR2J1feNBK7yFkDIBTRdGwC7kHozKFj9gomayA/t3KOaipI13EN8nS9h/zPa0Nipd9RuvozkYPmzG/dwGn/2qyT9Ly2xFIyHO5EfNjC7NNEpSeb7i7lNwIDAQAB";
    private BillingProcessor bp;


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
        bp = new BillingProcessor(getContext(), LICENSE_KEY, this);
        bp.initialize();

        initUSD1(view);
        initUSD2(view);
        initUSD3(view);
        initMarket(view);
        initGithub(view);
        initReddit(view);
        initTelegram(view);
        initShare(view);
    }

    private void initUSD1(View view) {
        view.findViewById(R.id.usd1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.subscribe(getActivity(), PRODUCT_ID_1);
            }
        });
    }

    private void initUSD2(View view) {
        view.findViewById(R.id.usd2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.subscribe(getActivity(), PRODUCT_ID_2);
            }
        });
    }

    private void initUSD3(View view) {
        view.findViewById(R.id.usd3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.subscribe(getActivity(), PRODUCT_ID_3);
            }
        });
    }

    private void initGithub(View view) {
        view.findViewById(R.id.github).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL("https://github.com/teshlya/Serotonin");
            }
        });
    }

    private void initShare(View view) {
        view.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Serotonin for Reddit");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getContext().getPackageName() + " \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                }
            }
        });
    }

    private void initMarket(View view) {
        view.findViewById(R.id.market).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getContext().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }

    private void initReddit(View view) {
        view.findViewById(R.id.reddit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL("https://www.reddit.com/r/serotoninapp/");
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
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=serotoninapp"));
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


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}
