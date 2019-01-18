package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.Component
import andrey.ru.yourspendings.views.store.AppState

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
open class Container(open var context: MainActivity) {
    open lateinit var component: Component

    var children = ArrayList<Container>()

    open fun onStateChanged(state: AppState, prevState: AppState) {
        children.forEach { it.onStateChanged(state,prevState)}
    }

    companion object {
        private var mInstances = HashMap<String,Container>()

        fun <T:Container>getInstance(ctx:MainActivity,item:T):T {
            val className = item::class.java.toString()
            if (!mInstances.containsKey(className)) mInstances[className] = item
            return (mInstances[className]!! as T).apply { context = ctx}
        }
    }

    open fun getView():Component {
        return component
    }

}