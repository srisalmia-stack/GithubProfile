package id.ac.polbeng.srisalmiapahma.githubprofile.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceConfig {
    companion object {
        const val BASE_URL = "https://api.github.com/"

        fun getGithubService(): GithubService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(GithubService::class.java)
        }
    }
}
