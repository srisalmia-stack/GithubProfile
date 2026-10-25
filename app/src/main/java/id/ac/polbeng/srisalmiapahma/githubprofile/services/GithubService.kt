package id.ac.polbeng.srisalmiapahma.githubprofile.services

import id.ac.polbeng.srisalmiapahma.githubprofile.models.GithubUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GithubService {
    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String,
        @Header("Authorization") token: String? = null
    ): Call<GithubUser>
}
