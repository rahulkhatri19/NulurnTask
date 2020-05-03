package `in`.rahul.nulurntask.adapter

import `in`.rahul.nulurntask.R
import `in`.rahul.nulurntask.model.CourseDetailModel
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.course_detail_layout.view.*

class CourseDetailAdapter(val context: Context, val list: MutableList<CourseDetailModel>) :
    RecyclerView.Adapter<CourseDetailAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnSubject = view.btn_subject
        val btnDownload = view.btn_download
        val ivYoutube = view.iv_youtube
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.course_detail_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = list.get(position)
        holder.btnSubject.text = listItem.subjectName
        holder.btnDownload.text = listItem.download
        holder.ivYoutube.setOnClickListener {
            val appIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + listItem.youtubeLink))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + listItem.youtubeLink)
            )
            try {
                context.startActivity(appIntent)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(webIntent)
            }
        }
    }
}