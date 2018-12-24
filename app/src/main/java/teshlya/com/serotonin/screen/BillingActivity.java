package teshlya.com.serotonin.screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import androidx.appcompat.app.AppCompatActivity;
import teshlya.com.serotonin.R;

public class BillingActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    private static final String PRODUCT_ID = "test_product";
    private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAijFB3LauVEC6cxbQYyn57BgCcnnfNa9Vdk8vKFiqWs9S0nu+YiWMz06dcNxaE0JRlS82C5gSmcWxewSOcJVc3iW36fxsYQrtqKGnLowoAnZpkJh5DeO5VBoIJ9lQmjZBrPz/8lpkT8lR1UWjdB3MU3Oi2qRzDl+6ypvebUJ2M8/2814R/P4BX2dTQP6zNGfzDhCPL/TjEW4HnZwqXd+hZvZfEhXTK+b0/tQRs9HKkmRRqfvGd+nQ0qe/41+wv8yE+UeEMb+Cm6fUMGeYF6DHjCEmEqlqKPgOs7a9Gm4Yy168cGicS7OkNf4k8THi/AV3QIPyz2jTxKUNQPhFpgSTvwIDAQAB";
    private static final String MERCHANT_ID = null;
    private Button buyButton;
    private BillingProcessor bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        buyButton = (Button)findViewById(R.id.buyButton);

        bp = new BillingProcessor(this, LICENSE_KEY, this);
        bp.initialize();
    }


    public void buyClick (View view)
    {
        //bp.purchase(BillingActivity.this, PRODUCT_ID);
        bp.subscribe(BillingActivity.this, PRODUCT_ID);
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Log.d("qwerty", "onProductPurchased");

    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d("qwerty", "onPurchaseHistoryRestored");

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d("qwerty", "onBillingError");

    }

    @Override
    public void onBillingInitialized() {
        Log.d("qwerty", "onBillingInitialized");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
