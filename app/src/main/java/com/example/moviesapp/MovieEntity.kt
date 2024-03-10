package com.example.moviesapp
import androidx.room.Entity

import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val rating: Double, // Changed from Double to String to store stars directly
    val releaseDate: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String
)
