package com.flickr.demo.data.api

import com.flickr.demo.data.dtos.FlickrPhotoSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrAPI {
    //Base URL https://www.flickr.com/services/
    @GET("rest/?method=flickr.photos.search&format=json&nojsoncallback=1&per_page=25")
    suspend fun searchPhoto(
        @Query("api_key") apiKey: String ,
        @Query("tags") tags: String,
        @Query("page") page: Int
    ): FlickrPhotoSearchResponse

}