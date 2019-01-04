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

    private static final String PRODUCT_ID_1 = "Product_1";
    private static final String PRODUCT_ID_2 = "Product_2";
    private static final String PRODUCT_ID_3 = "Product_3";
    private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAijFB3LauVEC6cxbQYyn57BgCcnnfNa9Vdk8vKFiqWs9S0nu+YiWMz06dcNxaE0JRlS82C5gSmcWxewSOcJVc3iW36fxsYQrtqKGnLowoAnZpkJh5DeO5VBoIJ9lQmjZBrPz/8lpkT8lR1UWjdB3MU3Oi2qRzDl+6ypvebUJ2M8/2814R/P4BX2dTQP6zNGfzDhCPL/TjEW4HnZwqXd+hZvZfEhXTK+b0/tQRs9HKkmRRqfvGd+nQ0qe/41+wv8yE+UeEMb+Cm6fUMGeYF6DHjCEmEqlqKPgOs7a9Gm4Yy168cGicS7OkNf4k8THi/AV3QIPyz2jTxKUNQPhFpgSTvwIDAQAB";
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
                openWebURL("https://github.com/reddit");
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
