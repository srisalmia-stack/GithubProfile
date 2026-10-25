package id.ac.polbeng.srisalmiapahma.githubprofile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import id.ac.polbeng.srisalmiapahma.githubprofile.databinding.ActivityMainBinding
import id.ac.polbeng.srisalmiapahma.githubprofile.models.GithubUser
import id.ac.polbeng.srisalmiapahma.githubprofile.services.ServiceConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = getString(R.string.app_name)

        binding.btnSearch.setOnClickListener {
            val username = binding.etUsername.text.toString()
            if (username.isNotEmpty()) {
                hideKeyboard()
                searchUser(username)
            } else {
                Toast.makeText(this, getString(R.string.please_enter_username), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchUser(username: String) {
        showLoading(true)
        val service = ServiceConfig.getGithubService()
        val call = service.getUser(username)

        call.enqueue(object : Callback<GithubUser> {
            override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        displayUser(user)
                    }
                } else {
                    binding.cvProfile.visibility = View.GONE
                    Toast.makeText(this@MainActivity, getString(R.string.user_not_found, response.message()), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                showLoading(false)
                binding.cvProfile.visibility = View.GONE
                Toast.makeText(this@MainActivity, getString(R.string.error, t.message), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayUser(user: GithubUser) {
        binding.cvProfile.visibility = View.VISIBLE
        binding.tvName.text = user.name ?: getString(R.string.no_name)
        binding.tvUsername.text = getString(R.string.username_format, user.login)
        binding.tvBio.text = user.bio ?: getString(R.string.no_bio)
        binding.tvFollowers.text = user.followers.toString()
        binding.tvFollowing.text = user.following.toString()
        binding.tvRepos.text = user.publicRepos.toString()

        Glide.with(this)
            .load(user.avatarUrl)
            .into(binding.imgAvatar)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etUsername.windowToken, 0)
    }
}
