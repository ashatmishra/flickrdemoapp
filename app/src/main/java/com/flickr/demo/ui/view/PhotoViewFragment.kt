package com.flickr.demo.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.flickr.demo.R
import com.flickr.demo.databinding.FragmentPhotoViewBinding
import com.flickr.demo.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PHOTO_URL = "photo_url"

@AndroidEntryPoint
class PhotoViewFragment : Fragment() {

    private lateinit var photoUrl: String

    private var _binding: FragmentPhotoViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoUrl = it.getString(ARG_PHOTO_URL).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoViewBinding.inflate(inflater, container, false)
        Glide.with(requireContext())
            .load(photoUrl) // image url
            .centerCrop()
            .into(binding.photoView)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).showActionBar(title = "Photo", showBack = true)
    }

    companion object {
        @JvmStatic
        fun newInstance(photoUrl: String) =
            PhotoViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHOTO_URL, photoUrl)
                }
            }
    }
}