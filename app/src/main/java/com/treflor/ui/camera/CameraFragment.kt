package com.treflor.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.treflor.R
import com.treflor.ui.camera.Keys.INPUT_SIZE
import kotlinx.android.synthetic.main.camera_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.FileNotFoundException

class CameraFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private lateinit var viewModel: CameraViewModel
    private val viewModelFactory: CameraViewModelFactory by instance()
    private lateinit var navController: NavController

    private lateinit var cameraFragmentBinding: CameraFragmentBinding

    private val CHOOSE_IMAGE = 1001
    private lateinit var photoImage: Bitmap
    private lateinit var classifier: ImageClassifier

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.camera_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CameraViewModel::class.java)
        cameraFragmentBinding =
            CameraFragmentBinding.bind(view)
        bindUI()

        setContentView(R.layout.camera_fragment)
        classifier = ImageClassifier(getAssets())
        imageResult.setOnClickListener {
            choosePicture()
        }

    }

    public fun BindUI() = launch {
        //TODO Implement
    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, CHOOSE_IMAGE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == CHOOSE_IMAGE && resultCode == activity.RESULT_OK)
            try {
                val stream = contentResolver!!.openInputStream(data.getData())
                if (::photoImage.isInitialized) photoImage.recycle()
                photoImage = BitmapFactory.decodeStream(stream)
                photoImage = Bitmap.createScaledBitmap(photoImage, INPUT_SIZE, INPUT_SIZE, false)
                imageResult.setImageBitmap(photoImage)
                classifier.recognizeImage(photoImage).subscribeBy(
                    onSuccess = {
                        txtResult.text = it.toString()
                    }
                )
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifier.close()
    }

}
