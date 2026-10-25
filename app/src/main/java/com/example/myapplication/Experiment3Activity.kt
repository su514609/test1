package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator

class Experiment3Activity : AppCompatActivity() {

    private val fragments = listOf(
        ListViewFragment(),
        AlertDialogFragment(),
        MenuXmlFragment(),
        ActionModeFragment(),
        RecyclerViewFragment()
    )

    private val titles = arrayOf(
        "ListView", "AlertDialog", "MenuTest", "ActionMode", "RecyclerView"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = titles[0]
        // 不显示返回箭头，参考图里没有
        // supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_experiment3)

        val viewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<com.google.android.material.tabs.TabLayout>(R.id.tab_layout)

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        // 切换 Tab 时更新标题
        viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                supportActionBar?.title = titles[position]
            }
        })
    }

    // 不再需要返回箭头处理
}
