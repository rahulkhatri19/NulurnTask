package `in`.rahul.nulurntask.activity

import `in`.rahul.nulurntask.R
import `in`.rahul.nulurntask.adapter.CourseDetailAdapter
import `in`.rahul.nulurntask.model.CourseDetailModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_course_detail.*

class CourseDetailActivity : AppCompatActivity() {

    var stCourse = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        stCourse = intent?.getStringExtra("title")!!
        tv_course.text = stCourse

        val subjectList: MutableList<CourseDetailModel> = mutableListOf()

        for (i in 0..3) {
            subjectList.add(CourseDetailModel("Logical Reasoning", "Download Capsule", "o5XZ_ZXkB6I"))
            subjectList.add(CourseDetailModel("Analytical Reasoning", "Download Capsule", "E_2eFXQYGcg"))
        }
        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = CourseDetailAdapter(this, subjectList)
    }
}
