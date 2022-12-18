package com.flickr.demo.data.datasource

import androidx.paging.PagingSource
import com.flickr.demo.data.api.FlickrAPI
import com.flickr.demo.data.dtos.FlickrPhotoSearchResponse
import com.flickr.demo.data.dtos.Photo
import com.flickr.demo.data.dtos.Photos
import com.flickr.demo.domain.model.FlickrPhoto
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given

@ExperimentalCoroutinesApi
class PhotoPagingSourceTest {

    private val response = FlickrPhotoSearchResponse(
        photos = Photos(
            page = 1,
            pages = 20,
            perpage = 25,
            photo = listOf(
                Photo(
                    farm = 1,
                    id = "id",
                    isfamily = 1,
                    isfriend = 1,
                    ispublic = 1,
                    owner = "owner",
                    secret = "secret",
                    server = "server",
                    title = "title"
                )
            ),
            total = 100
        ),
        stat = "stat"
    )

    private lateinit var source: PhotoPagingSource

    private val service: FlickrAPI = mock()

    @Before
    fun `set up`() {
        source = PhotoPagingSource(service, "apiKey", "tag")
    }

    @Test
    fun `paging source - success`() = runTest {
        given(service.searchPhoto(any(), any(), any())).willReturn(response)
        val expectedResult = PagingSource.LoadResult.Page(
            data = response.photos.photo.map { photo ->
                FlickrPhoto(
                    id = photo.id,
                    title = photo.title,
                    imageUrl = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_q.jpg",
                    largeImageUrl = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_b.jpg"

                )
            },
            prevKey = -1,
            nextKey = 1
        )
        assertEquals(
            expectedResult, source.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }
}