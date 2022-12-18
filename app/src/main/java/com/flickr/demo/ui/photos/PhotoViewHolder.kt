package com.flickr.demo.ui.photos

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flickr.demo.R
import com.flickr.demo.domain.model.FlickrPhoto

class PhotoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(
        parent.context
    ).inflate(
        R.layout.view_flickr_photo,
        parent,
        false
    )
) {
    private val contentImageView = itemView.findViewById<ImageView>(R.id.contentImageView)
    private val titleView = itemView.findViewById<TextView>(R.id.titleView)
    private val containerView = itemView.findViewById<LinearLayoutCompat>(R.id.containerView)

    fun bindTo(context: Context, photo: FlickrPhoto, onPhotoSelected: (String) -> Unit) {
        titleView.text = photo.title
        Glide.with(context)
            .load(photo.imageUrl) // image url
            .override(100, 100) // resizing
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .centerCrop()
            .into(contentImageView)

        containerView.setOnClickListener {
            onPhotoSelected.invoke(photo.largeImageUrl)
        }
    }

}
