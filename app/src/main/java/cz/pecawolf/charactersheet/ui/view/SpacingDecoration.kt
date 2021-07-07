package cz.pecawolf.charactersheet.ui.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingDecoration(
    private val spacingVertical: Int = 0,
    private val spacingHorizontal: Int = 0
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
        outRect.right = spacingHorizontal
        outRect.left = spacingHorizontal

        outRect.bottom = spacingVertical

        if (itemPosition == 0) outRect.top = spacingVertical
    }
}
