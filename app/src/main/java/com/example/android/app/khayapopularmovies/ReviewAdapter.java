package com.example.android.app.khayapopularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by noybs on 03/07/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    public ArrayList<String> reviews;
    private final String TAG = ReviewAdapter.class.getSimpleName();
    private int mReviewsCount;
    private Context mContext;

    public ReviewAdapter(Context context){
        this.mContext = context;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{
        TextView mReviewTextView;
        public ReviewHolder(View itemView){
            super(itemView);
            mReviewTextView = (TextView) itemView.findViewById(R.id.tv_review);
        }
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmedietly = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmedietly);
        ReviewHolder reviewHolder = new ReviewHolder(view);

        return reviewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.mReviewTextView.setText(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        if (null == reviews) return 0;
        return reviews.size();
    }

    public void setReviews(ArrayList<String> reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }
}
