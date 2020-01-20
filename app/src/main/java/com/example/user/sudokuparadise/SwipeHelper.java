package com.example.user.sudokuparadise;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerViewAdapter recyclerViewAdapter;

    public SwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public SwipeHelper(RecyclerViewAdapter recyclerViewAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT);
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        recyclerViewAdapter.deleteSuccessFailureInstance(viewHolder.getAdapterPosition());
    }
}

