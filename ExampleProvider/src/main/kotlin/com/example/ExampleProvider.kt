package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class ExampleProvider : MainAPI() {
    override var mainUrl = "https://mlsbd.co"
    override var name = "MLSBD"
    override val hasMainPage = true
    override var lang = "bn"
    override val supportedTypes = setOf(TvType.Movie)

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(mainUrl).document
        val homeItems = mutableListOf<SearchResponse>()

        document.select("div.single-post").forEach { element ->
            val title = element.select("h2.post-title").text().trim()
            val url = element.select("div.thumb a").attr("href")
            val posterUrl = element.select("div.thumb img").attr("src")

            if (title.isNotEmpty() && url.isNotEmpty()) {
                homeItems.add(
                    newMovieSearchResponse(title, url, TvType.Movie) {
                        this.posterUrl = posterUrl
                    }
                )
            }
        }

        return newHomePageResponse(listOf(HomePageList("Latest Movies", homeItems)))
    }
}
