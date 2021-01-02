package br.com.liebersonsantos.masteringrecyclerview.view.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.util.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import br.com.liebersonsantos.masteringrecyclerview.R
import br.com.liebersonsantos.masteringrecyclerview.model.Email
import kotlinx.android.synthetic.main.email_item.view.*

class EmailAdapter(val emails: MutableList<Email>) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null
    val selectedItems =
        SparseBooleanArray() //parecido com hashmap, porém, mais performático. consome menos recursos do device
    private var currentSelectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.email_item, parent, false)
        return EmailViewHolder(view)
    }

    override fun getItemCount(): Int = emails.count()

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(emails[position])
        holder.itemView.setOnClickListener {
            if (selectedItems.isNotEmpty()) onItemClick?.invoke(position)

        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(position)
            return@setOnLongClickListener true
        }

        if (currentSelectedPosition == position) currentSelectedPosition = -1
    }

    fun toggleSelection(position: Int) {
        currentSelectedPosition = position
        if (selectedItems[position, false]) {
            selectedItems.delete(position)
            emails[position].selected = false
        } else {
            selectedItems.put(position, true)
            emails[position].selected = true
        }

        notifyItemChanged(position)
    }

    fun deleteEmails() {
        emails.removeAll(emails.filter { it.selected })
        notifyDataSetChanged()
        currentSelectedPosition = -1
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
                    if (stared) R.drawable.ic_baseline_star_black_24 else R.drawable.ic_baseline_star_border_24
                )

                if (email.selected) {
                    itemView.txtIcon.background = itemView.txtIcon.oval(
                        Color.rgb(26, 115, 233)
                    )
                    itemView.background = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 32f
                        setColor(Color.rgb(232, 240, 253))
                    }
                } else {
                    itemView.background = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 32f
                        setColor(Color.WHITE)
                    }
                }

                if (selectedItems.isNotEmpty()) {
                    animate(itemView.txtIcon, email)
                }

            }

        }
    }

    private fun animate(view: TextView, email: Email) {
        val oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1F, 0F)
        val oa2 = ObjectAnimator.ofFloat(view, "scaleX", 0F, 1F)

        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()

        oa1.duration = 200
        oa2.duration = 200

        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (email.selected)
                    view.text = "\u2713"
                oa2.start()

            }
        })

        oa1.start()
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
}