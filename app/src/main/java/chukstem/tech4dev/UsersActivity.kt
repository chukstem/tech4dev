package chukstem.tech4dev
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import chukstem.tech4dev.Adapters.UsersAdapter
import chukstem.tech4dev.Models.User
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_users.*
import org.json.JSONObject

class UsersActivity : AppCompatActivity() {

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var body: LinearLayout
    private var qStation: RequestQueue? = null
    var name: String = ""
    var url: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        var actionBar=getSupportActionBar()

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        name = intent.getStringExtra("name").toString()
        url = intent.getIntExtra("url", 1)

        qStation = Volley.newRequestQueue(this@UsersActivity)

        setTitle(name)
        body = findViewById(R.id.body)
        swipeRefreshLayout = findViewById(R.id.swipe)
        swipeRefreshLayout.setOnRefreshListener {
            getStation()
        }
        swipeRefreshLayout.isRefreshing = true
        getStation()

        Name.text = "Name: ${name}"

    }
    private fun getStation(){
        val request = object: JsonObjectRequest(Method.GET, "https://swapi.dev/api/people/$url/", null, Response.Listener<JSONObject> { response ->

            val dObject = JSONObject(response.toString())
            try {
                var height = dObject.getString("height")
                var gender = dObject.getString("gender")
                var age = dObject.getString("birth_year")

                Gender.text = "Gender: ${gender}"
                Height.text = "Height: ${height}"
                Age.text = "Age: ${age}"
            }catch (e: Exception){
                Toast.makeText(applicationContext, "Network error", Toast.LENGTH_LONG).show()
            }

            swipeRefreshLayout.isRefreshing = false
            body.visibility = View.VISIBLE
        }, Response.ErrorListener { e ->
            swipeRefreshLayout.isRefreshing = false
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["accept"] = "application/json"
                return headers
            }
        }
        qStation!!.add(request)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
