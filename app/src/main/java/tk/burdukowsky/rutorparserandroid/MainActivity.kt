package tk.burdukowsky.rutorparserandroid

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import java.lang.ref.WeakReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutProgress: LinearLayout
    private lateinit var list: ListView
    private lateinit var empty: TextView
    private lateinit var errorMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleIntent(intent)

        linearLayoutProgress = findViewById(R.id.linearLayoutProgress)
        list = findViewById(android.R.id.list)
        empty = findViewById(android.R.id.empty)
        errorMessage = findViewById(R.id.errorMessage)
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
            val query = intent.getStringExtra(SearchManager.QUERY)
            GetResultsTask(this).execute(query)
        }
    }

    private class GetResultsTask
    internal constructor(context: MainActivity) : AsyncTask<String, Void, Call<List<Result>>>() {

        private val activityReference: WeakReference<MainActivity> = WeakReference(context)
        private val apiService: ApiService = ApiServiceProvider.instance

        override fun onPreExecute() {
            val activity = activityReference.get()
            if (activityEnded(activity)) return
            activity!!
            setViewVisibility(activity.linearLayoutProgress, true)
            setViewVisibility(activity.empty, false)
            setViewVisibility(activity.errorMessage, false)
        }

        override fun doInBackground(vararg params: String): Call<List<Result>> {
            return apiService.getResults(params[0])
        }

        override fun onPostExecute(call: Call<List<Result>>) {
            val activity = activityReference.get()
            if (activityEnded(activity)) return
            activity!!
            setViewVisibility(activity.linearLayoutProgress, false)

            call.enqueue(
                object : Callback<List<Result>> {
                    override fun onResponse(call: Call<List<Result>>, response: Response<List<Result>>) {
                        val results = response.body()
                        if (response.isSuccessful && results != null) {
                            activity.list.adapter = ResultListAdapter(activity, results)
                            setViewVisibility(activity.empty, results.isEmpty())
                        } else {
                            activity.errorMessage.text =
                                activity.getString(R.string.error_message, response.code(), "") // todo: message
                            setViewVisibility(activity.errorMessage, true)
                            activity.list.adapter = ResultListAdapter(activity, Collections.emptyList())
                        }
                    }

                    override fun onFailure(call: Call<List<Result>>, t: Throwable) {
                        // todo: дублирование
                        activity.errorMessage.text = activity.getString(R.string.request_failed_message)
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

}
