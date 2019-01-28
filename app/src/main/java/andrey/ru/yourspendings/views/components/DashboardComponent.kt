package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.LinearLayout

/**
 * Created by Andrey Germanov on 1/17/19.
 */
@SuppressLint("ViewConstructor")
class DashboardComponent(context: MainActivity): Component(context) {

    lateinit var purchasesButton: Button
    lateinit var placesButton: Button
    lateinit var newPurchaseButton: Button
    lateinit var logoutButton: Button

    override fun render() {
        addView(LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT).apply {
                setMargins(10,10,10,10)
            }
            orientation = LinearLayout.VERTICAL
            addView(button(context.getString(R.string.new_purchase)).apply { newPurchaseButton = this })
            addView(button(context.getString(R.string.purchases)).apply { purchasesButton = this })
            addView(button(context.getString(R.string.places)).apply { placesButton = this })
            addView(LinearLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT,0,10.0f)
            })
            addView(button(context.getString(R.string.sign_out)).apply { logoutButton = this })
        })
    }

}