package andrey.ru.yourspendings.views.fragments.purchases

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.extensions.DateFromAny
import andrey.ru.yourspendings.models.PlacesCollection
import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.SelectModelActivity
import andrey.ru.yourspendings.views.fragments.ModelItemFragment
import andrey.ru.yourspendings.views.fragments.ui.DateTimePickerFragment
import andrey.ru.yourspendings.views.viewmodels.ActivityEvent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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
    private lateinit var placeLabel: TextView
    private lateinit var place_id: String
    private lateinit var dateSelectBtn: ImageButton
    private lateinit var placeSelectBtn: ImageButton

    override fun bindUI(view: View) {
        date = view.findViewById(R.id.purchase_date)
        placeLabel = view.findViewById(R.id.purchase_shop)
        dateSelectBtn = view.findViewById(R.id.select_date_btn)
        placeSelectBtn = view.findViewById(R.id.select_place_btn)
        super.bindUI(view)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)
        date.setOnKeyListener(this)

        dateSelectBtn.setOnClickListener {
            val dateTimePicker = DateTimePickerFragment()
            val arguments = Bundle()
            arguments.putSerializable("initialDateTime",DateFromAny(date.text))
            arguments.putString("subscriberId",fragmentId.toString()+"-"+currentItemId)
            dateTimePicker.arguments = arguments
            dateTimePicker.show(activity!!.supportFragmentManager,"Select date")
        }

        placeSelectBtn.setOnClickListener {
            val intent = Intent(activity,SelectModelActivity::class.java).apply {
                putExtra("model","Place")
                putExtra("currentItemId", place_id)
                putExtra("subscriberId",fragmentId.toString()+"-"+currentItemId)
            }
            activity!!.startActivityForResult(intent,(activity as MainActivity).RESULT_ACTIVITY_SELECTED)
        }

    }

    override fun getFields(): HashMap<String, Any> {
        return hashMapOf(
            "date" to date.text.toString().trim(),
            "place_id" to place_id,
            "id" to currentItemId.trim()
        )
    }

    override fun setFields(fields:Map<String,Any>?) {
        val fields = fields ?: viewModel.getFields()
        date.text = DateFromAny(fields["date"]).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        placeLabel.text = PlacesCollection.getItemById(fields["place_id"]?.toString() ?: "")?.name ?: ""
        place_id = fields["place_id"]?.toString() ?: ""
    }

    override fun onActivityEvent(event: ActivityEvent) {
        if (event.subscriberId != fragmentId.toString()+"-"+currentItemId) return
        when (event.eventName) {
            "dialogSubmit" -> onDateTimeChange(event.eventData as LocalDateTime)
            "itemSelected" -> onPlaceChange(event.eventData.toString())
        }
    }

    private fun onDateTimeChange(datetime: LocalDateTime) {
        date.text = datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        viewModel.setFields(getFields())
    }

    private fun onPlaceChange(placeId: String) {
        place_id = placeId
        viewModel.setFields(getFields())
        setFields()
    }

}
