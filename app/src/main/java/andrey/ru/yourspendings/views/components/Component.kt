package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*

/**
 * Created by Andrey Germanov on 1/17/19.
 */
@SuppressLint("ViewConstructor")
open class Component(context: MainActivity): FrameLayout(context) {

    val tableLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT)
    private val rowLayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT)

    open fun render() {}

    open fun bindUI() {}

    fun button(title:String): Button {
        return Button(context).apply {
            layoutParams = horizontal()
            text = title
        }
    }

    fun textView(title:String): TextView {
        return TextView(context).apply {
            layoutParams = horizontal()
            text = title
        }
    }

    private fun editText(hint:String): EditText {
        return EditText(context).apply {
            layoutParams = horizontal()
            setHint(hint)
            setSingleLine()
        }
    }

    fun imageButton(resourceId:Int,title:String): ImageButton {
        return ImageButton(context).apply {
            layoutParams = wrap().apply { gravity = Gravity.CENTER }
            setImageResource(resourceId)
            contentDescription = title
            setPadding(5,5,5,5)
        }
    }

    fun horizontal():LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun fullScreen():LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)
    }

    fun wrap():LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)

    fun renderTextRow(title:String,callback:(f:EditText)->Unit) = TableRow(context).apply {
        layoutParams = rowLayoutParams
        addView(textView(title).apply { layoutParams = rowLayoutParams })
        addView(editText(title).apply { layoutParams = rowLayoutParams; callback(this) })
    }

    fun renderPasswordRow(title:String,callback:(f:EditText)->Unit) = TableRow(context).apply {
        layoutParams = rowLayoutParams
        addView(textView(title).apply { layoutParams = rowLayoutParams })
        addView(editText(title).apply { layoutParams = rowLayoutParams
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            callback(this)
        })
    }

}