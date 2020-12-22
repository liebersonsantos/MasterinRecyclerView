package br.com.liebersonsantos.masteringrecyclerview.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.liebersonsantos.masteringrecyclerview.R
import br.com.liebersonsantos.masteringrecyclerview.model.email
import br.com.liebersonsantos.masteringrecyclerview.model.fakeEmails
import br.com.liebersonsantos.masteringrecyclerview.view.adapter.EmailAdapter
import com.mooveit.library.Fakeit
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter: EmailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fakeit.init()
        setContentView(R.layout.activity_main)

        mAdapter = EmailAdapter(fakeEmails())

        with(rvMain){
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        fab.setOnClickListener {
            addEmail()
            rvMain.scrollToPosition(0)
        }


    }

    fun addEmail(){
        val sdf =  SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).parse(
            Fakeit.dateTime().dateFormatter()
        )

        mAdapter.emails.add(0, email {
            stared = false
            unread = false
            user = Fakeit.name().firstName()
            subject = Fakeit.company().name()
            date = SimpleDateFormat("d MMM", Locale("pt", "BR")).format(sdf)
            preview = mutableListOf<String>().apply {
                repeat(10){
                    add(Fakeit.lorem().words())
                }
            }.joinToString(" ")
        })

        mAdapter.notifyItemInserted(0)
    }
}