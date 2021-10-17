package solvro.spaceflights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import solvro.spaceflights.retrofit.RetrofitClientInstance.retrofitInstance
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback
import retrofit2.Response
import solvro.spaceflights.adapters.RecyclerAdapter
import solvro.spaceflights.retrofit.GSONArticle
import solvro.spaceflights.retrofit.GetDataService


class FragmentAll: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val service = retrofitInstance!!.create(
            GetDataService::class.java
        )

        val call: Call<List<GSONArticle>?>? = service.articles
        call!!.enqueue(object: Callback<List<GSONArticle>?> {
            override fun onResponse(
                call: Call<List<GSONArticle>?>,
                response: Response<List<GSONArticle>?>
            ) {
                val recyclerView = view!!.findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = RecyclerAdapter(response.body(), activity!!)
            }

            override fun onFailure(call: Call<List<GSONArticle>?>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}