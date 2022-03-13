package com.example.flixter

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

// this is needed for some of the Glide features to work
@GlideModule
class MyAppGlideModule : AppGlideModule() { }

private const val TAG = "MovieAdapter"
const val MOVIE_EXTRA = "MOVIE_EXTRA"
class MovieAdapter(private val context: Context, private val movies: List<Movie>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val POPULARMOVIECUTOFF = 7.0
    private val NORMALMOVIE = 0
    private val POPULARMOVIE = 1




    // Expensive operation: create a view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val viewHolder : RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(context)

        when (viewType) {
            NORMALMOVIE -> {
                val view = inflater.inflate(R.layout.item_movie, parent, false)
                viewHolder = DefaultViewHolder(view)
            }
            POPULARMOVIE -> {
                val view = inflater.inflate(R.layout.item_popular_movie, parent, false)
                viewHolder = PopularViewHolder(view)
            }
            else -> {
                Log.e("MovieAdapter", "Unhandled movie type" + viewType)
                val view = inflater.inflate(R.layout.item_movie, parent, false)
                viewHolder = DefaultViewHolder(view)
            }
        }

        return viewHolder
    }

    // Cheap: simply bind data to an existing viewholder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            NORMALMOVIE -> {
                (holder as DefaultViewHolder).bind(movies[position])
            }
            POPULARMOVIE -> {
                (holder as PopularViewHolder).bind(movies[position])
                Log.i("POPULAR", movies[position].toString())
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (movies[position].rating > POPULARMOVIECUTOFF) {
            return POPULARMOVIE
        } else {
            return NORMALMOVIE
        }
    }

    override fun getItemCount() =  movies.size

    abstract inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        init {
            itemView.setOnClickListener(this)
        }
        abstract fun bind(movie: Movie);

        override fun onClick(p0: View?) {
            // 1. Get notified of the particular movie which was tapped on
            val movie = movies[adapterPosition]

            // 2. Use the intent system to navigate to the new screen
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movie)
            context.startActivity(intent)
        }

    }

    inner class DefaultViewHolder(itemView: View) : MovieViewHolder(itemView) {


        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)
        private val ivMovieImg = itemView.findViewById<ImageView>(R.id.ivMovieImg)

        override fun bind(movie: Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview

            val orientation = context.resources.configuration.orientation
            val imgUrlToUse = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                movie.backdropImageUrl
            } else {
                movie.posterImageUrl
            }
            // render either the backdrop or the poster based on orientation
            Glide.with(context)
                .load(imgUrlToUse)
                .placeholder(R.drawable.imagenotfound)
                .into(ivMovieImg)
        }

    }

    inner class PopularViewHolder(itemView: View) : MovieViewHolder(itemView) {

        private val ivMoviePoster = itemView.findViewById<ImageView>(R.id.moviePoster)

        override fun bind(movie: Movie) {
            Glide.with(context)
                .load(movie.backdropImageUrl)
                .placeholder(R.drawable.imagenotfound)
                .into(ivMoviePoster)
        }
    }
}
