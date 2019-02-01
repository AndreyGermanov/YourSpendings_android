package andrey.ru.yourspendings.extensions

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by Andrey Germanov on 1/10/19.
 */
fun dateFromAny(value:Any?):LocalDateTime {
    return when (value) {
        is LocalDateTime -> value
        is Long -> LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC)
        is Timestamp -> LocalDateTime.ofEpochSecond(value.seconds, 0, ZoneOffset.UTC)
        is String -> {
            if (value.toLongOrNull() != null)
                LocalDateTime.ofEpochSecond(value.toLong(), 0, ZoneOffset.UTC)
            else
                LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ?: LocalDateTime.now()
        }
        is Date -> {
            LocalDateTime.ofEpochSecond(value.time / 1000, 0, ZoneOffset.UTC)
        }
        else -> LocalDateTime.now()
    }
}
