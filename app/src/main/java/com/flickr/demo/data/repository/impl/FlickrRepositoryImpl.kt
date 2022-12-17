package com.flickr.demo.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.flickr.demo.data.api.FlickrAPI
import com.flickr.demo.data.datasource.PhotoPagingSource
import com.flickr.demo.data.repository.FlickrRepository
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

class FlickrRepositoryImpl(
    private val flickrAPI: FlickrAPI,
    private val scope: CoroutineContext
 ): FlickrRepository {

    override fun searchPhoto(apiKey: String, tag: String) = Pager(
            config = PagingConfig(
                pageSize = 25
            ),
            pagingSourceFactory = {
                PhotoPagingSource(flickrAPI, apiKey, tag)
            }
        ).flow.flowOn(scope)
}