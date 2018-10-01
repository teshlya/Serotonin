package teshlya.com.reddit.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import teshlya.com.reddit.callback.CallbackComment;
import teshlya.com.reddit.parse.ParseArticle;
import teshlya.com.reddit.R;
import teshlya.com.reddit.model.ArticleDataWithComment;

public class ArticleFragment extends Fragment implements CallbackComment {

    TextView title;
    ImageView imageView;

    public ArticleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        title = view.findViewById(R.id.title);
        imageView = view.findViewById(R.id.image);
        (new ParseArticle(this, "https://www.reddit.com/r/pics/comments/9helmu.json", getContext())).execute();
    }


    @Override
    public void showDataOnScreen(ArticleDataWithComment articleDataWithComment) {
        Picasso.with(getContext()).load(articleDataWithComment.getImageUrl()).into(imageView);

        title.setText(articleDataWithComment.getTitle());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.comments, new CommentsFragment(articleDataWithComment.getCommentData()));
        ft.commit();
    }
}
