package teshlya.com.serotonin.screen;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.utils.Calc;

public class MainMenuActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;
    private int cliskState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
    }

    private void init() {
        getArgs();
        initFragmentMainMemu();
        initBottomSheet();
        initClickEmptyActivityArea();
    }

    private void getArgs(){
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            cliskState = 0;
        } else {
            cliskState= extras.getInt("click");
        }
    }

    private void initFragmentMainMemu() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MainMenuFragment mainMenuFragment = MainMenuFragment.newInstance(cliskState);
        ft.replace(R.id.bottom_sheet, mainMenuFragment);
        ft.commit();
    }

    private void initBottomSheet() {
        View bottom_sheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottomSheetBehavior.setPeekHeight(Calc.getWindowSizeInPx(this).y * 62 / 100);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                    finish();
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void initClickEmptyActivityArea() {
        findViewById(R.id.empty_activity_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
