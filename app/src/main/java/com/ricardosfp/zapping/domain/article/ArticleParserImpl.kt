package com.ricardosfp.zapping.domain.article

import com.prof.rssparser.*
import com.ricardosfp.zapping.domain.model.*
import com.ricardosfp.zapping.infrastructure.util.date.*
import java.text.*
import java.util.*
import javax.inject.*

// todo test this class
@Singleton
class ArticleParserImpl @Inject constructor(
    private val dateUtils: DateUtils,
): ArticleParser {

    companion object {
        private val dateFormat = SimpleDateFormat("E, d MMM yyyy HH:mm:ss", Locale.ENGLISH)
    }

    override fun parse(article: Article): Match? {
        try {
            val pubDate = article.pubDate
            val originalText = article.title

            if (pubDate != null && originalText != null) {
                val date = dateUtils.parse(dateFormat, pubDate)

                if (date != null) {
                    val parts = originalText.split(" - ")
                    if (parts.size == 3) {
                        val teams = parts[0].split(" x ")
                        if (teams.size == 2) {
                            val homeTeam = teams[0]
                            val awayTeam = teams[1]
                            val channel = parts[2]

                            return Match(homeTeam, awayTeam, date, channel, originalText)

                        } else {
                            // not two teams
                            // todo report this error
                        }
                    } else {
                        // not the right number of string slices to extract
                        // information from
                        // todo report this error
                    }
                } else {
                    // date null
                    // todo report this error
                }
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            // todo report this error
        }

        return null
    }

}