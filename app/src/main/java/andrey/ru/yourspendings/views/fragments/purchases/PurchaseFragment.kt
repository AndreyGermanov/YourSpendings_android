package andrey.ru.yourspendings.views.fragments.purchases

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.views.fragments.ModelItemFragment
import android.view.View
import android.widget.EditText

@Suppress("NAME_SHADOWING")

/**
 * Created by Andrey Germanov on 1/9/19.
 */
class PurchaseFragment: ModelItemFragment<Purchase>() {

    override var fragmentId:Int = R.layout.fragment_purchase
    override var className:String = Purchase.getClassName()

    private lateinit var date: EditText
    private lateinit var place: EditText

    override fun bindUI(view: View) {
        date = view.findViewById(R.id.purchase_date)
        place = view.findViewById(R.id.purchase_shop)
        super.bindUI(view)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)
        date.setOnKeyListener(this)
        place.setOnKeyListener(this)
    }

    override fun getFields(): HashMap<String, String> {
        return hashMapOf(
            "date" to date.text.toString().trim(),
            "place_id" to place.text.toString().trim(),
            "id" to currentItemId.trim()
        )
    }

    override fun setFields(fields:Map<String,Any>?) {
        val fields = fields ?: viewModel.getFields()
        date.setText(fields["date"]?.toString() ?: "")
        place.setText(fields["place_id"]?.toString() ?: "")
    }
}
