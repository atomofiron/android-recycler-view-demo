package app.atomofiron.recyclerview.utils

// "равномерный" рандомизатор
class RandomFactory<T>(private val items: Array<T>) {
    companion object {
        private const val MAX_PROBABILITY = 1f
        private const val MIN_PROBABILITY = 0.08f
        private const val DISTRIBUTION_RATIO = 0.5f
    }

    private val probabilities = FloatArray(items.size) { MAX_PROBABILITY / items.size }

    fun next(): T {
        var luckyIndex = probabilities.indexOfFirst { Math.random() < it }
        if (luckyIndex == -1) {
            luckyIndex = probabilities.lastIndex
        }
        val probability = probabilities[luckyIndex]
        val probabilityToDistribute = when {
            probability < MIN_PROBABILITY -> probability
            else -> probability * DISTRIBUTION_RATIO
        }
        val pieceOfLuck = probabilityToDistribute / probabilities.size.dec();
        probabilities.forEachIndexed { index, _ ->
            when (index) {
                luckyIndex -> probabilities[index] -= probabilityToDistribute
                else -> probabilities[index] += pieceOfLuck
            }
        }
        return items[luckyIndex]
    }
}