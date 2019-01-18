package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.views.MainActivity
import android.content.Context
import android.widget.FrameLayout

/**
 * Created by Andrey Germanov on 1/17/19.
 */
open class Component(context: MainActivity): FrameLayout(context) {

    open fun render() {}

    open fun bindUI() {}
}