package com.lightfeather.wide_awakefinancials.ui.util

import android.content.Context
import android.graphics.Canvas
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.ui.home.TransactionsListAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeGesture(val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val editColor = ContextCompat.getColor(context, R.color.cyan_blue_azure)
    private val deleteColor = ContextCompat.getColor(context, R.color.madder_lake)
    private val editIcon = R.drawable.ic_baseline_edit_24
    private val deleteIcon = R.drawable.ic_baseline_delete_24
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = true

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(editIcon)
            .addSwipeRightBackgroundColor(editColor)
            .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP,8)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
    override fun getSwipeDirs (recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder is TransactionsListAdapter.DateViewHolder) return 0
        return super.getSwipeDirs(recyclerView, viewHolder)
    }

}