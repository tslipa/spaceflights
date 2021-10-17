package solvro.spaceflights.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import solvro.spaceflights.R
import solvro.spaceflights.retrofit.GSONArticle


class RecyclerAdapter(private val dataSet: List<GSONArticle>?, private val mContext: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textTitle = view.findViewById<TextView>(R.id.text_title)

        init {
            view.setOnClickListener {
                //fragment.callDetails(view.tag as String, layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = dataSet!![position]
        holder.itemView.tag = position
        holder.textTitle.text = article.title
    }

    override fun getItemCount(): Int {
        return dataSet!!.size
    }
}