package com.capgemini.drmask

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capgemini.drmask.retrofitdatabase.ArticlesList
import com.capgemini.drmask.retrofitdatabase.NewsDbInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class NewsFragment : Fragment() {

    private var columnCount = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val key = resources.getString(R.string.api_key)
        val request = NewsDbInterface.getInstance().getHealthNews("https://newsapi.org/v2/top-headlines?category=health&country=in&apiKey=$key")

        request.enqueue(object :Callback<ArticlesList>{
            override fun onResponse(call: Call<ArticlesList>, response: Response<ArticlesList>) {
                if(response.isSuccessful){
                    val news = response.body()
                    Log.d("Retrofit","Success ")
                    if(news != null){
                        if (view is RecyclerView){
                            Log.d("Retrofit","RECYCLER")
                            view.adapter = NewsRecyclerViewAdapter(news.articles){
                                val url = it.url
                                val intent=Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArticlesList>, t: Throwable) {
                Log.d("Retrofit","Fail")
            }

        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
//                adapter = NewsRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                NewsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}