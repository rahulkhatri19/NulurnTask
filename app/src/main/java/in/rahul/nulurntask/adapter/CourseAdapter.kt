package `in`.rahul.nulurntask.adapter

import `in`.rahul.nulurntask.R
import `in`.rahul.nulurntask.model.CourseTypeModel
import `in`.rahul.nulurntask.utils.CommonUtils.LogMessage
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.course_layout.view.*
import kotlinx.android.synthetic.main.sub_course_layout.view.*


class CourseAdapter(val context: Context, val courseList: MutableList<CourseTypeModel>, val courseItemInterface:CourseItemInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val Type_Course = 1
    private val Type_SubCourse = 2
    private var selectionPosition = -1

    class CourseHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCourseTitle = view.tv_course_title
        val tvCourseSubTitle = view.tv_course_subtitle
    }

    class SubCourseHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSubCourseTitle = view.tv_sub_course_title
        val tvSubCourseSubTitle = view.tv_sub_course_subtitle
        val clSubCourse = view.cl_sub_course
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View
        if (viewType == Type_Course) {
            view = LayoutInflater.from(context).inflate(R.layout.course_layout, parent, false)
            return CourseHolder(view)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.sub_course_layout, parent, false)
            return SubCourseHolder(view)
        }
    }

    override fun getItemCount(): Int {
       return courseList.size
    }

    override fun getItemViewType(position: Int): Int {
      return if ( courseList.get(position).type == 1){
          Type_Course
      } else {
          Type_SubCourse
      }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val list = courseList.get(position)

        if (getItemViewType(position) == Type_Course){
            (holder as CourseHolder).tvCourseTitle.text = list.courseList?.courseName
            (holder as CourseHolder).tvCourseSubTitle.text = list.courseList?.courseFullName
        } else if (getItemViewType(position) == Type_SubCourse) {
            (holder as SubCourseHolder).tvSubCourseTitle.text = list.courseLevelList?.firstSubtitle
            (holder as SubCourseHolder).tvSubCourseSubTitle.text = list.courseLevelList?.lastSubtitle
            if (selectionPosition == position){
                (holder as SubCourseHolder).clSubCourse.background = ContextCompat.getDrawable(context, R.drawable.background_r_blue)
                LogMessage("course ad", "subname: ${list.courseLevelList?.firstSubtitle}, subfull: ${list.courseLevelList?.lastSubtitle}")
            } else {
                (holder as SubCourseHolder).clSubCourse.background = ContextCompat.getDrawable(context, R.drawable.background_r_white)
            }
            (holder as SubCourseHolder).clSubCourse.setOnClickListener {
                courseItemInterface.userChoice(list.courseLevelList?.firstSubtitle, list.courseLevelList?.lastSubtitle, true)
//                LogMessage("course add", "subname: ${list.courseLevelList?.firstSubtitle}, subfull: ${list.courseLevelList?.lastSubtitle}")

                if (selectionPosition >= 0)
                    notifyItemChanged(selectionPosition)
                    selectionPosition = (holder as SubCourseHolder).adapterPosition
                    notifyItemChanged(selectionPosition)

            }
        }
    }
    interface CourseItemInterface{
        fun userChoice(title:String?, subtitle:String?, clicked:Boolean)
    }
}