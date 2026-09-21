package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ExperimentTabs()
            }
        }
    }
}

// ========== 主界面 - 实验选择 Tab ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperimentTabs() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("线性布局", "表格布局", "约束布局1", "约束布局2", "Compose任务")

    Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontSize = 12.sp) }
                    )
                }
            }
            when (selectedTab) {
                0 -> LinearLayoutExperiment()
                1 -> TableLayoutExperiment()
                2 -> ConstraintLayoutCalculator()
                3 -> ConstraintLayoutSpaceTravel()
                4 -> ComposeTaskApp()
            }
        }
}

// ========== 实验1：线性布局 - 4x4网格 ==========
@Composable
fun LinearLayoutExperiment() {
    val rows = listOf("One", "Two", "Three", "Four")
    val cols = listOf("One", "Two", "Three", "Four")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                cols.forEach { col ->
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(64.dp)
                            .border(1.dp, Color(0xFF555555))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$row,$col",
                            color = Color(0xFFCCCCCC),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

// ========== 实验2：表格布局 - 菜单列表 ==========
@Composable
fun TableLayoutExperiment() {
    val menuItems = listOf(
        Triple("Open...", "Ctrl-O", false),
        Triple("Save...", "Ctrl-S", false),
        Triple("Save As...", "Ctrl-Shift-S", false),
        Triple("Import...", null, true),
        Triple("Export...", "Ctrl-E", true),
        Triple("Quit", null, false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // 标题栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF888888))
                .padding(vertical = 10.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "Hello TableLayout",
                color = Color.White,
                fontSize = 15.sp
            )
        }

        // 菜单
        menuItems.forEachIndexed { index, (label, shortcut, hasX) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1A1A))
                    .padding(vertical = 14.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (hasX) {
                        Text(
                            text = "X ",
                            color = Color(0xFFCCCCCC),
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = label,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
                if (shortcut != null) {
                    Text(
                        text = shortcut,
                        color = Color(0xFFAAAAAA),
                        fontSize = 13.sp
                    )
                }
            }
            // 分隔线：Save As 后、Export 后
            if (index == 2 || index == 4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF444444))
                )
            }
        }
    }
}

// ========== 实验3：约束布局1 - 计算器 ==========
@Composable
fun ConstraintLayoutCalculator() {
    var displayText by remember { mutableStateOf("0.0") }

    val buttons = listOf(
        listOf("7", "8", "9", "+"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "+"),
        listOf("-", "0", "=", "-")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // 标题
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2E7D32))
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "ConstraintLayoutTest",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Input",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 显示区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFD4C48A), RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = displayText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 按钮区域
        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { btn ->
                    Button(
                        onClick = {
                            if (btn == "=") {
                                displayText = "0.0"
                            } else {
                                if (displayText == "0.0") {
                                    displayText = btn
                                } else {
                                    displayText += btn
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE0E0E0),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = btn, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

// ========== 实验4：约束布局2 - 太空旅行 ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstraintLayoutSpaceTravel() {
    var oneWay by remember { mutableStateOf(false) }
    var travellers by remember { mutableIntStateOf(1) }
    var selectedTab by remember { mutableIntStateOf(2) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 顶部图标导航栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                R.drawable.space_station_icon to "Space Stations",
                R.drawable.rocket_icon to "Flights",
                R.drawable.rover_icon to "Rovers"
            )
            items.forEachIndexed { index, (iconRes, title) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(text = title, fontSize = 10.sp, color = Color.Black)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFDDDDDD))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // DCA <-> MARS 区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF2E7D32))
                            .padding(horizontal = 50.dp, vertical = 28.dp)
                    ) {
                        Text("DCA", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF2E7D32))
                            .padding(horizontal = 50.dp, vertical = 28.dp)
                    ) {
                        Text("MARS", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
                // 白色底框 + 箭头（同样大）
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .size(52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.double_arrows),
                        contentDescription = "swap",
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // One Way 开关 (靠左窄条)
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .background(Color(0xFFE65100))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("One Way", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                Switch(
                    checked = oneWay,
                    onCheckedChange = { oneWay = it },
                    modifier = Modifier.height(24.dp),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF8D6E63)
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 旅客数量 (靠左窄条)
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .background(Color(0xFFE65100))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "1 Traveller",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 图片区域：星球居中，火箭在星球左24dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.galaxy),
                    contentDescription = "galaxy",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.drawable.rocket_icon),
                    contentDescription = "rocket",
                    modifier = Modifier
                        .size(40.dp)
                        .offset(x = (-96).dp)
                )
            }

            // DEPART 按钮
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                ),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("DEPART", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

// ========== 实验5：Compose 课程学习任务 ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeTaskApp() {
    data class Task(val id: Int, val text: String, val isCompleted: Boolean)

    val tasks = remember {
        mutableStateListOf(
            Task(1, "学习 Column 和 Row", true),
            Task(2, "学习状态管理", false),
            Task(3, "完成 Compose 实验", false)
        )
    }
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 标题
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "课程学习任务",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828)
            )
        }

        // 输入区域
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("请输入学习任务") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        tasks.add(Task(tasks.size + 1, inputText, false))
                        inputText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC62828)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("添加")
            }
        }

        // 统计
        Text(
            text = "已完成: ${tasks.count { it.isCompleted }} / ${tasks.size}",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp,
            color = Color.Gray
        )

        // 任务列表
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(count = tasks.size) { index ->
                val task = tasks[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = task.isCompleted,
                            onCheckedChange = { checked ->
                                tasks[index] = tasks[index].copy(isCompleted = checked)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFC62828)
                            )
                        )
                        Text(
                            text = task.text,
                            fontSize = 15.sp,
                            color = if (task.isCompleted) Color.Gray else Color.Black,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                        )
                    }
                    TextButton(
                        onClick = {
                            tasks.removeAt(index)
                        }
                    ) {
                        Text("删除", color = Color(0xFFC62828), fontSize = 14.sp)
                    }
                }
                HorizontalDivider(color = Color(0xFFEEEEEE))
            }
        }
    }
}

// ========== Preview ==========
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExperimentPreview() {
    MyApplicationTheme {
        ExperimentTabs()
    }
}
