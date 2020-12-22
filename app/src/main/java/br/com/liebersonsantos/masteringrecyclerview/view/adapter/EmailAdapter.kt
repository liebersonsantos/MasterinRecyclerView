package br.com.liebersonsantos.masteringrecyclerview.view.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import br.com.liebersonsantos.masteringrecyclerview.R
import br.com.liebersonsantos.masteringrecyclerview.model.Email
import kotlinx.android.synthetic.main.email_item.view.*

class EmailAdapter(val emails: MutableList<Email>) :
    RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.email_item, parent, false)
        return EmailViewHolder(view)
    }

    override fun getItemCount(): Int = emails.count()

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(emails[position])
    }

    inner class EmailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(email: Email) {
            with(email) {
                val hash = user.hashCode()
                itemView.txtIcon.text = user.first().toString()
                itemView.txtIcon.background = itemView.oval(Color.rgb(hash, hash / 2, 0))
                itemView.txtUser.text = user
                itemView.txtSubject.text = subject
                itemView.txtPreview.text = preview
                itemView.txtDate.text = date

                itemView.txtUser.setTypeface(Typeface.DEFAULT, if (!unread) BOLD else NORMAL)
                itemView.txtSubject.setTypeface(Typeface.DEFAULT, if (!unread) BOLD else NORMAL)
                itemView.txtDate.setTypeface(Typeface.DEFAULT, if (!unread) BOLD else NORMAL)

                itemView.imgStar.setImageResource(
                    if (stared) R.drawable.ic_baseline_star_black_24 else R.drawable.ic_baseline_star_border_24)
            }
        }

    }
}

fun View.oval(@ColorInt color: Int): ShapeDrawable {
    val oval = ShapeDrawable(OvalShape())
    with(oval) {
        intrinsicHeight = height
        intrinsicWidth = width
        paint.color = color
    }

    return oval
}