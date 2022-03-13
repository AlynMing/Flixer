package com.example.flixter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

private const val TAG = "DetailActivity"
private const val YOUTUBE_API_KEY = "AIzaSyBSMFRph2-kqaAlLVV336Yum00g0UFHqkQ"
class DetailActivity : YouTubeBaseActivity() {

    private lateinit var tvTitle : TextView
    private lateinit var tvOverview : TextView
    private lateinit var ratingBar : RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        ratingBar = findViewById(R.id.rbVoteAverage)
        ytPlayerView = findViewById(R.id.player)


        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie

        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating = movie.rating.toFloat()

        ytPlayerView.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess")
                player?.cueVideo("5xVh-7ywKpE")
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitilizationFailure")
            }
        })
    }
}