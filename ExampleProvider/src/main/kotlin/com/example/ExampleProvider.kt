package com.example
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class MlsbdProvider : MainAPI() {
    override var mainUrl = "https://mlsbd.co"
    override var name = "MLSBD"
    override val hasMainPage = true
    override var lang = "bn" // যেহেতু এটি বাংলাদেশি সাইট
    override val supportedTypes = setOf(TvType.Movie)

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        // ওয়েবসাইটের মেইন পেজ লোড করা
        val document = app.get(mainUrl).document
        val homeItems = mutableListOf<SearchResponse>()

        // HTML থেকে মুভির লিস্ট (single-post) বের করা
        document.select("div.single-post").forEach { element ->
            
            // মুভির নাম, লিঙ্ক এবং ছবির লিঙ্ক বের করা
            val title = element.select("h2.post-title").text().trim()
            val url = element.select("div.thumb a").attr("href")
            val posterUrl = element.select("div.thumb img").attr("src")

            // যদি টাইটেল এবং লিঙ্ক পাওয়া যায়, তবেই লিস্টে যোগ করবে
            if (title.isNotEmpty() && url.isNotEmpty()) {
                homeItems.add(
                    newMovieSearchResponse(title, url, TvType.Movie) {
                        this.posterUrl = posterUrl
                    }
                )
            }
        }

        // অ্যাপের হোমপেজে "Latest Movies" নামে লিস্টটি দেখানো
        return newHomePageResponse(listOf(HomePageList("Latest Movies", homeItems)))
    }
}
