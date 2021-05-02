package com.capgemini.drmask

data class ArticlesDetails(
    val title:String,
    val url:String,
    val urlToImage:String,
    val description:String,
    )

data class ArticlesList(
    val articles: List<ArticlesDetails>
)