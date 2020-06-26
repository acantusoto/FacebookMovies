package com.example.facebookmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.facebookmovies.MovieDetailsActivity;
import com.example.facebookmovies.R;
import com.example.facebookmovies.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;

    }

    //Usually inflates layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter","onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);

    }


    //Involves populating data into the item thought the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter","onBindViewHolder" + position);
        //get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the VH
        holder.bind(movie);

    }

    //Returns total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);

        }



        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            //if phone is in landscape
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//                imageUrl = movie.getBackdropPath();
                int radius = 30; // corner radius, higher value = more rounded
                int margin = 0; // crop margin, set to 0 for corners with no crop
                Glide.with(context)
                        .load(movie.getBackdropPath())
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(ivPoster);
            }
            //then image url is = backdrop image
            else{
                //imageUrl = movie.getPosterPath();
                int radius = 30; // corner radius, higher value = more rounded
                int margin = 0; // crop margin, set to 0 for corners with no crop
                Glide.with(context)
                        .load(movie.getPosterPath())
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(ivPoster);
            }

//            int radius = 30; // corner radius, higher value = more rounded
//            int margin = 0; // crop margin, set to 0 for corners with no crop
//            Glide.with(context)
//                    .load(imageUrl).fitCenter()
//                    .into(ivPoster);
            //Glide.with(context).load(imageUrl).into(ivPoster);
        }

        @Override
        public void onClick(View view) {

            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }

        }
    }
}
