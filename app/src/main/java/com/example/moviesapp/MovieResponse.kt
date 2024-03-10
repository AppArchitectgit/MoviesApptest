package com.example.moviesapp

data class MovieResponse (val results: List<Movie>)

data class Movie ( val id: Int = 0, // Provided a default value for id
                   val genreIds: List<Int> = emptyList(), // Provided a default value for genreIds
                   val title: String,
                   val vote_average: Double,
                   val release_date: String,
                   val overview: String,
                   val poster_path: String,
                   val backdrop_path: String)

