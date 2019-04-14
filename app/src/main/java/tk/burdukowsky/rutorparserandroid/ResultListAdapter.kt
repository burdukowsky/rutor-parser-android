package tk.burdukowsky.rutorparserandroid

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.content.Context
import android.view.LayoutInflater

class ResultListAdapter(context: Context, private var results: List<Result>) : BaseAdapter() {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: layoutInflater.inflate(R.layout.result, parent, false)
        val result = getItem(position)
        (view.findViewById(R.id.textViewResultTitle) as TextView).text = result.title
        (view.findViewById(R.id.textViewResultSize) as TextView).text = result.size
        return view
    }

    override fun getItem(position: Int): Result {
        return results[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return results.size
    }
}
