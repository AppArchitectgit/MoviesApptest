import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: Any
        get() {
            TODO()
        }
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "13444352f8cb6b6d374ecf7cc323c4ed"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //use for json parsing
            .build()
    }
}
