package com.flickr.demo.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.flickr.demo.R
import com.flickr.demo.databinding.ActivityMainBinding
import com.flickr.demo.ui.photos.PhotoListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        setSupportActionBar(binding.toolbar)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, PhotoListFragment.newInstance())
            .addToBackStack(PhotoListFragment::class.java.simpleName)
            .commit()
    }

    fun showActionBar(
        title: String? = null,
        showBack: Boolean = false,
        showSearch: Boolean = false,
        searchListener: ((String) -> Unit)? = null,
    ) {
        binding.backButton.isVisible = showBack
        binding.backButton.setOnClickListener {
            supportFragmentManager.popBackStack()
        }

        binding.searchImage.isVisible = showSearch
        binding.searchInput.isVisible = false

        title?.let {
            binding.titleText.isVisible = true
            binding.titleText.text = title
        }

        binding.searchImage.setOnClickListener {
            binding.searchInput.setText("")
            binding.searchInput.isVisible = true
            binding.searchInput.requestFocus()
            inputMethodManager.showSoftInput(binding.searchInput, 0)
            binding.titleText.isVisible = false
            binding.searchImage.isVisible = false
        }

        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.searchInput.text.toString().isEmpty()) {
                    Toast.makeText(this, "Enter tag to search.", Toast.LENGTH_SHORT).show()
                } else {
                    inputMethodManager.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
                    searchListener?.invoke(binding.searchInput.text.toString())
                }
            }
            false
        }
    }
}