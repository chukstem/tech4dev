package chukstem.tech4dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import chukstem.tech4dev.Adapters.UsersAdapter
import chukstem.tech4dev.Models.User
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var qStation: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        qStation = Volley.newRequestQueue(this@MainActivity)

        setTitle("Tech4Dev")
        swipeRefreshLayout = findViewById(R.id.swipe)
        swipeRefreshLayout.setOnRefreshListener {
            getStation()
        }
        swipeRefreshLayout.isRefreshing = true
        getStation()
    }

    private fun getStation(){
        val aList = arrayListOf<User>()
        val request = object: JsonObjectRequest(Method.GET, "https://swapi.dev/api/people/", null, Response.Listener<JSONObject> { response ->
            
            val dObject = JSONObject(response.toString())
            try {
                val dUsers = JSONArray(dObject.get("results").toString())
                var id = 1
                for (i in 0 until dUsers.length()) {
                    var dataInner: JSONObject = dUsers.getJSONObject(i)
                    aList.add(
                        User(
                            dataInner.getString("name"),
                            dataInner.getString("height"),
                            dataInner.getString("gender"),
                            id++
                        )
                    )
                }
            val adapter = UsersAdapter(this, aList)
            ListView.adapter = adapter
            }catch (e: Exception){
                Toast.makeText(applicationContext, "Network error", Toast.LENGTH_LONG).show()
            }

            ListView.setOnItemClickListener { _, _, position, _ ->
                var intent: Intent? = Intent(this, UsersActivity::class.java)

                intent!!.putExtra("name",aList[position].NAME)
                intent!!.putExtra("url",aList[position].URL)

                startActivity(intent)
            }

            swipeRefreshLayout.isRefreshing = false
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

}
