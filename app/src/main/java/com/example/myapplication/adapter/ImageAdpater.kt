package com.example.myapplication.adapter

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.APIResponseItem
import java.net.URL
import kotlin.concurrent.thread


class ImageAdpater(
    private var topicList: ArrayList<APIResponseItem>,
    private var context: Context
) : RecyclerView.Adapter<ImageAdpater.NewViewHolder>() {

    class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.child_image, parent, false)
        return NewViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val thumbnail = topicList[position].thumbnail
        val imageUrl = thumbnail.domain + "/" + thumbnail.basePath + "/0/" + thumbnail.key
       /* Glide.with(context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.place_holder) // Placeholder image
                    .error(R.drawable.place_holder) // Error image in case of loading failure
            )
            .into(holder.imageView)*/
        bindImage(imageUrl, holder.imageView)

    }

    override fun getItemCount(): Int {
        return topicList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun bindImage(imgUrl: String?, imageView: ImageView) {

        val imgUri = imgUrl?.toUri()?.buildUpon()?.scheme("https")?.build().toString()


        val uiHandler = Handler(Looper.getMainLooper())
        thread(start = true) {
            val bitmap = downloadBitmap(imgUri)
            uiHandler.post {
                imageView.setImageBitmap(bitmap)
            }
        }

    }

    private fun downloadBitmap(imageUrl: String): Bitmap? {
        return try {
            val conn = URL(imageUrl).openConnection()
            conn.connect()
            val inputStream = conn.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Exception $e")
            getBitmap(R.drawable.place_holder)
        }
    }

    private fun getBitmap(drawableRes: Int): Bitmap {
        val drawable: Drawable = context.resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

}