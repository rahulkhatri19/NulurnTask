package `in`.rahul.nulurntask.activity

import `in`.rahul.nulurntask.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onSkip(view: View){
        startActivity(Intent(this, CourseActivity::class.java))
        finish()
    }
}
