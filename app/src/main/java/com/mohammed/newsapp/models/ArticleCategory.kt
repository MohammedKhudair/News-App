package com.mohammed.newsapp.models

import com.mohammed.newsapp.models.ArticleCategory.*

enum class ArticleCategory(val categoryName: String) {
    TECHNOLOGY("technology"),
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
}


fun getAllArticleCategory(): List<ArticleCategory> {
    return listOf(
        BUSINESS, ENTERTAINMENT,GENERAL,HEALTH,SCIENCE,SPORTS,TECHNOLOGY)
}


fun getArticleCategory(category:String) :ArticleCategory?{
    val map = values().associateBy(ArticleCategory::categoryName)
    return map[category]
}