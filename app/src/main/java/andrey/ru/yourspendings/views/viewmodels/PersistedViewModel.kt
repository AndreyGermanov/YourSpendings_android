package andrey.ru.yourspendings.views.viewmodels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import java.nio.charset.Charset
import java.nio.file.*

/**
 * Created by Andrey Germanov on 1/15/19.
 */
@Suppress("UNCHECKED_CAST")
open class PersistedViewModel {

    private val gson = Gson()
    private var rootPath:String = ""

    val stateDir:String
        get() = rootPath+"/state/"+getId()

    val statePath: Path
        get() = Paths.get(stateDir)

    open fun initialize(rootPath: String) {
        this.rootPath = rootPath
        Files.createDirectories(statePath.parent)
        load()
    }

    open fun getId() = this::class.java.toString().split(" ").last().split(".").last()

    open fun getState():HashMap<String,Any> {
        return HashMap()
    }

    open fun setState(state:HashMap<String,Any>) {}

    fun save() {
        synchronized(this) {
            Files.write(statePath, listOf(gson.toJson(getState())) as List<CharSequence>,
                Charset.forName("UTF8"),StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.CREATE)
        }
    }

    fun load() {
        if (!Files.exists(statePath)) return
        val lines = Files.readAllLines(statePath)
        if (lines.size==0) return
        setState(gson.fromJson(lines.first(),HashMap::class.java) as HashMap<String,Any>)
    }
}