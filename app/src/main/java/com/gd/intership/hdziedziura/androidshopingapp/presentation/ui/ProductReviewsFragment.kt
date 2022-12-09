package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gd.intership.hdziedziura.androidshopingapp.BuildConfig
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentProductReviewsBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.ProductReviewsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.io.File

class ProductReviewsFragment : BaseFragment() {
    private val viewModel: ProductReviewsViewModel by viewModels()
    private lateinit var binding: FragmentProductReviewsBinding
    override val showToolbar: Boolean
        get() = true
    override val showBottomNavBar: Boolean
        get() = false
    override val toolbarTitle: String
        get() = resources.getString(R.string.rating_reviews)

    private lateinit var permissionCamera: ActivityResultLauncher<String>
    private lateinit var permissionStorage: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        var latestTempUri: Uri? = null

        val pickFromGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    viewModel.addedPhotosUriList.add(uri)
                    viewModel.createPhotos()
                }
            }

        val capturePhoto =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    latestTempUri?.let { uri ->
                        viewModel.addedPhotosUriList.add(uri)
                        viewModel.createPhotos()
                    }
                }
            }

        permissionCamera =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        getTempFileUri().let { uri ->
                            latestTempUri = uri
                            capturePhoto.launch(uri)
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.permission_denied, Toast.LENGTH_SHORT
                    ).show()
                }
            }

        permissionStorage =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        pickFromGallery.launch("image/*")
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.permission_denied, Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_reviews, container, false
        )

        viewModel.category = arguments?.getString("itemCategory").toString()
        viewModel.title = arguments?.getString("itemTitle").toString()

        lifecycle.addObserver(viewModel)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvents.collect {
                        when (it) {
                            is ProductReviewsViewModel.Event.FetchingErrorEvent -> Toast.makeText(
                                context, getString(R.string.error_fetching), Toast.LENGTH_SHORT
                            ).show()
                            is ProductReviewsViewModel.Event.WriteReviewEvent ->
                                ReviewBottomSheetFragment.show(
                                    childFragmentManager,
                                    ReviewBottomSheetFragment::class.simpleName,
                                )
                            is ProductReviewsViewModel.Event.ReviewClickEvent -> Toast.makeText(
                                context, "Review ${it.item.name} click", Toast.LENGTH_SHORT
                            ).show()
                            is ProductReviewsViewModel.Event.RemovePhotoEvent -> {
                                viewModel.addedPhotosUriList.remove(it.item.item)
                                viewModel.createPhotos()
                            }
                            is ProductReviewsViewModel.Event.AddPhotoEvent -> {
                                val items = arrayOf(
                                    resources.getString(R.string.take_photo),
                                    resources.getString(R.string.choose_gallery),
                                )

                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle(resources.getString(R.string.add_photos))
                                    .setItems(items) { _, which ->
                                        when (which) {
                                            0 -> permissionCamera.launch(
                                                android.Manifest.permission.CAMERA
                                            )
                                            1 -> permissionStorage.launch(
                                                if (android.os.Build.VERSION.SDK_INT >=
                                                    android.os.Build.VERSION_CODES.TIRAMISU
                                                ) {
                                                    android.Manifest.permission.READ_MEDIA_IMAGES
                                                } else {
                                                    READ_EXTERNAL_STORAGE
                                                }
                                            )
                                        }
                                    }
                                    .show()
                            }
                            else -> {}
                        }
                    }
                }
            }
        }

        binding.vm = viewModel

        return binding.root
    }

    private fun getTempFileUri(): Uri {
        val tempFile =
            File.createTempFile("temp_image_file", ".png", requireActivity().cacheDir).apply {
                createNewFile()
            }
        return FileProvider.getUriForFile(
            requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tempFile
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}
