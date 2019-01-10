package teshlya.com.serotonin.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

public class NestedScrollingParentRecyclerView extends RecyclerView {
    private boolean mChildIsScrolling = false;
    private int mTouchSlop;
    private float mOriginalX;
    private float mOriginalY;

    public NestedScrollingParentRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public NestedScrollingParentRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NestedScrollingParentRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mChildIsScrolling = false;
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mChildIsScrolling = false;
                setOriginalMotionEvent(ev);
            }
            case MotionEvent.ACTION_MOVE: {
                if (mChildIsScrolling) {
                    return false;
                }
                final int xDiff = calculateDistanceX(ev);
                final int yDiff = calculateDistanceY(ev);

                if (yDiff > mTouchSlop && yDiff > xDiff) {
                    mChildIsScrolling = true;
                    return false;
                }
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    public void setOriginalMotionEvent(MotionEvent ev) {
        mOriginalX = ev.getX();
        mOriginalY = ev.getY();
    }

    public int calculateDistanceX(MotionEvent ev) {
        return (int) Math.abs(mOriginalX - ev.getX());
    }

    public int calculateDistanceY(MotionEvent ev) {
        return (int) Math.abs(mOriginalY - ev.getY());
    }
}
