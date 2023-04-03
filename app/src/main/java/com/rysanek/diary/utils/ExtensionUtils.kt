package com.rysanek.diary.utils

import io.realm.kotlin.types.RealmInstant
import java.time.Instant

fun RealmInstant.toInstant(): Instant {
    
    val secs: Long = epochSeconds
    val nano: Int = nanosecondsOfSecond
    
    return if (secs > 0) {
        Instant.ofEpochSecond(secs, nano.toLong())
    } else {
        Instant.ofEpochSecond(secs - 1, 1_000_000 + nano.toLong())
    
    }
    
}