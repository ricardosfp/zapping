package com.ricardosfp.zapping.data.repository.implementation

import com.prof18.rssparser.*
import com.ricardosfp.zapping.*
import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import javax.inject.*

// todo test
@Singleton
class ZappingRepositoryImpl @Inject constructor(): ZappingRepository {

    // todo inject the RssParser
    private val parser = RssParser()

    // todo handle parser/network errors
    override suspend fun getArticles(): GetArticlesResult {

        return try {
            // the library automatically changes the context of the coroutine
            val channel = parser.getRssChannel(BuildConfig.ZAPPING_URL)

            // is it worth it putting this into a separate class for testing purposes?
            val articleList = channel.items.mapNotNull {
                val date = it.pubDate
                val title = it.title

                if (date != null && title != null) {
                    MyArticle(date, title)
                } else {
                    null
                }
            }

            GetArticlesSuccess(articleList)
        }
        catch (th: Throwable) {
            GetArticlesError(th)
        }

    }
}