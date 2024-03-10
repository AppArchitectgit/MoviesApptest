import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.Movie
import com.example.moviesapp.R
import com.squareup.picasso.Picasso
import java.util.Locale

class MovieAdapter(
    private var movies: List<Movie>,
    private var onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(), Filterable {

    private var moviesFiltered: List<Movie> = movies

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = moviesFiltered[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return moviesFiltered.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var title: TextView = itemView.findViewById(R.id.movie_title)
        private var image: ImageView = itemView.findViewById(R.id.movie_id)
        private var releasedDate: TextView = itemView.findViewById(R.id.Released_date)
        private var rating: TextView = itemView.findViewById(R.id.Rating)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val movie = moviesFiltered[position]
                onItemClick(movie)
            }
        }

        fun bind(movie: Movie) {
            title.text = movie.title
            releasedDate.text = movie.release_date
            rating.text = getStarRating(movie.vote_average)
            if (movie.poster_path.isNotEmpty()) {
                Picasso.get().load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                    .error(R.drawable.ic_launcher_background)
                    .into(image)
            } else {
                image.setImageResource(R.drawable.ic_launcher_background)
            }
        }

        private fun getStarRating(rating: Double): String {
            val roundedRating = (rating / 2).toInt()
            return "★".repeat(roundedRating) + "☆".repeat(5 - roundedRating)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        moviesFiltered = newMovies
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val queryString = constraint?.toString()?.lowercase(Locale.ROOT)

                moviesFiltered = if (queryString.isNullOrEmpty()) {
                    movies
                } else {
                    movies.filter { movie ->
                        movie.title.lowercase(Locale.ROOT).contains(queryString)
                    }
                }

                filterResults.values = moviesFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                moviesFiltered = results?.values as? List<Movie> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}
