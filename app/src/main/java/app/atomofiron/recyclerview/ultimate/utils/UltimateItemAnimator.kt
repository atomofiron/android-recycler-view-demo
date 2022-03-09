package app.atomofiron.recyclerview.ultimate.utils

import android.animation.ValueAnimator
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView

class UltimateItemAnimator(lifecycle: Lifecycle) : RecyclerView.ItemAnimator(), ValueAnimator.AnimatorUpdateListener {

    private val animator = ValueAnimator.ofInt(0)
    private val transitions = mutableListOf<Transition>()

    init {
        animator.duration = 1000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener(this)
        animator.start()

        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                animator.cancel()
                // необходимо удалить слушатель, иначе он (этот аниматор) утечёт
                animator.removeUpdateListener(this)
            }
        }
        lifecycle.addObserver(lifecycleObserver)
    }

    override fun animateDisappearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo?
    ): Boolean {
        endIfExists(viewHolder)

        val transition = Transition.Disappearance(viewHolder, preLayoutInfo)
        transitions.add(transition)
        dispatchAnimationStarted(viewHolder)
        return false
    }

    override fun animateAppearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo?,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        endIfExists(viewHolder)

        val transition = Transition.Appearance(viewHolder, postLayoutInfo)
        transitions.add(transition)
        dispatchAnimationStarted(viewHolder)
        return false
    }

    override fun animatePersistence(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        endIfExists(viewHolder)

        val areLayoutInfoTheSame = Transition.areLayoutInfoTheSame(preLayoutInfo, postLayoutInfo)
        when {
            areLayoutInfoTheSame -> {
                dispatchAnimationStarted(viewHolder)
                dispatchAnimationFinished(viewHolder)
            }
            else -> {
                val transition = Transition.Persist(viewHolder, preLayoutInfo, postLayoutInfo)
                transitions.add(transition)
                dispatchAnimationStarted(viewHolder)
            }
        }
        return false
    }

    override fun recordPostLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder
    ): ItemHolderInfo {
        return super.recordPostLayoutInformation(state, viewHolder)
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        endIfExists(oldHolder)
        endIfExists(newHolder)

        val transition = Transition.Change(oldHolder, newHolder, preLayoutInfo, postLayoutInfo)
        transitions.add(transition)
        dispatchAnimationStarted(transition.holders)
        return false
    }

    override fun runPendingAnimations() = Unit

    override fun endAnimation(holder: RecyclerView.ViewHolder) = endIfExists(holder)

    override fun endAnimations() {
        val iterator = transitions.iterator()
        while (iterator.hasNext()) {
            val transition = iterator.next()
            transition.end()
            iterator.remove()
            dispatchAnimationFinished(transition.holders)
        }
    }

    private fun dispatchAnimationStarted(holders: Array<RecyclerView.ViewHolder>) {
        holders.forEach {
            dispatchAnimationStarted(it)
        }
    }

    private fun dispatchAnimationFinished(holders: Array<RecyclerView.ViewHolder>) {
        holders.forEach {
            dispatchAnimationFinished(it)
        }
    }

    private fun endIfExists(holder: RecyclerView.ViewHolder) {
        val index = transitions.indexOfFirst { it.contains(holder) }
        if (index != -1) {
            val transition = transitions.removeAt(index)
            transition.end()
            dispatchAnimationFinished(transition.holders)
        }
    }

    override fun isRunning(): Boolean = transitions.isNotEmpty()

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val iterator = transitions.iterator()
        while (iterator.hasNext()) {
            val transition = iterator.next()
            val isOver = transition.nextStep()
            if (isOver) {
                transition.end()
                iterator.remove()
                dispatchAnimationFinished(transition.holders)
            }
        }
    }
}