package com.pecawolf.charactersheet.ui.view

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class BottomSpacingDecoration(
    @DimenRes private val spacingVertical: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        getItemOffsets(outRect, parent.getChildLayoutPosition(view), parent)
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        super.getItemOffsets(outRect, itemPosition, parent)
        parent.adapter
            ?.takeIf { it.itemCount > 0 }
            ?.takeIf { itemPosition == it.itemCount - 1 }
            ?.apply {
                outRect.bottom = parent.resources.getDimensionPixelSize(spacingVertical)
            }
    }
}
