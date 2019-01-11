package andrey.ru.yourspendings.views.fragments.purchases

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.extensions.DateFromAny
import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.views.fragments.ModelItemFragment
import andrey.ru.yourspendings.views.fragments.ui.DateTimePickerFragment
import andrey.ru.yourspendings.views.viewmodels.ActivityEvent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("NAME_SHADOWING")

/**
 * Created by Andrey Germanov on 1/9/19.
 */
class PurchaseFragment: ModelItemFragment<Purchase>() {

    override var fragmentId:Int = R.layout.fragment_purchase
    override var className:String = Purchase.getClassName()

    private lateinit var date: TextView
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

        date.setOnClickListener {
            val dateTimePicker = DateTimePickerFragment()
            val arguments = Bundle()
            arguments.putSerializable("initialDateTime",DateFromAny(date.text))
            arguments.putString("subscriberId",fragmentId.toString()+"-"+currentItemId)
            dateTimePicker.arguments = arguments
            dateTimePicker.show(activity!!.supportFragmentManager,"Select date")
        }
    }

    override fun getFields(): HashMap<String, Any> {
        return hashMapOf(
            "date" to date.text.toString().trim(),
            "place_id" to place.text.toString().trim(),
            "id" to currentItemId.trim()
        )
    }

    override fun setFields(fields:Map<String,Any>?) {
        val fields = fields ?: viewModel.getFields()
        date.text = DateFromAny(fields["date"]).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        place.setText(fields["place_id"]?.toString() ?: "")
    }

    override fun onActivityEvent(event: ActivityEvent) {
        if (event.subscriberId != fragmentId.toString()+"-"+currentItemId) return
        if (event.eventName == "dialogSubmit") onDateTimeChange(event.eventData as LocalDateTime)
    }

    private fun onDateTimeChange(datetime: LocalDateTime) {
        date.text = datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        viewModel.setFields(getFields())
    }

}
