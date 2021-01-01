package br.com.liebersonsantos.masteringrecyclerview.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper.*
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
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fakeit.init()
        setContentView(R.layout.activity_main)

        mAdapter = EmailAdapter(fakeEmails())

        with(rvMain) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        fab.setOnClickListener {
            addEmail()
            rvMain.scrollToPosition(0)
        }

        val helper = androidx.recyclerview.widget.ItemTouchHelper(
            ItemTouchHelper(UP or DOWN, LEFT)
        )

        helper.attachToRecyclerView(rvMain)
        mAdapter.onItemClick = {
            enableActionMode(it)
        }

        mAdapter.onItemLongClick = {
            enableActionMode(it)

        }

    }

    private fun enableActionMode(position: Int) {
        if (actionMode == null)
            actionMode = startSupportActionMode(object : ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    mode?.menuInflater?.inflate(R.menu.menu_delete, menu)
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    if (item?.itemId == R.id.action_delete){
                        mAdapter.deleteEmails()
                        mode?.finish()
                        return true
                    }
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode?) {
                    mAdapter.selectedItems.clear()
                    mAdapter.emails
                        .filter { it.selected }
                        .forEach { it.selected = false }
                    mAdapter.notifyDataSetChanged()
                    actionMode = null
                }

            })

        mAdapter.toggleSelection(position)
        val size = mAdapter.selectedItems.size()
        if (size == 0) {
            actionMode?.finish()
        } else {
            actionMode?.title = "$size"
            actionMode?.invalidate()
        }
    }

    private fun addEmail() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).parse(
            Fakeit.dateTime().dateFormatter()
        )

        mAdapter.emails.add(0, email {
            stared = false
            unread = false
            user = Fakeit.name().firstName()
            subject = Fakeit.company().name()
            date = SimpleDateFormat("d MMM", Locale("pt", "BR")).format(sdf)
            preview = mutableListOf<String>().apply {
                repeat(10) {
                    add(Fakeit.lorem().words())
                }
            }.joinToString(" ")
        })

        mAdapter.notifyItemInserted(0)
    }

    inner class ItemTouchHelper(dragDirs: Int, swipeDirs: Int) :
        androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            Collections.swap(mAdapter.emails, from, to)
            mAdapter.notifyItemMoved(from, to)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            mAdapter.emails.removeAt(viewHolder.adapterPosition)
            mAdapter.notifyItemRemoved(viewHolder.adapterPosition)

        }

    }
}