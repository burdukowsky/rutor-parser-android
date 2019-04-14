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

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutProgress: LinearLayout
    private lateinit var list: ListView
    private lateinit var empty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleIntent(intent)

        linearLayoutProgress = findViewById(R.id.linearLayoutProgress)
        list = findViewById(android.R.id.list)
        empty = findViewById(android.R.id.empty)
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
    internal constructor(context: MainActivity) : AsyncTask<String, Void, List<Result>>() {

        private val activityReference: WeakReference<MainActivity> = WeakReference(context)
        private val apiService: ApiService = ApiServiceProvider.instance

        override fun onPreExecute() {
            val activity = activityReference.get()
            if (activityEnded(activity)) return
            activity!!.linearLayoutProgress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String): List<Result>? {
            return apiService.getResults(params[0]).execute().body() // todo: error catch
        }

        override fun onPostExecute(results: List<Result>) {
            val activity = activityReference.get()
            if (activityEnded(activity)) return
            activity!!
            activity.linearLayoutProgress.visibility = View.GONE
            activity.list.adapter = ResultListAdapter(activity, results)
            activity.empty.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
        }

        private fun activityEnded(activity: Activity?): Boolean {
            return activity == null || activity.isFinishing
        }
    }

}
