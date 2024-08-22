package com.diontambz.storyapp.view.add_story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.diontambz.storyapp.R
import com.diontambz.storyapp.data.Result
import com.diontambz.storyapp.databinding.ActivityAddStoryBinding
import com.diontambz.storyapp.util.ViewModelFactory
import com.diontambz.storyapp.util.createCustomTempFile
import com.diontambz.storyapp.util.reduceFileImage
import com.diontambz.storyapp.util.uriToFile
import com.diontambz.storyapp.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getLastLocation()
            } else {
                currentLocation = null
            }
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        setupViewModel()

        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        addStoryViewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]
    }

    private fun uploadImage() {
        addStoryViewModel.getUser().observe(this@AddStoryActivity) { user ->

            val token = "Bearer " + user.token
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)

                val description = "${binding.etDesc.text}".toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo", file.name, requestImageFile
                )
                var lat: RequestBody? = null
                var lon: RequestBody? = null
                if (binding.switchLocation.isChecked) {
                    lat = currentLocation?.latitude.toString()
                        .toRequestBody("text/plain".toMediaType())
                    lon = currentLocation?.longitude.toString()
                        .toRequestBody("text/plain".toMediaType())
                }

                addStoryViewModel.addStory(token, imageMultipart, description, lat, lon)
                    .observe(this@AddStoryActivity) { result ->
                        when (result) {
                            is Result.Success -> {
                                Toast.makeText(
                                    this@AddStoryActivity, result.data.message, Toast.LENGTH_SHORT
                                ).show()
                                showLoad(false)
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()

                            }

                            is Result.Loading -> showLoad(true)
                            is Result.Error -> {
                                Toast.makeText(
                                    this@AddStoryActivity,
                                    result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                                showLoad(false)
                            }
                        }
                    }
            } else {
                Toast.makeText(
                    this@AddStoryActivity, getString(R.string.input_image_first), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity, "com.diontambz.storyapp", it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile

            binding.previewImage.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewImage.setImageBitmap(result)
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_PERMISSIONS
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            currentLocation = location
        }
    }

    private fun showLoad(isLoad: Boolean) {
        binding.progressBar.visibility = if (isLoad) View.VISIBLE else View.GONE
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10

    }
}