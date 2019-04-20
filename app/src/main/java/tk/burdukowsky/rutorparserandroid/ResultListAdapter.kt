package tk.burdukowsky.rutorparserandroid

import android.content.ActivityNotFoundException
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.content.Context
import android.view.LayoutInflater
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast

class ResultListAdapter(private var context: Context, private var results: List<Result>) : BaseAdapter() {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: layoutInflater.inflate(R.layout.result, parent, false)
        val result = getItem(position)
        view.findViewById<TextView>(R.id.textViewResultTitle).text = result.title
        view.findViewById<TextView>(R.id.textViewResultSize).text = result.size
        view.findViewById<TextView>(R.id.textViewResultSeeds).text = context.getString(R.string.seeds, result.seeds)
        view.findViewById<TextView>(R.id.textViewResultLeaches).text = context.getString(R.string.leaches, result.leaches)
        view.findViewById<ImageView>(R.id.imageButtonResultMagnet).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.magnet))
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.not_found_application),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
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
