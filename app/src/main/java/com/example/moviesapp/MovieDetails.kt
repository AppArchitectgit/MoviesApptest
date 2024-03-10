package com.example.moviesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

import com.example.moviesapp.databinding.ActivityMovieDetailsBinding
import com.squareup.picasso.Picasso

class MovieDetails : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBarMovieDetails) // Setting toolbar as support action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) //default app name  removed

        // Receive data from intent
        val title = intent.getStringExtra("title")
        val backdropPath = intent.getStringExtra("backdropPath")
        val releasedDate = intent.getStringExtra("ReleasedDate")
        val rating = intent.getStringExtra("Rating")?.toDoubleOrNull()
        val overview = intent.getStringExtra("overview")

        //setting toolbar text
        binding.movieTitleInToolbar.text = title
        binding.releasedDateMovie.text = releasedDate
        binding.ratingMovie.text = rating.toString()
        binding.OverviewText.text = overview



        //it refers to object from it is being called
        rating?.let {
            binding.ratingMovie.text = getStarRating(it)
        }



        //Loading image using picasso library
        if (!backdropPath.isNullOrEmpty()) {
            Picasso.get().load("https://image.tmdb.org/t/p/original$backdropPath")
                .error(R.drawable.ic_launcher_background)
                .into(binding.imageDetailed)
        }
    }

    private fun getStarRating(rating: Double): String {
        val roundedRating = (rating / 2).toInt()
        return "★".repeat(roundedRating) + "☆".repeat(5 - roundedRating)
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Going back to the previous activity when the toolbar back button is clicked
        return true
    }
}