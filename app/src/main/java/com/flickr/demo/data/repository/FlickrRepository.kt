package com.flickr.demo.data.repository

import androidx.paging.PagingData
import com.flickr.demo.domain.model.FlickrPhoto
import kotlinx.coroutines.flow.Flow

interface FlickrRepository {
    fun searchPhoto(apiKey: String, tag: String): Flow<PagingData<FlickrPhoto>>
}