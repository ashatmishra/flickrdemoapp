package com.flickr.demo.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flickr.demo.data.api.FlickrAPI
import com.flickr.demo.data.dtos.Photo
import com.flickr.demo.domain.model.FlickrPhoto
import retrofit2.HttpException
import java.io.IOException

class PhotoPagingSource(
    private val flickrApi: FlickrAPI,
    private val apiKey: String,
    private val tag: String
) : PagingSource<Int, FlickrPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FlickrPhoto> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = flickrApi.searchPhoto(
                apiKey = apiKey,
                tags = tag,
                page = page
            )
            LoadResult.Page(
                data = response.photos.photo.map { photo ->
                    FlickrPhoto(
                        id = photo.id,
                        title = photo.title,
                        imageUrl = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_q.jpg",
                        largeImageUrl = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_b.jpg"
                    )
                },
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (response.photos.photo.isEmpty()) null else page.plus(1)
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, FlickrPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

}