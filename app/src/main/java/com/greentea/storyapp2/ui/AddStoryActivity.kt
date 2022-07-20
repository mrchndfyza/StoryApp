package com.greentea.storyapp2.ui

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.greentea.storyapp2.utils.Constants
import com.greentea.storyapp2.viewmodel.preferences.UserPreference
import com.greentea.storyapp2.databinding.ActivityAddStoryBinding
import com.greentea.storyapp2.services.models.db.StoryDB
import com.greentea.storyapp2.services.models.repository.StoryRepository
import com.greentea.storyapp2.utils.createCustomTempFile
import com.greentea.storyapp2.utils.uriToFile
import com.greentea.storyapp2.viewmodel.StoryViewModel
import com.greentea.storyapp2.viewmodel.factory.StoryViewModelProviderFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {
    //BINDING
    private lateinit var addStoryBinding: ActivityAddStoryBinding

    private lateinit var userPreference: UserPreference
    private lateinit var storyViewModel: StoryViewModel

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(addStoryBinding.root)

        userPreference = UserPreference(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        //CREATE CONNECTION ->
        val storyViewModelFactory = StoryViewModelProviderFactory(StoryRepository())
        storyViewModel = ViewModelProvider(
            this, storyViewModelFactory
        )[StoryViewModel::class.java]

        addStoryBinding.cameraButton.setOnClickListener { startTakePhoto() }
        addStoryBinding.galleryButton.setOnClickListener { startGallery() }
        addStoryBinding.uploadButton.setOnClickListener { uploadImage() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.greentea.storyapp2",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val descriptionFromLayout = addStoryBinding.cvEdtDesc.text.toString()
            val description = descriptionFromLayout.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val tokenFromPreferences = userPreference.getDataLogin(Constants.TOKEN)
            val realToken = "Bearer $tokenFromPreferences"

            storyViewModel.getAddStoryUserResponse(realToken, imageMultipart, description)
            observeAddStoryUsers()

        } else {
            Toast.makeText(this@AddStoryActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeAddStoryUsers(){
        storyViewModel.addStoryUsers.observe(this) { response ->
            if(response.isSuccessful){
                if(response.body()?.error == false){
                    Toast.makeText(this, "Berhasil upload gambar", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddStoryActivity, HomeActivity::class.java))
                    killActivity()
                }
                else{
                    Toast.makeText(this, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            } else{
                Toast.makeText(this, "Gagal instance Retrofit", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun killActivity(){
        finish()
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result =  BitmapFactory.decodeFile(getFile?.path)
            addStoryBinding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            addStoryBinding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}