package andrey.ru.yourspendings.db

/**
 * Created by Andrey Germanov on 1/4/19.
 */
object DatabaseManager {
    fun getDB():IDatabaseAdapter = FirebaseAdapter()
}