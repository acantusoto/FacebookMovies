package com.example.facebookmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facebookmovies.databinding.ActivityMovieDetailsBinding;
import com.example.facebookmovies.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {


    // the movie to display
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_details);

        // We are binding
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);

        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));


        // set the title and overview
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        Log.d("MovieDetailsActivity", String.format("Movie url for '%s'", movie.getBackdropPath()));
        Glide.with(this).load(movie.getBackdropPath()).into(binding.ivPoster);
    }

    public void onClick(View view) {
            // create intent for the new activity
            Intent intent = new Intent(this, MovieTrailerActivity.class);
            // serialize the movie using parceler, use its short name as a key
            intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
            // show the activity
            this.startActivity(intent);

    }
}

