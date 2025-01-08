import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.ApiService
import com.katielonsdale.chatterbox.api.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/v0/"
//    private const val BASE_URL = "https://chatter-box-be-c1487dd4c370.herokuapp.com/api/v0/"

    fun createApiService(sessionManager: SessionManager): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
