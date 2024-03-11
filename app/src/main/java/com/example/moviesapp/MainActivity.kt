package com.example.moviesapp


import ApiService
import MovieAdapter
import android.content.Context

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moviesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.*



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var database: MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchView()

        // Initialize Room database
        Log.d("Database", "Creating database...")
        database = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "my_database"
        ).build()

        // Checking internet connection and fetch data accordingly
        if (isNetworkAvailable()) {
            fetchMoviesFromAPI()
        } else {
            //fetching Movies From Database()
            fetchMoviesFromDatabase()
            binding.textViewInternet.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        binding.searchView.clearFocus()
        super.onPause()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter(emptyList()) { movie ->
            navigateToMovieDetails(movie)
        }
        binding.recyclerViewMovies.adapter = movieAdapter
    }

    private fun navigateToMovieDetails(movie: Movie) {
        val intent = Intent(this@MainActivity, MovieDetails::class.java).apply {
            putExtra("title", movie.title)
            putExtra("backdropPath", movie.backdrop_path)
            putExtra("ReleasedDate", movie.release_date)
            putExtra("Rating", movie.vote_average.toString()) // Convert rating to string
            putExtra("overview", movie.overview)
        }
        startActivity(intent)
    }

    private fun setupSearchView() {
        binding.searchView.queryHint = "Search for a movie"
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                movieAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }






    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchMoviesFromAPI() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = RetrofitInstance.retrofit.create(ApiService::class.java).getPopularMovies()
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    movieResponse?.let {
//                        for (movie in it.results) {
//
//                            //insertMovieIntoDatabase(movie)
////
//                        }
                        movieAdapter.updateMovies(it.results)
                    }
                } else {
                    Log.e("FetchMoviesError", "Failed to fetch movies from API: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FetchMoviesError", "Error fetching movies from API: ${e.message}", e)
            }
        }
    }







    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchMoviesFromDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            try {

                val viewedMovies = database.movieDao().getAllMovies().map { it.toMovie() }
                movieAdapter.updateMovies(viewedMovies)
            } catch (e: Exception) {
                Log.e("DatabaseFetchError", "Error fetching movies from database: ${e.message}", e)
            }
        }
    }






    @OptIn(DelicateCoroutinesApi::class)
    private fun insertMovieIntoDatabase(movie: Movie) {
        GlobalScope.launch(Dispatchers.IO) {
            // Checking here if the movie already exists in the database
            val existingMovie = database.movieDao().getMovieByTitle(movie.title)
            if (existingMovie == null) {
                // Movie doesn't exist, insert it into the database
                val movieEntity = MovieEntity(
                    title = movie.title,
                    rating = movie.vote_average,
                    releaseDate = movie.release_date,
                    overview = movie.overview,
                    posterPath = movie.poster_path,
                    backdropPath = movie.backdrop_path
                )
                database.movieDao().insertMovies(movieEntity)
                Log.d("DatabaseInsert", "Inserted movie: ${movie.title}")
            } else {
                Log.d("DatabaseInsert", "Movie already exists: ${movie.title}")
            }
        }
    }
}



fun MovieEntity.toMovie(): Movie {
    return Movie(
        title = this.title,
        vote_average = this.rating,
        release_date = this.releaseDate,
        overview = this.overview,
        poster_path = this.posterPath,
        backdrop_path = this.backdropPath
    )
}