
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
//TODO SUGGESTION This noybs chap has written large portions of your submission - or so it seems :^)

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    public final TrailerAdapterOnClickHandler mClickHandler;
    public ArrayList<String> trailers;
    private final String TAG = TrailerAdapter.class.getSimpleName();
    private int mTrailersCount;
    private Context mContext;
    private final String TRAILER_LABEL = "Trailer";

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler, Context context){
        this.mClickHandler = clickHandler;
        mTrailersCount = getItemCount();
        this.mContext = context;
    }

    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTrailerTextView;
        public TrailerHolder(View itemView){
            super(itemView);
            mTrailerTextView = (TextView) itemView.findViewById(R.id.tv_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String trailer = trailers.get(adapterPosition);
            mClickHandler.onClick(trailer);
        }
    }

    public interface TrailerAdapterOnClickHandler{
        void onClick(String trailer);
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmedietly = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmedietly);
        TrailerHolder reviewHolder = new TrailerHolder(view);

        return reviewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        holder.mTrailerTextView.setText(TRAILER_LABEL + " " + (position + 1));
    }

    @Override
    public int getItemCount() {
        if (null == trailers) return 0;
        return trailers.size();
    }

    public void setReviews(ArrayList<String> reviews){
        this.trailers = reviews;
        notifyDataSetChanged();
    }
}
