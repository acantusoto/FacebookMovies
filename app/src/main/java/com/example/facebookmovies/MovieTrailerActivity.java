package com.example.facebookmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.facebookmovies.databinding.ActivityMovieDetailsBinding;
import com.example.facebookmovies.databinding.ActivityMovieTrailerBinding;
import com.example.facebookmovies.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public static final String API_KEY = "f1cc5258f770587bdee59fdae894bb9c";
    public static String TAG = "YouTubeBaseActivity";
    String NOW_PLAYING_URL;
    Movie movie;
    String videoId = null;
    JSONArray results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieTrailerBinding binding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        NOW_PLAYING_URL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s" , movie.getId(),API_KEY);
        Log.d(TAG, "onCreate: "+NOW_PLAYING_URL);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG,"onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: "+results.toString());
                    videoId = results.getJSONObject(0).getString("key");
                    Log.i(TAG, "Id1: "+results.getJSONObject(0).getString("key"));
                    Log.i(TAG, "Id2: "+videoId);

                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    endActivity();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                Log.d(TAG,"onFailure");
                endActivity();
            }
        });




        // resolve the player view from the layout
        YouTubePlayerView playerView = binding.player;


            // initialize with API key stored in secrets.xml
            playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                    YouTubePlayer youTubePlayer, boolean b) {
                    Log.d(TAG, "onInitializationSuccess: videoId: " + videoId);
//                    // do any work here to cue video, play video, etc.
//                    youTubePlayer.cueVideo(videoId);
//                    youTubePlayer.play();
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                    YouTubeInitializationResult youTubeInitializationResult) {
                    // log the error
                    Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                }
            });

            endActivity();


    }




    public void endActivity(){
        Intent intent = new Intent(this, MovieDetailsActivity.class);// New activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // serialize the movie using parceler, use its short name as a key
        intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
        startActivity(intent);
        finish();
    }
}