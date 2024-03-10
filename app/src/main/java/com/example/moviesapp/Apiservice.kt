import com.example.moviesapp.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String = RetrofitInstance.API_KEY): Response<MovieResponse>
}
