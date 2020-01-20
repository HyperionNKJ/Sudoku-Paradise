package com.example.user.sudokuparadise;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class PagerAdapter extends android.support.v4.view.PagerAdapter{
    private Context context;
    private List<Integer> layoutArray;
    private LayoutInflater inflater;

    protected PagerAdapter(Context context, List<Integer> layoutArray) {
        this.context = context;
        this.layoutArray = layoutArray;
        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return layoutArray.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(layoutArray.get(position), container, false);
        view.setTag(position);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}


