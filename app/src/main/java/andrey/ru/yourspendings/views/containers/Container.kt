package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.Component
import andrey.ru.yourspendings.views.store.AppState
import andrey.ru.yourspendings.views.store.ModelState
import android.view.View
import android.view.ViewGroup

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
open class Container {
    open lateinit var context:MainActivity
    open lateinit var component: Component

    var children = ArrayList<Container>()

    open fun initialize(context:MainActivity) {
        this.context = context
    }

    open fun initialize(context:MainActivity,modelState:ModelState?=null) {
        this.initialize(context)
    }

    open fun onStateChanged(state: AppState, prevState: AppState) {
        children.forEach { it.onStateChanged(state,prevState)}
    }

    companion object {
        private var mInstances = HashMap<String,Container>()

        fun <T:Container>getInstance(ctx:MainActivity,item:Class<T>,id:String?=null):T {
            val itemId = id ?: item.name
            if (!mInstances.containsKey(itemId)) mInstances[itemId] = item.newInstance().apply { initialize(ctx) }
            return (mInstances[itemId]!! as T).apply { context = ctx}
        }

        fun <T:Container>getModelInstance(ctx:MainActivity,item:Class<T>,state:ModelState,id:String?=null):T {
            val itemId = id ?: item.name+"_"+state.modelName
            if (!mInstances.containsKey(itemId)) mInstances[itemId] = item.newInstance().apply { initialize(ctx,state) }
            return (mInstances[itemId]!! as T).apply { context = ctx}
        }
    }

    open fun getView():Component {
        return component
    }

    open fun addChild(child:Container):Container {
        if (!children.contains(child)) children.add(child)
        return child
    }

    open fun removeChild(child:Container) {
        if (children.contains(child)) children.remove(child)

    }

    open fun setView(to: ViewGroup) {
        if (component.parent != null) (component.parent as ViewGroup).removeView(component)
        to.addView(component)
    }

    open fun setView(): View {
        if (component.parent != null) (component.parent as ViewGroup).removeView(component)
        return component
    }

    open fun getId():String {
        return this::class.java.toString()
    }

}