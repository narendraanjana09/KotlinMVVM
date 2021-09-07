package com.nsa.navigation.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nsa.navigation.R
import com.nsa.navigation.models.RecylcerData
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import android.widget.RelativeLayout


class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>(){


   var items =ArrayList<RecylcerData>()

    fun setUpdatedData(items :ArrayList<RecylcerData>){
        this.items=items
        notifyDataSetChanged()

    }

    class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
        val posterIV = view.findViewById<ImageView>(R.id.posterIV)
        val titleTV = view.findViewById<TextView>(R.id.titleTV)
        val yearTV = view.findViewById<TextView>(R.id.yearTV)
        fun bind(data: RecylcerData){
            if(false){//data.Title.length>18){
                titleTV.text=data.Title.subSequence(0,17).toString()+"..."
            }else{
                titleTV.text=data.Title
            }
            yearTV.text=data.Year

            val url = data.Poster

            Picasso.get().load(url)
                .into(posterIV,object : Callback{
                    override fun onSuccess() {
                        Log.e("TAG","image Loaded")
                    }

                    override fun onError(e: Exception?) {
                        val res: Int = R.drawable.ic_baseline_image_24
                       posterIV.setImageResource(res)
                    }

                })
//            Picasso.get().load(url)
//                .into(object: com.squareup.picasso.Target {
//                    override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
//                        val res: Int = R.drawable.ic_baseline_image_24
//                        posterIV.setImageResource(res)
//                        Log.e("TAG","error"+ e?.message.toString() )
//                    }
//
//                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                    }
//
//                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                        posterIV.setImageBitmap(bitmap)
//                    //    checkHW(posterIV,bitmap)
//
//                    }
//                })
        }

        private fun checkHW(posterIV: ImageView, bitmap: Bitmap?) {
            var h=0;
            var w=0;
            if(bitmap!=null){
                 h = bitmap?.height
                 w = bitmap?.width
            }


            val aspect_ratio = w.toFloat() / h.toFloat()

            val layoutParams: RelativeLayout.LayoutParams
            layoutParams = if (aspect_ratio > 1) {
                RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
            } else {
                RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
            }

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            posterIV.setLayoutParams(layoutParams)



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    override fun getItemCount(): Int {
       return items.size
    }
}