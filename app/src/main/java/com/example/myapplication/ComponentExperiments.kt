package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

// ========== 实验3-1：ListView + SimpleAdapter ==========
class ListViewFragment : Fragment(R.layout.activity_listview) {

    private val animalNames = arrayOf("Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant")
    private val animalImages = intArrayOf(
        R.drawable.lion, R.drawable.tiger, R.drawable.monkey,
        R.drawable.dog, R.drawable.cat, R.drawable.elephant
    )
    private var selectedPos = -1
    private lateinit var listContainer: LinearLayout
    private lateinit var btnSelected: TextView
    private val itemViews = ArrayList<View>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listContainer = view.findViewById(R.id.ll_animal_list)
        btnSelected = view.findViewById(R.id.btn_selected)

        // 动态添加列表项，按钮紧贴最后一项下方
        for (i in animalNames.indices) {
            val itemView = layoutInflater.inflate(R.layout.item_animal, null)
            itemView.tag = i

            val tv = itemView.findViewById<TextView>(R.id.tv_name)
            val iv = itemView.findViewById<ImageView>(R.id.iv_animal)
            tv.text = animalNames[i]
            iv.setImageResource(animalImages[i])

            itemView.setOnClickListener {
                selectedPos = i
                updateSelection()

                val name = animalNames[i]
                Toast.makeText(requireContext(), "选中了：$name", Toast.LENGTH_SHORT).show()
                showNotification(name)

                btnSelected.text = name
                btnSelected.visibility = View.VISIBLE
            }

            listContainer.addView(itemView)
            itemViews.add(itemView)

            // 添加黑色分隔线（除了最后一项）
            if (i < animalNames.size - 1) {
                val divider = View(requireContext())
                divider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1
                )
                divider.setBackgroundColor(Color.BLACK)
                listContainer.addView(divider)
            }
        }

        btnSelected.setOnClickListener {
            Toast.makeText(requireContext(), "当前选中：${btnSelected.text}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSelection() {
        for ((idx, itemView) in itemViews.withIndex()) {
            val tv = itemView.findViewById<TextView>(R.id.tv_name)
            if (idx == selectedPos) {
                itemView.setBackgroundColor(Color.parseColor("#8E0000"))
                tv.setTextColor(Color.WHITE)
            } else {
                itemView.setBackgroundColor(Color.WHITE)
                tv.setTextColor(Color.parseColor("#333333"))
            }
        }
    }

    private fun showNotification(name: String) {
        val channelId = "animal_notification"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "动物通知", NotificationManager.IMPORTANCE_DEFAULT
            )
            val nm = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(name)
            .setContentText("这是从 ListView 发送的通知")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(requireContext())
            .notify(System.currentTimeMillis().toInt(), notification)
    }
}

// ========== 实验3-2：自定义 AlertDialog ==========
class AlertDialogFragment : Fragment(R.layout.activity_listview) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showLoginDialog()
    }

    private fun showLoginDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_login, null)
        val etUsername = dialogView.findViewById<android.widget.EditText>(R.id.et_username)
        val etPassword = dialogView.findViewById<android.widget.EditText>(R.id.et_password)
        val btnCancel = dialogView.findViewById<android.widget.Button>(R.id.btn_cancel)
        val btnSignIn = dialogView.findViewById<android.widget.Button>(R.id.btn_signin)

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnSignIn.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(requireContext(), "登录成功：$username", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "请输入用户名和密码", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        dialog.show()
    }
}

// ========== 实验3-3：XML 定义菜单 ==========
class MenuXmlFragment : Fragment(R.layout.activity_menu_test) {

    private lateinit var tvTestText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvTestText = view.findViewById(R.id.tv_test_text)
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_size_small -> { tvTestText.textSize = 10f; true }
            R.id.menu_size_medium -> { tvTestText.textSize = 16f; true }
            R.id.menu_size_large -> { tvTestText.textSize = 20f; true }
            R.id.menu_normal -> {
                Toast.makeText(requireContext(), "点击了普通菜单项", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_color_red -> { tvTestText.setTextColor(Color.RED); true }
            R.id.menu_color_black -> { tvTestText.setTextColor(Color.BLACK); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

// ========== 实验3-4：ActionMode 上下文菜单 ==========
class ActionModeFragment : Fragment(R.layout.activity_action_mode) {

    private lateinit var listView: android.widget.ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var customBar: View
    private lateinit var tvActionModeTitle: android.widget.TextView
    private val items = mutableListOf("One", "Two", "Three", "Four", "Five")
    private val selectedPositions = mutableSetOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listView = view.findViewById(R.id.lv_action_mode)
        customBar = view.findViewById(R.id.custom_actionmode_bar)
        tvActionModeTitle = view.findViewById(R.id.tv_actionmode_title)

        adapter = ArrayAdapter(
            requireContext(), R.layout.item_action_mode, R.id.tv_action_name, items
        )
        listView.adapter = adapter

        listView.setOnItemClickListener { _, v, position, _ ->
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position)
                v.setBackgroundColor(Color.WHITE)
            } else {
                selectedPositions.add(position)
                v.setBackgroundColor(Color.parseColor("#81D4FA"))
            }
            updateBar()
        }

        customBar.findViewById<android.widget.ImageView>(R.id.iv_check).setOnClickListener {
            val sorted = selectedPositions.sortedDescending()
            for (pos in sorted) {
                if (pos < items.size) {
                    Toast.makeText(requireContext(), "删除了：${items[pos]}", Toast.LENGTH_SHORT).show()
                    items.removeAt(pos)
                }
            }
            selectedPositions.clear()
            adapter.notifyDataSetChanged()
            for (i in 0 until listView.childCount) {
                listView.getChildAt(i).setBackgroundColor(Color.WHITE)
            }
            updateBar()
        }
    }

    private fun updateBar() {
        if (selectedPositions.isNotEmpty()) {
            customBar.visibility = View.VISIBLE
            tvActionModeTitle.text = "${selectedPositions.size} selected"
        } else {
            customBar.visibility = View.GONE
        }
    }
}

// ========== 实验3-5：RecyclerView ==========
class RecyclerViewFragment : Fragment(R.layout.activity_recycler_view) {

    private val animalNames = arrayOf("Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant")
    private val animalImages = intArrayOf(
        R.drawable.lion, R.drawable.tiger, R.drawable.monkey,
        R.drawable.dog, R.drawable.cat, R.drawable.elephant
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_animals)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AnimalAdapter(animalNames.toList(), animalImages.toList()) { name ->
            Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show()
        }
    }

    class AnimalAdapter(
        private val names: List<String>,
        private val images: List<Int>,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.Adapter<AnimalViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
            val itemView = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_animal, parent, false)
            return AnimalViewHolder(itemView)
        }
        override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
            holder.bind(names[position], images[position], onItemClick)
        }
        override fun getItemCount(): Int = names.size
    }

    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.iv_animal)
        private val textView = itemView.findViewById<TextView>(R.id.tv_name)
        fun bind(name: String, imageRes: Int, onClick: (String) -> Unit) {
            textView.text = name
            imageView.setImageResource(imageRes)
            itemView.setOnClickListener { onClick(name) }
        }
    }
}
