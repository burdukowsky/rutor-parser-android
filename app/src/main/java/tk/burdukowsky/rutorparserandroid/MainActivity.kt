package tk.burdukowsky.rutorparserandroid

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import java.lang.ref.WeakReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private val queryMinimumLength = 3

    private val apiService: ApiService = ApiServiceProvider.instance

    private lateinit var linearLayoutProgress: LinearLayout
    private lateinit var list: ListView
    private lateinit var empty: TextView
    private lateinit var errorMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutProgress = findViewById(R.id.linearLayoutProgress)
        list = findViewById(android.R.id.list)
        empty = findViewById(android.R.id.empty)
        errorMessage = findViewById(R.id.errorMessage)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            setIconifiedByDefault(false)
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY).trim()
            if (query.length >= queryMinimumLength) {
                getResults(query)
            } else {
                Toast
                    .makeText(
                        this,
                        this.getString(R.string.query_minimum_length, queryMinimumLength),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
    }

    private fun getResults(query: String) {
        setViewVisibility(this.linearLayoutProgress, true)
        setViewVisibility(this.empty, false)
        setViewVisibility(this.errorMessage, false)

        val context = this

        apiService.getResults(query).enqueue(
            object : Callback<List<Result>> {
                private val activityReference: WeakReference<MainActivity> = WeakReference(context)

                override fun onResponse(call: Call<List<Result>>, response: Response<List<Result>>) {
                    val activity = activityReference.get()
                    if (activityEnded(activity)) return
                    activity!!

                    val results = response.body()
                    if (response.isSuccessful && results != null) {
                        activity.list.adapter = ResultListAdapter(activity, results)
                        setViewVisibility(activity.empty, results.isEmpty())
                    } else {
                        var errorMessage = activity.getString(R.string.error_message, response.code())
                        val errorBody = response.errorBody()
                        if (errorBody != null) {
                            val apiError = ErrorConverterProvider.instance.convert(errorBody)
                            if (apiError != null) {
                                errorMessage += ": ${apiError.message}"
                            }
                        }
                        showErrorMessage(activity, errorMessage)
                    }

                    complete(activity)
                }

                override fun onFailure(call: Call<List<Result>>, t: Throwable) {
                    val activity = activityReference.get()
                    if (activityEnded(activity)) return
                    activity!!
                    showErrorMessage(activity, activity.getString(R.string.request_failed_message))
                    complete(activity)
                }

                private fun complete(activity: MainActivity) {
                    setViewVisibility(activity.linearLayoutProgress, false)
                }

                private fun showErrorMessage(activity: MainActivity, message: String) {
                    activity.errorMessage.text = message
                    setViewVisibility(activity.errorMessage, true)
                    activity.list.adapter = ResultListAdapter(activity, Collections.emptyList())
                }
            }
        )
    }

    private fun activityEnded(activity: Activity?): Boolean {
        return activity == null || activity.isFinishing
    }

    private fun setViewVisibility(view: View, status: Boolean) {
        view.visibility = if (status) View.VISIBLE else View.GONE
    }

}
