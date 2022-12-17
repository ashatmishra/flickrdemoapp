package com.flickr.demo.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flickr.demo.data.repository.FlickrRepository
import com.flickr.demo.domain.model.FlickrPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlickrPhotosViewModel @Inject constructor(
    private val repository: FlickrRepository
) : ViewModel() {

    private val _photoStateFlow =  MutableStateFlow<PagingData<FlickrPhoto>?>(null)
    val photoStateFlow: StateFlow<PagingData<FlickrPhoto>?> = _photoStateFlow

    fun searchPhotos(tag: String) {
        viewModelScope.launch {
            repository.searchPhoto(
                apiKey = "1508443e49213ff84d566777dc211f2a",
                tag = tag
            ).cachedIn(viewModelScope).collect {
                _photoStateFlow.value = it
            }
        }
    }
}