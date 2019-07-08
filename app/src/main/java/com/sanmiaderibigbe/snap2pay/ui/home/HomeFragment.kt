package com.sanmiaderibigbe.snap2pay.ui.home


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.repo.Status
import com.sanmiaderibigbe.snap2pay.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    private lateinit var imagePath: String
    private lateinit var fileName: String
    private val loginViewModel by viewModel<LoginViewModel>()
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        getAuthenticationState(navController)

        initCardScanner()


    }

    private fun initCardScanner() {
        ic_card_camera.setOnClickListener {

            initPermissions()
            initTakePictureIntent()
        }
    }

    private fun initPermissions() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {

            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_STORAGE
                )
            }

        } else {
            Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_STORAGE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {


                } else {

                    Toast.makeText(activity, getString(R.string.camera_permision_error), Toast.LENGTH_SHORT).show()
                }
                return
            }


            else -> {

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val atmCardImage = viewModel.getRotatedBitmap(imagePath)
            viewModel.cleanUpImage(fileName)

            if (atmCardImage != null) {
                ic_card_camera.setImageBitmap(atmCardImage)
                viewModel.getTextFromCard(atmCardImage)
                viewModel.getProcessedTextResource().observe(viewLifecycleOwner, Observer {
                    when (it.status) {
                        Status.SUCCESS -> {
                            Toast.makeText(activity, "${it.data}", Toast.LENGTH_SHORT).show()
                        }
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
                            if (it.data.isNullOrEmpty()) {
                                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        Status.LOADED -> {
                        }

                    }
                })
            }


        }

    }

    private fun getAuthenticationState(navController: NavController) {
        loginViewModel.getLoginResource().observe(viewLifecycleOwner, Observer {
            when (it.data) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    initEmailVerificationCheck()
                }
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToUnauthenticatedFragment())

            }
        })
    }

    private fun initEmailVerificationCheck() {
        if (!loginViewModel.isEmailVerified()!!) {
            ic_card_camera.visibility = View.GONE
            Toast.makeText(activity, getString(R.string.verification_error), Toast.LENGTH_SHORT).show()
        } else {
            ic_card_camera.visibility = View.VISIBLE
        }
    }

    private fun initTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity!!.packageManager)?.also {
                var file: File? = null

                try {
                    file = viewModel.createImageFile()
                    imagePath = file?.absolutePath!!
                    fileName = file?.nameWithoutExtension
                } catch (ex: IOException) {
                    Toast.makeText(activity, "${ex.message}", Toast.LENGTH_SHORT).show()
                }

                if (file != null) {
                    val fileUri = viewModel.getImageFileUri(file)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                }

                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_STORAGE: Int = 10
        const val REQUEST_IMAGE_CAPTURE = 11
    }
}
