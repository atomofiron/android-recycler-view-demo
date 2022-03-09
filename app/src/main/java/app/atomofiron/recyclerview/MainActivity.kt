package app.atomofiron.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import app.atomofiron.recyclerview.databinding.ActivityMainBinding
import app.atomofiron.recyclerview.list_view.ListViewAdapter
import app.atomofiron.recyclerview.ultimate.UltimateAdapter
import app.atomofiron.recyclerview.easy.EasyAdapter
import app.atomofiron.recyclerview.ultimate.api.UltimateItemListenerImpl
import app.atomofiron.recyclerview.ultimate.utils.CatDecoration
import app.atomofiron.recyclerview.ultimate.utils.UltimateItemAnimator
import app.atomofiron.recyclerview.ultimate.data.StringItem
import app.atomofiron.recyclerview.medium.MediumListAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var dividerItemDecoration: RecyclerView.ItemDecoration
    private lateinit var catDecoration: RecyclerView.ItemDecoration
    private lateinit var defaultItemAnimator: RecyclerView.ItemAnimator
    private val ultimateItemAnimator = UltimateItemAnimator(lifecycle)

    private enum class ListType {
        LIST_VIEW, EASY, MEDIUM, ULTIMATE,
    }

    private var listType = ListType.ULTIMATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = 0x01000000
        viewBinding = ActivityMainBinding.bind(findViewById(R.id.main_root))

        dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        catDecoration = CatDecoration(this)
        defaultItemAnimator = viewBinding.mainRecycler.itemAnimator!!
        //viewModel = ViewModelProvider(this)[MainViewModel::class]

        setSupportActionBar(viewBinding.mainToolbar)
        configListVew()
        applyInsets()
        switchList()

        subscribeToData()
    }

    private fun subscribeToData() {
        /*viewModel.viewModelScope.launch {
            viewModel.listItems.collect { items ->
                ultimateAdapter.setItems(items)
            }
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this).inflate(R.menu.toobar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        listType = when (item.itemId) {
            R.id.menu_listview -> ListType.LIST_VIEW
            R.id.menu_easy -> ListType.EASY
            R.id.menu_simple -> ListType.MEDIUM
            R.id.menu_ultimate -> ListType.ULTIMATE
            else -> return false
        }.apply {
            if (this == listType) return false
        }
        switchList()
        return true
    }

    private fun configListVew() {
        viewBinding.mainList.adapter = ListViewAdapter()
        val inflater = LayoutInflater.from(this)
        val header = inflater.inflate(R.layout.item_search, viewBinding.mainList, false)
        val footer = inflater.inflate(R.layout.item_button_add, viewBinding.mainList, false)
        viewBinding.mainList.addHeaderView(header)
        viewBinding.mainList.addFooterView(footer)
    }

    private fun switchList() {
        viewBinding.mainRecycler.isVisible = true
        viewBinding.mainList.isVisible = false
        viewBinding.mainRecycler.clearItemDecorations()
        when (listType) {
            ListType.LIST_VIEW -> toListView()
            ListType.EASY -> toEasyList()
            ListType.MEDIUM -> toMediumList()
            ListType.ULTIMATE -> toUltimateList()
        }
        viewBinding.root.requestApplyInsets()
    }

    // обработка инсетов это отдельная большая тема
    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.mainAppbar) { appbar, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            appbar.updatePaddingRelative(top = systemBars.top)
            insets
        }
        val recyclerView = viewBinding.mainRecycler
        val listView = viewBinding.mainList
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { _, insets ->
            val half = recyclerView.resources.getDimensionPixelSize(R.dimen.common_margin_half)
            val cat = recyclerView.resources.getDimensionPixelSize(R.dimen.cat_part)
            val padding = when (listType) {
                ListType.LIST_VIEW -> half
                ListType.EASY -> half
                ListType.MEDIUM -> half
                ListType.ULTIMATE -> cat
            }
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            recyclerView.updatePaddingRelative(top = padding, bottom = systemBars.bottom + padding)
            listView.updatePaddingRelative(top = padding, bottom = systemBars.bottom + padding)
            insets
        }
    }

    private fun RecyclerView.clearItemDecorations() {
        while (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
    }

    private fun toListView() {
        viewBinding.mainRecycler.isVisible = false
        viewBinding.mainList.isVisible = true
    }

    private fun toEasyList() {
        viewBinding.mainRecycler.run {
            itemAnimator = null
            layoutManager = LinearLayoutManager(context)
            this.adapter = EasyAdapter()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun toMediumList() {
        viewBinding.mainRecycler.run {
            itemAnimator = defaultItemAnimator
            val adapter = MediumListAdapter().apply {
                val items = IntArray(30) { it }.map { StringItem("Item $it") }
                submitList(items)
            }
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)

            lifecycleScope.launch {
                delay(2000)
                val items = IntArray(30) { if (it < 5) it + 100 else it }.map { StringItem("Item $it") }
                adapter.submitList(items)
            }
        }
    }

    private fun toUltimateList() {
        viewBinding.mainRecycler.run {
            addItemDecoration(CatDecoration(context))
            itemAnimator = ultimateItemAnimator
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            val adapter = UltimateAdapter()
            adapter.setListener(UltimateItemListenerImpl(adapter))
            startDemoActions(adapter)
            this.adapter = adapter
        }
    }

    private fun startDemoActions(adapter: UltimateAdapter) {
        lifecycleScope.launch {
            /*val insert = CardItem("Changed Item", UltimateDataFactory.randomColor(), System.currentTimeMillis())
            val replace = StringItem("Inserted Item")*/

            /*delay(2000)
            adapter.insertItem(1, insert)
            delay(2000)
            adapter.replaceItem(1, replace)
            delay(2000)
            adapter.swap(3, 5)*/

            /*delay(2000)
            val old = ArrayList(adapter.items)
            val new = ArrayList(old)
            new.add(1, insert)
            new[4] = replace
            adapter.setItems(new)*/
        }
    }
}