package andrey.ru.yourspendings.views.fragments.purchases

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.extensions.dateFromAny
import andrey.ru.yourspendings.models.PlacesCollection
import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.services.LocationManager
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.SelectModelActivity
import andrey.ru.yourspendings.views.adapters.ModelImagesAdapter
import andrey.ru.yourspendings.views.fragments.ModelItemFragment
import andrey.ru.yourspendings.views.fragments.ui.DateTimePickerFragment
import andrey.ru.yourspendings.views.viewmodels.ActivityEvent
import andrey.ru.yourspendings.views.viewmodels.PurchasesViewModel
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Suppress("NAME_SHADOWING", "UNCHECKED_CAST", "PrivatePropertyName")

/**
 * Created by Andrey Germanov on 1/9/19.
 */
class PurchaseFragment: ModelItemFragment<Purchase>() {

    override var fragmentId:Int = R.layout.fragment_purchase
    override var className:String = Purchase.getClassName()

    private lateinit var date: TextView
    private lateinit var placeLabel: TextView
    private lateinit var place_id: String
    private lateinit var images:Map<String,String>

    private lateinit var dateSelectBtn: ImageButton
    private lateinit var placeSelectBtn: ImageButton
    private lateinit var placeDetectBtn: ImageButton
    private lateinit var takePictureBtn: Button

    private lateinit var listAdapter:ModelImagesAdapter<Purchase>

    override fun bindUI(view: View) {
        with (view) {
            date = findViewById(R.id.purchase_date)
            placeLabel = findViewById(R.id.purchase_shop)
            dateSelectBtn = findViewById(R.id.select_date_btn)
            placeSelectBtn = findViewById(R.id.select_place_btn)
            placeDetectBtn = findViewById(R.id.detect_place_btn)
            takePictureBtn = findViewById(R.id.take_picture_button)
        }
        viewModel.setContext(activity!!)
        setupImagesList(view)
        super.bindUI(view)
    }

    override fun prepareItemForm(view: View) {
        super.prepareItemForm(view)
        val viewModel = viewModel as PurchasesViewModel
        val item = viewModel.getItems().value?.find { it.id == currentItemId }
        if (viewModel.getFields()["id"] != item?.id || !viewModel.isLoaded) {
            viewModel.syncImageCache {
                listAdapter.notifyDataSetChanged()
                viewModel.isLoaded = true
            }
            if (currentItemId == "new") detectPlace()
        }
    }

    private fun setupImagesList(view: View) {
        listAdapter = ModelImagesAdapter(viewModel)
        view.findViewById<RecyclerView>(R.id.images_list_container).apply {
            layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
            adapter = listAdapter
        }
    }

    private fun detectPlace() {
        LocationManager.getLocation { lat,lng ->
            PlacesCollection.getClosestPlace(lat,lng) {
                place_id = it?.id ?: ""
                viewModel.setFields(getFields())
                setFields()
            }
        }
    }

    override fun setListeners(view: View) {
        super.setListeners(view)
        date.setOnKeyListener(this)

        dateSelectBtn.setOnClickListener {
            DateTimePickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("initialDateTime", dateFromAny(date.text))
                    putString("subscriberId", fragmentId.toString() + "-" + currentItemId)
                }
            }.show(activity!!.supportFragmentManager, "Select date")
        }

        placeSelectBtn.setOnClickListener {
            with (activity!! as MainActivity) {
                startActivityForResult(Intent(this,SelectModelActivity::class.java).apply {
                    putExtra("model","Place")
                    putExtra("currentItemId", place_id)
                    putExtra("subscriberId",fragmentId.toString()+"-"+currentItemId)
                },this.REQUEST_SELECT_ITEM)
            }
        }

        placeDetectBtn.setOnClickListener { detectPlace() }

        takePictureBtn.setOnClickListener {
            activity?.let { activity ->
                AlertDialog.Builder(activity).apply {
                    setTitle(R.string.imageSource)
                    setItems(R.array.imageSources) { _:DialogInterface, sourceId:Int ->
                        when (sourceId) {
                            ImageSource.CAMERA.code -> takePictureFromCamera()
                            ImageSource.PICTURE_LIBRARY.code -> takePictureFromLibrary()
                        }
                    }
                }.create().show()
            }
        }
    }

    private fun takePictureFromCamera() {
        val viewModel = viewModel as PurchasesViewModel
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val fileName = UUID.randomUUID().toString()
        val file = File.createTempFile(fileName,".jpg",storageDir)
        viewModel.setImagePath(file.absolutePath)
        val fileUri = FileProvider.getUriForFile(activity!!,"andrey.ru.yourspendings.fileprovider",file)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri)
        activity!!.startActivityForResult(intent,(activity as MainActivity).REQUEST_TAKE_PICTURE_FROM_CAMERA)
    }

    private fun takePictureFromLibrary() {
        with (activity!! as MainActivity) {
            startActivityForResult(Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            },REQUEST_TAKE_PICTURE_FROM_PICTURE_LIBRARY)
        }
    }

    override fun getFields(): HashMap<String, Any> {
        return hashMapOf(
            "date" to date.text.toString().trim(),
            "place_id" to place_id,
            "id" to currentItemId.trim(),
            "images" to images
        )
    }

    override fun setFields(fields:Map<String,Any>?) {
        val fields = fields ?: viewModel.getFields()
        date.text = dateFromAny(fields["date"]).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        placeLabel.text = PlacesCollection.getItemById(fields["place_id"]?.toString() ?: "")?.name ?: ""
        place_id = fields["place_id"]?.toString() ?: ""
        images = fields["images"] as? Map<String,String> ?: HashMap()
    }

    override fun onActivityEvent(event: ActivityEvent) {
        val viewModel = viewModel as PurchasesViewModel
        if (event.eventName == "purchaseImageCapturedFromCamera") onAddImageFromCamera(viewModel.getImagePath())
        if (event.eventName == "purchaseImageCapturedFromLibrary") onAddImageFromLibrary(event.eventData as Uri)
        if (event.subscriberId != fragmentId.toString()+"-"+currentItemId) return
        when (event.eventName) {
            "dialogSubmit" -> onDateTimeChange(event.eventData as LocalDateTime)
            "itemSelected" -> onPlaceChange(event.eventData.toString())
            "purchaseImageRemoved"-> {onRemoveImage(event.eventData.toString())}
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

    private fun onAddImageFromCamera(path:String) {
        val file = File(path)
        val destDir = activity!!.filesDir.absolutePath+"/images/"+currentItemId
        Files.createDirectories(Paths.get(destDir))
        (images as HashMap<String,String>)[file.nameWithoutExtension] = (file.lastModified()/1000).toString()
        Files.move(Paths.get(file.absolutePath),Paths.get(destDir+"/"+file.name))
        viewModel.setFields(getFields())
        setFields()
        listAdapter.notifyDataSetChanged()
    }

    private fun onAddImageFromLibrary(uri:Uri) {
        val imageId = UUID.randomUUID().toString()
        val filePath = activity!!.filesDir.absolutePath+"/images/"+currentItemId
        Files.createDirectories(Paths.get(filePath))
        (images as HashMap<String,String>)[imageId] = (File("$filePath/$imageId.jpg").apply {
            outputStream().write(activity!!.contentResolver.openInputStream(uri)!!.readBytes()
        )}.lastModified()/1000).toString()
        viewModel.setFields(getFields())
        setFields()
        listAdapter.notifyDataSetChanged()
    }

    private fun onRemoveImage(imagePath:String) {
        val imageId = File(imagePath).nameWithoutExtension
        (images as HashMap<String,String>).remove(imageId)
        viewModel.setFields(getFields())
        setFields()
        listAdapter.notifyDataSetChanged()
    }

}

enum class ImageSource(val code:Int) {
    CAMERA(0), PICTURE_LIBRARY(1)
}