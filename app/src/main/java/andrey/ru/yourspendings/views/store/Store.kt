package andrey.ru.yourspendings.views.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.GsonBuilder
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
class Store(application: Application): AndroidViewModel(application) {

    private val gson = GsonBuilder().setPrettyPrinting().create()!!
    private val stateDir = getApplication<Application>().filesDir.absolutePath+"/state/state.json"
    lateinit var state:AppState
    lateinit var rootView: IStoreSubscriber
    private val statePath: Path  = Paths.get(stateDir)
    var app = application

    init {
        Files.createDirectories(statePath.parent)
        load()
    }

    fun save(fields:MutableMap<String,Any>) {
        synchronized(this) {
            Files.write(
                statePath, listOf(gson.toJson(fields)) as List<CharSequence>,
                Charset.forName("UTF8"), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE
            )
        }
    }

    fun load() {
        if (!Files.exists(statePath)) {
            state = AppState(this,HashMap()).apply { initialize() }
            return
        }
        val text = Files.readAllLines(statePath).reduce {s,s1 -> s+s1}
        if (text.isEmpty()) return
        state = AppState(this,gson.fromJson(text,MutableMap::class.java) as MutableMap<String,Any>).apply {initialize()}
    }

}