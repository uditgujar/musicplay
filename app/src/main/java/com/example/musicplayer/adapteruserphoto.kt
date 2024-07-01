package com.example.musicplayer


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class adapteruserphoto(var context: Context, var array: ArrayList<dataclassuserphoto>,private val itemClickListener: OnItemClickListener
)
    : RecyclerView.Adapter<adapteruserphoto.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_photosuser,parent,false)
        return MyViewHolder(view,itemClickListener)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.posterImage.setImageResource(array[position].Image)
        holder.photographyitem.text =array[position].Name
        holder.Singersong.text=array[position].songname

    }

    class MyViewHolder(itemView: View, itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val photographyitem: TextView =itemView.findViewById<TextView>(R.id.singernameitem)
        val posterImage: ImageView =itemView.findViewById<ImageView>(R.id.Singerphoto)
        val Singersong:TextView=itemView.findViewById<TextView>(R.id.Singersong)


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(position)
                }
            }
        }
    }
}
