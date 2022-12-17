package com.flickr.demo.ui.photos

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.flickr.demo.data.dtos.Photo
import com.flickr.demo.domain.model.FlickrPhoto
val USER_COMPARATOR = object : DiffUtil.ItemCallback<FlickrPhoto>() {
    override fun areItemsTheSame(oldItem: FlickrPhoto, newItem: FlickrPhoto): Boolean =
        // User ID serves as unique ID
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: FlickrPhoto, newItem: FlickrPhoto): Boolean =
        // Compare full contents (note: Java users should call .equals())
        oldItem == newItem
}

class PhotoAdapter(
    private val context: Context,
    private val onPhotoSelected: (String) -> Unit
) : PagingDataAdapter<FlickrPhoto, PhotoViewHolder>(USER_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bindTo(context, item, onPhotoSelected)
        }
    }
}