package com.flickr.demo.ui.photos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.flickr.demo.R
import com.flickr.demo.databinding.FragmentPhotoListBinding
import com.flickr.demo.ui.MainActivity
import com.flickr.demo.ui.view.PhotoViewFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FlickrPhotosViewModel>()
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoListBinding.inflate(inflater, container, false)
        val view = binding.root
        inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        photoAdapter = PhotoAdapter(requireContext()) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, PhotoViewFragment.newInstance(it))
                .addToBackStack(PhotoViewFragment::class.java.simpleName)
                .commit()
        }
        binding.photoList.apply {
            setHasFixedSize(false)
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        setupObservers()

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).finish()
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).showActionBar(title = "PhotoList", showSearch = true) {
            viewModel.searchPhotos(it)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.photoStateFlow.collectLatest {
                if (it != null) {
                    photoAdapter.submitData(lifecycle, it)
                }
            }
        }

        lifecycleScope.launch {
            photoAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressIndicator.isVisible = loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
                binding.emptyView.isVisible = loadStates.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached

                val errorState = when {
                    loadStates.append is LoadState.Error -> loadStates.append as LoadState.Error
                    loadStates.prepend is LoadState.Error ->  loadStates.prepend as LoadState.Error
                    loadStates.refresh is LoadState.Error -> loadStates.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireContext(), "Unexpected error occured please check your network connection.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhotoListFragment()
    }
}