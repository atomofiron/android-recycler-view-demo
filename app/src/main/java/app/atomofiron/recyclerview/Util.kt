package app.atomofiron.recyclerview

import android.util.Log

/* best way чтобы логгировать свой код.
 * если в Logcat отфильтровать по уровню Error,
 * в поиск вписать фрагмент имени пакета приложения и выбрать No Filters,
 * то в логах будут только крэши и наши логи.
 * как раз только то, что для нас важно.
 */
fun Any.say(s: String) {
    Log.e("recyclerview", "[${javaClass.simpleName}] $s")
}

