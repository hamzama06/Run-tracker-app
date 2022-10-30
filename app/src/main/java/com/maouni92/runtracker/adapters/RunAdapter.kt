package com.maouni92.runtracker.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maouni92.runtracker.R
import com.maouni92.runtracker.data.Run
import com.maouni92.runtracker.helper.AppExtensions.getFormattedTime
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter  : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)


    fun submitList(list: List<Run>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_run,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {



        val run = differ.currentList[position]
        holder.apply {
            Glide.with(this.itemView).load(run.image).into(imageView)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            dateTextView.text = dateFormat.format(calendar.time)

            val avgSpeed = "${run.averageSpeed}km/h"
            averageSpeedTextView.text = avgSpeed

            val distanceInKm = "${run.distance / 1000f}km"
            distanceTextView.text = distanceInKm

            timeTextView.text = run.time.getFormattedTime()

            val caloriesBurned = "${run.caloriesBurned}kcal"
            caloriesTextView.text = caloriesBurned
        }


    }

    inner class RunViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView){

        val imageView:ImageView = itemView.findViewById(R.id.run_image_view)
        val dateTextView:TextView = itemView.findViewById(R.id.date_text_view)
        val timeTextView:TextView = itemView.findViewById(R.id.time_text_view)
        val distanceTextView:TextView = itemView.findViewById(R.id.distance_text_view)
        val averageSpeedTextView:TextView = itemView.findViewById(R.id.average_speed_text_view)
        val caloriesTextView:TextView = itemView.findViewById(R.id.calories_text_view)
    }

}