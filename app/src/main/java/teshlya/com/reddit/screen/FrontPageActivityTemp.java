package teshlya.com.reddit.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import me.saket.inboxrecyclerview.InboxRecyclerView;
import me.saket.inboxrecyclerview.dimming.CompleteListTintPainter;
import me.saket.inboxrecyclerview.page.ExpandablePageLayout;
import teshlya.com.reddit.BlankFragment;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.MyAdapter;

import android.graphics.Color;
import android.os.Bundle;

public class FrontPageActivityTemp extends AppCompatActivity {

    InboxRecyclerView recyclerView;
    MyAdapter myAdapter;
    ExpandablePageLayout expandablePageLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page_temp);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        expandablePageLayout = findViewById(R.id.inbox_email_thread_page);
        expandablePageLayout.setAnimationDurationMillis(1000);
        expandablePageLayout.setPullToCollapseEnabled(false);
        BlankFragment blankFragment = new BlankFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(expandablePageLayout.getId(), blankFragment)
                .commitNowAllowingStateLoss();
        recyclerView.setExpandablePage(expandablePageLayout);
        recyclerView.setTintPainter(new CompleteListTintPainter(Color.WHITE, 0.65F));

        myAdapter = new MyAdapter(recyclerView);
        recyclerView.setAdapter(myAdapter);
    }
}
