package `in`.rahul.nulurntask.activity

import `in`.rahul.nulurntask.R
import `in`.rahul.nulurntask.adapter.CourseAdapter
import `in`.rahul.nulurntask.model.CourseLevelModel
import `in`.rahul.nulurntask.model.CourseModel
import `in`.rahul.nulurntask.model.CourseTypeModel
import `in`.rahul.nulurntask.utils.ApiUrlHelper.Companion.Courses
import `in`.rahul.nulurntask.utils.CommonUtils.LogMessage
import `in`.rahul.nulurntask.utils.CommonUtils.isOnline
import `in`.rahul.nulurntask.utils.CommonUtils.showMessage
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_course.*
import org.json.JSONObject

class CourseActivity : AppCompatActivity(), CourseAdapter.CourseItemInterface {

    var titleUser = ""
    var subtitleUser = ""
    var userBoolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        shimmer_layout.visibility = View.VISIBLE
        recycleView.visibility = View.GONE
        shimmer_layout.startShimmer()

//        getCourseData()

        ll_continue.setOnClickListener {
            if (userBoolean) {
                startActivity(
                    Intent(this, CourseDetailActivity::class.java).putExtra("title", titleUser)
                        .putExtra("subtitle", subtitleUser)
                )
            }
        }
    }

    private fun getCourseData() {

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, Courses, null, Response.Listener { response ->
                "Response: %s".format(response.toString())
                LogMessage("Course", "Response: %s".format(response.toString()))
                val mainObject = JSONObject(response.toString())
                mainObject.getString("message")
                if (mainObject.getString("message").equals("success")) {

                    val courseLevelList = mutableListOf<CourseLevelModel>()
                    val courseList = mutableListOf<CourseModel>()
                    val courseTypeList = mutableListOf<CourseTypeModel>()

                    val dataArray = mainObject.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(i)

                        val courseName = dataObject.getString("course_name")
                        val courseFullName = dataObject.getString("fullname")
                        courseList.add(CourseModel(courseName, courseFullName))
                        val courseTypeModel = CourseTypeModel()
                        courseTypeModel.type = 1
                        courseTypeModel.courseList = CourseModel(courseName, courseFullName)
                        courseTypeList.add(courseTypeModel)
                        LogMessage("Course Act", "cn: ${courseName}, fn:${courseFullName}")

//                    course_levels
                        val courseLevelArray = dataObject.getJSONArray("course_levels")
                        for (j in 0 until courseLevelArray.length()) {
                            val courseLevelObject = courseLevelArray.getJSONObject(j)

                            val firstSubtitle = courseLevelObject.getString("first_subtitle")
                            val lastSubtitle = courseLevelObject.getString("last_subtitle")
                            val id = courseLevelObject.getString("_id")
                            val courseLevel = courseLevelObject.getString("course_level")
                            val courseLevelId = courseLevelObject.getString("course_level_id")
                            val icon = courseLevelObject.getString("icon")

                            courseLevelList.add(CourseLevelModel(firstSubtitle, lastSubtitle))
                            LogMessage(
                                "Course Act sub",
                                "first: ${firstSubtitle}, last: ${lastSubtitle}"
                            )

                            val courseSubTypeModel = CourseTypeModel()
                            courseSubTypeModel.type = 2
                            courseSubTypeModel.courseLevelList =
                                CourseLevelModel(firstSubtitle, lastSubtitle)
                            courseTypeList.add(courseSubTypeModel)
                        }
                    }

//                recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    val layoutManager = GridLayoutManager(this, 2)
                    val adapter = CourseAdapter(this, courseTypeList, this)
                    recycleView.layoutManager = layoutManager
                    recycleView.adapter = adapter

                    shimmer_layout.stopShimmer()
                    shimmer_layout.visibility = View.GONE
                    recycleView.visibility = View.VISIBLE

                    layoutManager.setSpanSizeLookup(object : SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (adapter.getItemViewType(position) == 2) {
                                1
                            } else {
                                2
                            }
                        }
                    })
                }


            }, Response.ErrorListener {
                showMessage(this, "Please Try Again")
            })

        queue.add(jsonObjectRequest)
    }

    override fun userChoice(title: String?, subtitle: String?, clicked: Boolean) {
        tv_continue.setTextColor(Color.parseColor("#000000"))
        titleUser = title!!
        subtitleUser = subtitle!!
        userBoolean = clicked
        updateView(clicked)
    }

    private fun updateView(clicked: Boolean) {
        if (clicked) {
            tv_continue.setTextColor(Color.parseColor("#000000"))
        } else {
            tv_continue.setTextColor(Color.parseColor("#dedede"))
        }
    }
    fun internetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please Connect to Internet")
        builder.setCancelable(false).setIcon(R.drawable.ic_internet)
            .setTitle("No Internet Connection")
        builder.setPositiveButton(
            "OK"
        ) { dialog, _ ->
            dialog.cancel()
           finish()
        }.setNegativeButton("Retry") { dialog, _ ->
            if (!isOnline(this)) {
//                internetDialog()
            } else {
                dialog.dismiss()
                getCourseData()
            }
        }
        val alert = builder.create()
        alert.show()
    }

    override fun onResume() {
        super.onResume()
        shimmer_layout.startShimmer()
        tv_continue.setTextColor(Color.parseColor("#dedede"))
        if (!isOnline(this)){
            internetDialog()
        } else {
            getCourseData()
        }
    }

    override fun onPause() {
        shimmer_layout.stopShimmer()
        super.onPause()
    }
}
