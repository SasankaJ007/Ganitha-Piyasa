package lk.learningApp.helper.UIUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kikt.view.CustomRatingBar;

import java.util.ArrayList;
import java.util.List;

public class NonSwipeRatingBar extends CustomRatingBar {
    private List<ImageView> list = new ArrayList<>();
    public NonSwipeRatingBar(Context context) {
        super(context);
        init();
    }
    public NonSwipeRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public NonSwipeRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    void init(){
        for(int i = 0; i < this.mMaxStar; ++i) {
            ImageView child = (ImageView) this.getChildAt(i);
            this.list.add(child);
        }
    }
    @Override
    public void setStars(float stars) {
        super.setStars(stars*2);
        this.setView();
    }
    public void setView() {
        if (this.stars < this.mMinStar * 2.0F) {
            this.stars = this.mMinStar * 2.0F;
        }
        int stars = (int)this.stars;
        int i;
        if (stars % 2 == 0) {
            for(i = 0; i < this.mMaxStar; ++i) {
                if (i < stars / 2) {
                    this.setFullView((ImageView)this.list.get(i));
                } else {
                    this.setEmptyView((ImageView)this.list.get(i));
                }
            }
        } else {
            for(i = 0; i < this.mMaxStar; ++i) {
                if (i < stars / 2) {
                    this.setFullView((ImageView)this.list.get(i));
                } else if (i == stars / 2) {
                    this.setHalfView((ImageView)this.list.get(i));
                } else {
                    this.setEmptyView((ImageView)this.list.get(i));
                }
            }
        }
    }
}
