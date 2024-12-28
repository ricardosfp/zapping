package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.data.repository.model.MyArticle
import com.ricardosfp.zapping.domain.model.Match
import com.ricardosfp.zapping.domain.model.MatchParseDateError
import com.ricardosfp.zapping.domain.model.MatchParseOtherExceptionError
import com.ricardosfp.zapping.domain.model.MatchParseResult
import com.ricardosfp.zapping.domain.model.MatchParseSuccess
import com.ricardosfp.zapping.domain.model.MatchParseTitleError
import com.ricardosfp.zapping.infrastructure.util.date.DateUtils
import java.time.format.DateTimeParseException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchParserImpl @Inject constructor(
    private val dateUtils: DateUtils,
): MatchParser {

    companion object {
        private const val DATE_FORMAT = "E, d MMM yyyy HH:mm:ss"
    }

    override fun parse(article: MyArticle): MatchParseResult {
        return try {
            val date = dateUtils.parse(dateString = article.date, pattern = DATE_FORMAT, Locale.ENGLISH)
            val originalText = article.title

            val parts = originalText.split(" - ")
            if (parts.size == 3) {
                val teams = parts[0].split(" x ")
                if (teams.size == 2) {
                    val homeTeam = teams[0]
                    val awayTeam = teams[1]
                    val channel = parts[2]

                    if (homeTeam.isEmpty() || awayTeam.isEmpty() || channel.isEmpty()) {
                        MatchParseTitleError
                    } else {
                        MatchParseSuccess(
                            Match(
                                homeTeam,
                                awayTeam,
                                date,
                                channel,
                                originalText))
                    }
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
        }
        catch (ex: DateTimeParseException) {
            ex.printStackTrace()
            // todo report this error
            return MatchParseDateError(ex)
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            // todo report this error
            return MatchParseOtherExceptionError(ex)
        }
    }

}