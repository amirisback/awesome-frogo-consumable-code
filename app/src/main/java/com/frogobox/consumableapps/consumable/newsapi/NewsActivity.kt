package com.frogobox.consumableapps.consumable.newsapi

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.frogobox.consumableapps.R
import com.frogobox.consumableapps.base.BaseActivity
import com.frogobox.frogonewsapi.ConsumeNewsApi
import com.frogobox.frogonewsapi.callback.NewsResultCallback
import com.frogobox.frogonewsapi.data.model.Article
import com.frogobox.frogonewsapi.data.response.ArticleResponse
import com.frogobox.frogonewsapi.util.NewsConstant
import com.frogobox.frogonewsapi.util.NewsUrl
import com.frogobox.recycler.boilerplate.adapter.callback.FrogoAdapterCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_sport.*
import kotlinx.android.synthetic.main.content_list_main.view.*

class NewsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        setupDetailTitle()
        setupNewsApi()
    }

    private fun setupNewsApi() {
        val consumeNewsApi = ConsumeNewsApi(NewsUrl.NEWS_API_KEY)
        consumeNewsApi.usingChuckInterceptor(this)
        consumeNewsApi.getTopHeadline(
            null,
            null,
            null,
            NewsConstant.COUNTRY_ID,
            null,
            null,
            object : NewsResultCallback<ArticleResponse> {
                override fun failedResult(statusCode: Int, errorMessage: String?) {

                }

                override fun getResultData(data: ArticleResponse) {
                    setupRecyclerView(data.articles)
                }

                override fun onHideProgress() {

                }

                override fun onShowProgress() {

                }
            })
    }

    private fun setupRecyclerView(articles: List<Article>?) {

        val frogoadapterCallback = object : FrogoAdapterCallback<Article> {
            override fun onItemClicked(data: Article) {
                data.title?.let { showToast(it) }
            }

            override fun onItemLongClicked(data: Article) {
                data.title?.let { showToast(it) }
            }

            override fun setupInitComponent(view: View, data: Article) {
                Glide.with(this@NewsActivity).load(data.urlToImage).into(view.iv_icon)
                view.tv_title.text = data.title
                view.tv_link.text = data.author
            }
        }

        articles?.let {
            rv_news.injector<Article>()
                .addData(it)
                .addCustomView(R.layout.content_list_main)
                .addEmptyView(null)
                .addCallback(frogoadapterCallback)
                .createLayoutLinearVertical(false)
                .createAdapter()
                .build(rv_news)
        }


    }

}