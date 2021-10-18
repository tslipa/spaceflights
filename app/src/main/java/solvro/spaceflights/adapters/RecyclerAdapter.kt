package solvro.spaceflights.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import solvro.spaceflights.FragmentAll
import solvro.spaceflights.R
import solvro.spaceflights.api.Article


class RecyclerAdapter(
    private val dataSet: List<Article>?,
    private val mContext: Context,
    private val fragment: FragmentAll
) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        private val view: View,
        private val fragment: FragmentAll,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(view) {
        val textDate: TextView = view.findViewById(R.id.text_date)
        val textTitle: TextView = view.findViewById(R.id.text_title)
        val textSummary: TextView = view.findViewById(R.id.text_summary)
        private val imageIsFavourite: ImageView = view.findViewById(R.id.image_isfavourite)

        init {
            view.setOnClickListener {
                fragment.startActivityArticle(view.tag as Int)
            }

            imageIsFavourite.setOnClickListener {
                val value = if (context.getSharedPreferences("favourites", Context.MODE_PRIVATE)
                        .getString(view.tag.toString(), "N") == "N") {
                    "Y"
                } else {
                    "N"
                }
                val editor =
                    context.getSharedPreferences("favourites", Context.MODE_PRIVATE).edit()
                editor.putString(view.tag.toString(), value)
                editor.apply()

                setImageIsFavourite()
            }
        }

        fun setImageIsFavourite() {
            val resource = if (context.getSharedPreferences("favourites", Context.MODE_PRIVATE)
                    .getString(view.tag.toString(), "N") == "N"
            ) {
                R.drawable.star1
            } else {
                R.drawable.star2
            }

            imageIsFavourite.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    resource,
                    null
                )
            )
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_item, viewGroup, false)
        return ViewHolder(view, fragment, mContext)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = dataSet!![position]
        holder.itemView.tag = article.id
        holder.setImageIsFavourite()
        holder.textDate.text = mContext.getString(
            R.string.date_format,
            article.updatedAt?.subSequence(0, 10),
            article.updatedAt?.subSequence(11, 19)
        )
        holder.textTitle.text = article.title
        holder.textSummary.text = if (article.summary!!.length <= 75) {
            article.summary
        } else {
            StringBuilder().append(article.summary.substring(0, 75)).append("...").toString()
        }
    }

    override fun getItemCount(): Int {
        return dataSet!!.size
    }
}