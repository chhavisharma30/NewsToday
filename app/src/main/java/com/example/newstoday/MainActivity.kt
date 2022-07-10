package com.example.newstoday

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsViewAdapter(this)
        recyclerView.adapter=mAdapter
    }

    private fun fetchData(){
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=88a127e0a1824f2397f3e97d4f9f278f"
                val jsonObjectRequest =object: JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    { response ->
                        val newsJsonArray = response.getJSONArray("articles")
                        val newsArray = ArrayList<News>()
                        for(i in 0 until newsJsonArray.length()) {
                            val newsJsonObject = newsJsonArray.getJSONObject(i)
                            val news = News(
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("author"),
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage")
                            )
                            newsArray.add(news)
                        }

                        mAdapter.updateNews(newsArray)
                    },
                    { _ ->

                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["User-Agent"] = "Mozilla/5.0"
                        return headers
                    }
                }
                MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
            }

            override fun onItemClicked(item: News) {
                val builder =  CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(this, Uri.parse(item.url))
            }
        }