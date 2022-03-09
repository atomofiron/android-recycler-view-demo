package app.atomofiron.recyclerview.ultimate.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.AnimatedImageDrawable
import androidx.recyclerview.widget.RecyclerView
import app.atomofiron.recyclerview.R
import kotlin.math.max
import kotlin.math.min

class CatDecoration(context: Context) : RecyclerView.ItemDecoration() {
    companion object {
        private const val HEADER_FILE_NAME = "cat_header.gif"
        private const val MIDDLE_FILE_NAME = "cat_body.gif"
        private const val FOOTER_FILE_NAME = "cat_footer.gif"
    }

    private val catPartSize = context.resources.getDimensionPixelSize(R.dimen.cat_part)

    private val header = ImageDecoder.createSource(context.assets, HEADER_FILE_NAME).run {
        ImageDecoder.decodeDrawable(this) as AnimatedImageDrawable
    }
    private val body = ImageDecoder.createSource(context.assets, MIDDLE_FILE_NAME).run {
        ImageDecoder.decodeDrawable(this) as AnimatedImageDrawable
    }
    private val footer = ImageDecoder.createSource(context.assets, FOOTER_FILE_NAME).run {
        ImageDecoder.decodeDrawable(this) as AnimatedImageDrawable
    }

    init {
        header.start()
        body.start()
        footer.start()
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val lastIndex = parent.adapter?.itemCount?.dec()
        if (lastIndex == null || parent.childCount == 0 || parent.childCount == 0) {
            return
        }

        val size = header.intrinsicWidth
        val scale = catPartSize.toFloat() / size
        val count = canvas.save()
        canvas.translate((parent.width - catPartSize) / 2f, 0f)

        val bodyTop = parent.findMinTop()
        val bodyBottom = parent.findMaxBottom()
        var partCount = canvas.save()
        canvas.translate(0f, (bodyTop - catPartSize).toFloat())
        canvas.scale(scale, scale)
        header.draw(canvas)
        canvas.restoreToCount(partCount)

        partCount = canvas.save()
        canvas.translate(0f, bodyBottom.toFloat())
        canvas.scale(scale, scale)
        footer.draw(canvas)
        canvas.restoreToCount(partCount)

        canvas.translate(0f, bodyTop.toFloat())
        val bodyDrawRange = bodyBottom - bodyTop
        val bodyDif = (parent.height - catPartSize) % catPartSize
        var bodyCount = (parent.height - catPartSize) / catPartSize
        if (bodyDif >= catPartSize / 2) bodyCount++
        val bodyScale = bodyDrawRange.toFloat() / (catPartSize * bodyCount)
        canvas.scale(scale, scale * bodyScale)
        for (i in 0 until bodyCount) {
            body.draw(canvas)
            canvas.translate(0f, size.toFloat())
        }

        canvas.restoreToCount(count)
    }

    private fun RecyclerView.findMinTop(): Int {
        var minTop = height
        for (i in 0 until childCount) {
            minTop = min(minTop, getChildAt(i).top)
            if (minTop <= 0) break
        }
        return max(0, minTop)
    }

    private fun RecyclerView.findMaxBottom(): Int {
        var maxBottom = 0
        for (i in 0 until childCount) {
            maxBottom = max(maxBottom, getChildAt(i).bottom)
            if (maxBottom >= height) break
        }
        return min(height, maxBottom)
    }
}