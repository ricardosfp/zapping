package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.data.repository.model.*
import com.ricardosfp.zapping.domain.model.*
import com.ricardosfp.zapping.infrastructure.util.date.*
import java.text.*
import java.util.*
import javax.inject.*

// todo test this class
@Singleton
class MatchParserImpl @Inject constructor(
    private val dateUtils: DateUtils,
): MatchParser {

    companion object {
        private val dateFormat = SimpleDateFormat("E, d MMM yyyy HH:mm:ss", Locale.ENGLISH)
    }

    override fun parse(article: MyArticle): MatchParseResult {
        return try {
            val date = dateUtils.parse(dateFormat, article.date)
            val originalText = article.title

            if (date != null) {
                val parts = originalText.split(" - ")
                if (parts.size == 3) {
                    val teams = parts[0].split(" x ")
                    if (teams.size == 2) {
                        val homeTeam = teams[0]
                        val awayTeam = teams[1]
                        val channel = parts[2]

                        MatchParseSuccess(Match(homeTeam, awayTeam, date, channel, originalText))

                    } else {
                        // not two teams
                        // todo report this error
                        MatchParseTitleError
                    }
                } else {
                    // not the right number of string slices to extract
                    // information from
                    // todo report this error
                    MatchParseTitleError
                }
            } else {
                // date null. I don't think that this happens
                // todo report this error
                MatchParseUnknownError
            }
        }
        catch (ex: ParseException) {
            ex.printStackTrace()
            // todo report this error
            return MatchParseDateError(ex)
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            // todo report this error
            return MatchParseExceptionError(ex)
        }
    }

}