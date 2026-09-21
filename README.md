# Android 布局实验

> 福建师范大学 · 移动软件开发课程实验作业

## 📋 项目简介

本项目是一个基于 **Android Jetpack Compose** 的布局实验应用，通过顶部 Tab 切换实现了 **5 个不同的 UI 界面**，涵盖了 Android 开发中主要的布局方式：线性布局、表格布局、约束布局 以及 Compose 声明式 UI。

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Kotlin | - | 主要开发语言 |
| Jetpack Compose | BOM | 声明式 UI 框架 |
| Material 3 | - | Material Design 组件库 |
| Gradle | 9.5.0 | 构建工具 |
| compileSdk | 37 | Android SDK 编译版本 |
| minSdk | 24 | 最低支持 Android 7.0 |

## 📂 项目结构

```
移动软件开发/
├── app/
│   └── src/main/
│       ├── java/com/example/myapplication/
│       │   ├── MainActivity.kt          # 主入口，包含所有实验界面
│       │   └── ui/theme/                # 主题配置
│       ├── res/
│       │   ├── drawable/                # 图片资源（6张）
│       │   └── values/                  # 字符串、颜色、主题
│       └── AndroidManifest.xml
├── 实验2图片/                            # 原始图片资源目录
├── build.gradle.kts                     # 根构建配置
└── settings.gradle.kts
```

## 🧪 实验内容

### 实验 1：线性布局（LinearLayout）

- **实现方式**：Compose `Row` + `Column`
- **效果**：4×4 网格，格子宽度由文字自适应（`wrapContentWidth()`），黑色背景 + 深灰边框，文字居中
- **关键点**：`Arrangement.spacedBy(0.dp)` 实现紧贴无边距

### 实验 2：表格布局（TableLayout）

- **实现方式**：Compose `Column` + `Row` 组合模拟表格行
- **效果**：暗色菜单列表，标题栏 "Hello TableLayout"，包含 Open/Save/Save As/Import/Export/Quit，Import 和 Export 前有红色 X 标记，快捷键右对齐
- **关键点**：使用 `Triple(label, shortcut, hasX)` 数据结构管理菜单项

### 实验 3：约束布局 1 — 计算器

- **实现方式**：Compose `Column` + `Row` 模拟 ConstraintLayout
- **效果**：绿色标题栏、浅黄色显示屏、4×4 数字/运算符按钮网格
- **关键点**：`weight(1f)` 均分按钮宽度，`mutableStateOf` 管理显示状态

### 实验 4：约束布局 2 — 太空旅行

- **实现方式**：Compose `Box` 叠加 + `Row` 组合
- **效果**：
  - 顶部图标导航栏（Space Stations / Flights / Rovers）
  - DCA / MARS 绿色板块 + 白色底框双箭头（重叠效果）
  - 橙色 One Way 开关条 + 1 Traveller 旅客条
  - 星球居中 + 火箭偏左（`offset(x = -96.dp)`）
  - 绿色 DEPART 出发按钮
- **图片资源**：`galaxy.png`（星球）、`rocket_icon.png`（火箭）、`double_arrows.png`（双向箭头）、`space_station_icon.png`、`rover_icon.png`

### 实验 5：Android Compose — 课程学习任务管理

- **实现方式**：`LazyColumn` + `mutableStateListOf` + Checkbox 状态联动
- **效果**：
  - 红色标题 "课程学习任务"
  - 输入框 + 添加按钮（红色）
  - 实时统计："已完成: X / Y"
  - 任务列表：勾选完成后文字变灰 + 划删除线（`TextDecoration.LineThrough`）
  - 右侧删除按钮
- **关键点**：使用 `items(count = tasks.size)` 索引访问，确保状态正确追踪

## 🖼️ 图片资源

| 资源文件 | 用途 |
|----------|------|
| `galaxy.png` | 星球/行星图案 |
| `rocket_icon.png` | 火箭图标 |
| `double_arrows.png` | 双向交换箭头 |
| `single_arrow.png` | 单向箭头 |
| `space_station_icon.png` | 空间站图标 |
| `rover_icon.png` | 火星车图标 |

## ▶️ 运行方式

### 前置条件
- Android Studio 最新版本
- JDK 11+
- Android SDK（compileSdk 37）

### 步骤

1. **克隆仓库**
   ```bash
   git clone https://github.com/su514609/test1.git
   ```

2. **用 Android Studio 打开**
   - File → Open → 选择项目根目录

3. **等待 Gradle Sync 完成**
   - 首次同步会自动下载依赖

4. **运行到模拟器或真机**
   - 点击工具栏 ▶️ Run 按钮
   - 或 `Shift + F10`

## ⚠️ 注意事项

项目路径包含中文字符，已在 `gradle.properties` 中添加：
```properties
android.overridePathCheck=true
```
避免 Gradle 在 Windows 平台上因非 ASCII 路径报错。

## 📝 开发过程记录

| 问题 | 解决方案 |
|------|----------|
| LinearLayout 4×4 格子文字换行截断 | 使用 `wrapContentWidth()` 替代 `weight(1f)`，添加 `maxLines = 1` |
| 太空旅行双箭头与绿块不重叠 | 使用 `Box` 叠加两层，箭头层覆盖在绿块层上方 |
| Compose 任务添加不刷新 | `items(tasks.toList())` 创建新列表破坏状态 → 改为 `items(count = tasks.size)` |
| 勾选任务无删除线 | 添加 `textDecoration = TextDecoration.LineThrough` |
| Switch 默认状态不对 | `mutableStateOf(true)` → `mutableStateOf(false)` |
| 中文路径构建失败 | `gradle.properties` 添加 `android.overridePathCheck=true` |

## 📄 许可证

MIT License
