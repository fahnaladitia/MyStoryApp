package com.pahnal.mystoryapp.presentation.story_features.add_story

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pahnal.mystoryapp.R
import com.pahnal.mystoryapp.databinding.ActivityAddStoryBinding
import com.pahnal.mystoryapp.databinding.MainToolbarBinding
import com.pahnal.mystoryapp.domain.model.AddStory
import com.pahnal.mystoryapp.presentation.auth_features.login.LoginActivity
import com.pahnal.mystoryapp.presentation.story_features.home.HomeActivity
import com.pahnal.mystoryapp.utils.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var toolbar: MainToolbarBinding? = null
    private var getFile: File? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var currentPhotoPath: String
    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory)[AddStoryViewModel::class.java]
    }
    private val userLogin = DataPref.getUser(this).asLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        toolbar = binding.toolbar
        setContentView(binding.root)
        setupUI()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    private fun setupUI() {
        initObservers()
        setupToolbar()
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUIRED_CODE_PERMISSIONS
            )
        }
        with(binding) {
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            btnUpload.setOnClickListener { uploadStory() }
        }

    }

    private fun uploadStory() {
        userLogin.observe(this@AddStoryActivity) { user ->
            if (user != null) {
                if (getFile != null) {
                    val file = reduceFileImage(getFile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo", file.name, requestImageFile
                    )
                    with(binding) {
                        if (edDescription.text?.trim().toString().isEmpty()) {
                            edDescription.error =
                                getString(R.string.is_required, getString(R.string.description))
                            return@observe
                        }
                        if (ActivityCompat.checkSelfPermission(
                                this@AddStoryActivity, Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                this@AddStoryActivity, Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            viewModel.addStory(
                                imageMultipart,
                                AddStory(edDescription.text.toString()),
                                token = user.token,
                            )
                        }
                        mFusedLocationClient.lastLocation.addOnSuccessListener {
                            viewModel.addStory(
                                imageMultipart,
                                AddStory(
                                    edDescription.text.toString(),
                                    it.latitude,
                                    it.longitude,
                                ),
                                token = user.token,
                            )
                        }
                    }
                } else {
                    toast(getString(R.string.error_message_input_image))
                }
            } else {
                toast(getString(R.string.please_try_login))
                goTo(LoginActivity::class.java, true)
            }
        }

    }

    private fun allPermissionGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun initObservers() {
        with(binding) {
            viewModel.isLoading.observe(this@AddStoryActivity) { isLoading ->
                btnUpload.isEnabled = !isLoading
                btnCamera.isEnabled = !isLoading
                btnGallery.isEnabled = !isLoading
                btnUpload.text =
                    if (isLoading) getString(R.string.loading) else getString(R.string.upload)
            }
            viewModel.errorText.observe(this@AddStoryActivity) { text ->
                if (text.isNotEmpty()) {
                    toast(text)
                }
            }
            viewModel.isSuccess.observe(this@AddStoryActivity) { isSuccess ->
                if (isSuccess) {
                    goBackWithSingleData(HomeActivity.EXTRA_IS_RELOAD, isSuccess)
                    toast(getString(R.string.success_create_story))
                }
            }
        }

    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(this, getString(R.string.app_dir), it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            try {
                launcherIntentCamera.launch(intent)
            } catch (e: SecurityException) {
                toast(getString(R.string.access_camera_denied))
            }
        }
    }

    private fun setupToolbar() {
        toolbar?.root?.title = getString(R.string.add_story)
        setSupportActionBar(toolbar?.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> goBack()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val selectedImg = activityResult.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar = null
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        private const val REQUIRED_CODE_PERMISSIONS = 10
    }

}