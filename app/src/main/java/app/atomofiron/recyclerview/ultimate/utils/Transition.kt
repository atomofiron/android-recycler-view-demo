package app.atomofiron.recyclerview.ultimate.utils

import android.view.View
import android.view.animation.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator.ItemHolderInfo
import kotlin.math.abs
import kotlin.math.min

sealed class Transition(
    val holders: Array<RecyclerView.ViewHolder>,
) {
    companion object {
        private val bounce = BounceInterpolator()
        private val accelerateDecelerate = AccelerateDecelerateInterpolator()
        private val decelerate = DecelerateInterpolator()
        private val accelerate = AccelerateInterpolator()
        private val overshoot = OvershootInterpolator()
        private val anticipate = AnticipateInterpolator()
        private val anticipateOvershoot = AnticipateOvershootInterpolator()
        private val appearing = overshoot
        private val disappearing = overshoot
        private val changing = overshoot
        private val moving = accelerateDecelerate

        private const val MAX_PROGRESS = 1f
        private const val SCALE_NORMAL = 1f

        private fun calc(from: Int, to: Int, progress: Float): Int = from + ((to - from) * progress).toInt()

        private fun Float.inverseProgress(): Float = MAX_PROGRESS - this

        private fun move(
            itemView: View,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo,
            progress: Float,
            deltaY: Int,
        ) {
            val interpolation = moving.getInterpolation(progress)
            itemView.left = calc(preLayoutInfo.left, postLayoutInfo.left, interpolation)
            itemView.top = calc(preLayoutInfo.top, postLayoutInfo.top, interpolation) + deltaY
            itemView.right = calc(preLayoutInfo.right, postLayoutInfo.right, interpolation)
            itemView.bottom = calc(preLayoutInfo.bottom, postLayoutInfo.bottom, interpolation) + deltaY
        }

        private fun appear(itemView: View, progress: Float) {
            val interpolation = appearing.getInterpolation(progress)
            itemView.scaleX = interpolation
            itemView.scaleY = interpolation
        }

        private fun disappear(itemView: View, progress: Float) {
            val interpolation = disappearing.getInterpolation(progress.inverseProgress())
            itemView.scaleX = interpolation
            itemView.scaleY = interpolation
        }

        fun areLayoutInfoTheSame(first: ItemHolderInfo, second: ItemHolderInfo): Boolean {
            return first.left == second.left &&
                    first.top == second.top &&
                    first.right == second.right &&
                    first.bottom == second.bottom
        }
    }

    class Appearance(
        private val holder: RecyclerView.ViewHolder,
        private val postLayoutInfo: ItemHolderInfo,
    ) : Transition(arrayOf(holder)) {

        init {
            prepare()
        }

        override fun onStep(progress: Float) {
            appear(holder.itemView, progress)
        }

        override fun reset() {
            holder.itemView.scaleX = SCALE_NORMAL
            holder.itemView.scaleY = SCALE_NORMAL
        }
    }

    class Disappearance(
        private val holder: RecyclerView.ViewHolder,
        private val preLayoutInfo: ItemHolderInfo,
    ) : Transition(arrayOf(holder)) {

        init {
            prepare()
        }

        override fun onStep(progress: Float) {
            disappear(holder.itemView, progress)
        }

        override fun reset() {
            holder.itemView.scaleX = SCALE_NORMAL
            holder.itemView.scaleY = SCALE_NORMAL
        }
    }

    class Change(
        private val oldHolder: RecyclerView.ViewHolder,
        private val newHolder: RecyclerView.ViewHolder,
        private val preLayoutInfo: ItemHolderInfo,
        private val postLayoutInfo: ItemHolderInfo,
    ) : Transition(
        when {
            oldHolder === newHolder -> arrayOf(newHolder)
            else -> arrayOf(oldHolder, newHolder)
        }
    ) {
        init {
            prepare()
        }

        override fun onStep(progress: Float) {
            if (oldHolder === newHolder) {
                val scale = 1.5f - abs(progress - 0.5f)
                newHolder.itemView.scaleX = changing.getInterpolation(scale)
                newHolder.itemView.scaleY = changing.getInterpolation(scale)
            } else {
                appear(newHolder.itemView, progress)
                disappear(oldHolder.itemView, progress)
                move(newHolder.itemView, preLayoutInfo, postLayoutInfo, progress, deltaY)
                move(oldHolder.itemView, preLayoutInfo, postLayoutInfo, progress, deltaY)
            }
        }

        override fun reset() {
            newHolder.itemView.scaleX = SCALE_NORMAL
            newHolder.itemView.scaleY = SCALE_NORMAL
            oldHolder.itemView.scaleX = SCALE_NORMAL
            oldHolder.itemView.scaleY = SCALE_NORMAL
        }
    }

    class Persist(
        private val holder: RecyclerView.ViewHolder,
        private val preLayoutInfo: ItemHolderInfo,
        private val postLayoutInfo: ItemHolderInfo,
    ) : Transition(arrayOf(holder)) {

        init {
            prepare()
        }

        override fun onStep(progress: Float) {
            move(holder.itemView, preLayoutInfo, postLayoutInfo, progress, deltaY)
        }

        override fun reset() = Unit
    }

    private var progress = 0f
    private val step = 0.01f

    private var lastY = 0
    protected var deltaY = 0
        private set

    /* это нельзя сделать в конструкторе суперкласса (Transition),
     * потому что поля дочернего класса ещё не инициализированы.
     */
    protected fun prepare() {
        onStep(progress)
        val itemView = holders.first().itemView
        lastY = itemView.top
    }

    fun nextStep(): Boolean {
        if (progress < MAX_PROGRESS) {
            progress = min(MAX_PROGRESS, progress + step)

            /* каждый раз между шагами анимации,
             * отслеживаем смещение ячеек в результате скроллинга пользователем,
             * чтобы при анимации перемещения делать поправку на скроллинг.
             */
            val itemView = holders.first().itemView
            deltaY += itemView.top - lastY
            onStep(progress)
            lastY = itemView.top
        }
        return progress >= MAX_PROGRESS
    }

    fun end() {
        progress = MAX_PROGRESS
        reset()
    }

    fun contains(holder: RecyclerView.ViewHolder): Boolean {
        return holders.find { it === holder } != null
    }

    protected abstract fun reset()
    protected abstract fun onStep(progress: Float)
}