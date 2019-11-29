package com.example.complaintsystembeta.ui.complaints;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.model.ReportForward;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImagesSwipe extends PagerAdapter {
    Context mContext;

    private List<String> urls;

    ImagesSwipe(Context c, List<String> list){
        this.mContext = c;
        this.urls = list;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageView imageView  = new ImageView(mContext);
        Picasso.get().load(Constants.URL_IMAGES + urls.get(position)).into(imageView);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
