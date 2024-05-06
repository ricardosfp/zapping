package com.ricardosfp.zapping.data.repository.implementation

import android.content.*
import com.prof.rssparser.*
import com.ricardosfp.zapping.BuildConfig
import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import dagger.hilt.android.qualifiers.*
import javax.inject.*

@Singleton
class ZappingRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
): ZappingRepository {

    // since this field is already part of a singleton and is only used in here, there is no
    // strong need to inject it
    private val parser: Parser = Parser.Builder() //        .charset(StandardCharsets.ISO_8859_1)
        .context(context).cacheExpirationMillis((1000 * 60 * 60 * 24).toLong()).build()

    override suspend fun getMatches(): GetMatchesRepositoryResponse {

        return try {
            // the library automatically changes the context of the coroutine
            val channel = parser.getChannel(BuildConfig.ZAPPING_URL)

            GetMatchesRepositoryResponseSuccess(channel)
        }
        catch (th: Throwable) {
            GetMatchesRepositoryResponseError(th)
        }

    }
}