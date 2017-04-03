package com.example.nino.tvlistingmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nino.tvlistingmovies.MoviesListingFragment.OnListFragmentInteractionListener;
import com.example.nino.tvlistingmovies.utils.MovieContent.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MovieItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private final List<MovieItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;

    public MovieRecyclerViewAdapter(List<MovieItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movie_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.bindMovieData(holder.mItem);

        /**
         * Click Listener for the List View Item position
         */
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                    //Factory Method, used to initiate a fragment.
                    //IDEA Mudar para um argumento único
                    MovieDetailsFragment details = MovieDetailsFragment
                            .newInstance(holder.mItem.mId,
                                    holder.mItem.mTitle,
                                    holder.mItem.mAvgVotes,
                                    holder.mItem.mTotalVotes,
                                    holder.mItem.mReleaseDate,
                                    holder.mItem.mOverview,
                                    holder.mItem.mBackdrop);

                    ((MainActivity)mContext).loadFragment(details);
                }
            }
        });
    }

    /**
     *
     * @return Current Item count
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //A ViewHolder describes an item view and metadata about its place within the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder{


        public final View mView;
        public final ImageView mMoviePoster;
        public final TextView mMovieTitle;
        public final RatingBar mMovieRattingBar;
        public final TextView mMovieVoteCount;
        public final TextView mMovieOverview;
        public MovieItem mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mMoviePoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            mMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            mMovieRattingBar = (RatingBar) itemView.findViewById(R.id.rb_vote_average);
            mMovieRattingBar.setFocusable(false);
            mMovieVoteCount = (TextView) itemView.findViewById(R.id.tv_vote_count);
            mMovieOverview = (TextView) itemView.findViewById(R.id.tv_overview);

        }

        /**
         * Used to"bind" de details of the movie.
         *
         * @param movieData Object contains all the information to populate the view with information.
         */
        public void bindMovieData(MovieItem movieData){
            mItem = movieData;
            Picasso.with(mMoviePoster.getContext())
                    .load(mItem.mPoster)
                    .placeholder(R.drawable.ic_image_photo)
                    .error(R.drawable.ic_image_error)
                    .into(mMoviePoster);
            mMovieTitle.setText(movieData.mTitle);
            mMovieVoteCount.setText(String.valueOf(movieData.mTotalVotes));
            mMovieOverview.setText(movieData.mOverview);
            mMovieRattingBar.setRating(movieData.mAvgVotes);
        }

    }
}
