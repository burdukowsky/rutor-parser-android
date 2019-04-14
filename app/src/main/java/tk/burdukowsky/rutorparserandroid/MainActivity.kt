package tk.burdukowsky.rutorparserandroid

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import javax.inject.Inject
import android.os.AsyncTask

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleIntent(intent)
        DaggerAppComponent.builder().build().inject(this)
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
            GetResultsTask().execute(query)
        }
    }

    class GetResultsTask : AsyncTask<String, Void, List<Result>>() {
        @Inject
        lateinit var apiService: ApiService

        override fun onPreExecute() {
            DaggerAppComponent.builder().build().inject(this)
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String): List<Result>? {
            return apiService.getResults(params[0]).execute().body()
        }

        override fun onPostExecute(result: List<Result>) {
            super.onPostExecute(result)
        }
    }
}
