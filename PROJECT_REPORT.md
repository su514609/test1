# Android 移动软件开发实验 3 —— 组件实验报告

## 一、项目概述

| 项目 | 说明 |
|------|------|
| 项目名称 | MyApplication（移动软件开发实验3） |
| 开发环境 | Android Studio + JDK 17 |
| 构建工具 | Gradle 8.x |
| 最低 SDK | API 24（Android 7.0） |
| 目标 SDK | API 34（Android 14） |
| 开发语言 | Kotlin + XML 布局 |
| 架构 | 传统 View + AppCompatActivity + Fragment |
| 主题 | Theme.AppCompat.Light.DarkActionBar |

## 二、实验内容

本次实验包含 **5 个 UI 组件实验**，通过 **TabLayout + ViewPager2** 在单个 Activity 中切换展示：

| 序号 | Tab 名称 | 组件类型 | 说明 |
|------|----------|----------|------|
| 1 | ListView | 列表视图 | 6 只动物列表，点选变深红背景 |
| 2 | AlertDialog | 对话框 | 自定义登录对话框（用户名 + 密码） |
| 3 | MenuTest | XML 菜单 | 右上角 3 点菜单（字体大小 + 字体颜色） |
| 4 | ActionMode | 上下文菜单 | 点击进入选择模式，顶部浮栏（勾 + 标题 + 垃圾桶） |
| 5 | RecyclerView | 列表视图 | 标准 Adapter 实现，点击 Toast |

## 三、核心架构

### 3.1 Activity 结构

```
Experiment3Activity (AppCompatActivity)
├── TabLayout                          // 顶部 5 个 Tab
└── ViewPager2
    ├── Fragment 1: ListViewFragment
    ├── Fragment 2: AlertDialogFragment
    ├── Fragment 3: MenuTestFragment
    ├── Fragment 4: ActionModeFragment
    └── Fragment 5: RecyclerViewFragment
```

### 3.2 主题配置（关键）

AndroidManifest.xml 中为 Experiment3Activity 单独指定主题：

```xml
<activity
    android:name=".Experiment3Activity"
    android:theme="@style/Theme.MyApplication.WithActionBar" />
```

themes.xml 定义：

```xml
<style name="Theme.MyApplication.WithActionBar" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="android:windowBackground">@android:color/white</item>
    <item name="android:textColorPrimary">#000000</item>
</style>
```

> **注意**：AppCompatActivity 必须使用 Theme.AppCompat 后代主题，使用 MaterialComponents 会导致崩溃。

## 四、各实验实现详情

### 4.1 ListView（实验 1）

**布局**：`activity_listview.xml` — ListView + 底部按钮

**列表项**：`item_animal.xml` — ImageView（动物图） + TextView（名称）

**交互逻辑**：
- 点击列表项 → 选中行背景变深红 `#8E0000` + 白字
- 再次点击 → 取消选中，恢复白底黑字
- 底部按钮紧贴 ListView

### 4.2 AlertDialog（实验 2）

**对话框布局**：`dialog_login.xml` — EditText(用户名) + EditText(密码) + Button(取消) + Button(登录)

**交互逻辑**：
- 点击按钮 → `AlertDialog.Builder` inflate 自定义 View
- 登录按钮 → Toast 提示登录信息
- 取消按钮 → dismiss

### 4.3 MenuTest（实验 3）

**菜单资源**：`menu/menu_main.xml`

```
3 点菜单
├── 字体大小（子菜单）
│   ├── 小（10sp）
│   ├── 中（16sp）
│   └── 大（20sp）
├── 普通菜单项
└── 字体颜色（子菜单）
    ├── 红
    └── 黑
```

**交互逻辑**：
- `onCreateOptionsMenu` 加载菜单
- `onOptionsItemSelected` 处理点击 → 修改居中 TextView 的字号/颜色

### 4.4 ActionMode（实验 4）⭐

**实现方式**：**自定义 FrameLayout 浮层**（而非系统 ActionMode）

> 原因：系统 `startActionMode()` 会强制在 customView 前插入返回箭头，无法隐藏。

**布局**：`activity_action_mode.xml`

```xml
<FrameLayout>
    <ListView />                         <!-- 底层列表 -->
    <LinearLayout id="custom_actionmode_bar">  <!-- 顶部浮层，默认 gone -->
        <ImageView iv_check     />        <!-- ✅ 勾图标（白色） -->
        <TextView tv_actionmode_title />  <!-- "N selected" -->
        <ImageView iv_delete    />        <!-- 🗑️ 垃圾桶（仅图标） -->
    </LinearLayout>
</FrameLayout>
```

**交互逻辑**：

| 操作 | 效果 |
|------|------|
| 点击列表项 | 选中/取消切换，选中项浅蓝背景 `#81D4FA` |
| 首次选中 | 顶部深蓝灰浮栏 `#263238` 出现 |
| 点击 ✅ 勾 | 删除所有选中项（Toast 提示），浮栏保持打开 |
| 点击 🗑️ 垃圾桶 | 无功能（仅图标显示） |
| 全部取消/删光 | 浮栏自动隐藏 |

### 4.5 RecyclerView（实验 5）

**Adapter**：`AnimalAdapter`

**列表项**：`item_animal.xml` 复用

**交互逻辑**：点击 item → Toast 显示动物名称

## 五、资源文件清单

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/myapplication/
│   ├── Experiment3Activity.kt         // TabLayout + ViewPager2 主容器
│   ├── ComponentExperiments.kt         // 5 个 Fragment 实现
│   └── MainActivity.kt                // Compose 入口（备用）
└── res/
    ├── values/themes.xml               // 主题定义
    ├── layout/
    │   ├── activity_experiment3.xml    // TabLayout + ViewPager2
    │   ├── activity_listview.xml       // 实验 1
    │   ├── item_animal.xml             // 动物列表项
    │   ├── dialog_login.xml            // 实验 2 对话框
    │   ├── activity_menu_test.xml      // 实验 3
    │   ├── activity_action_mode.xml    // 实验 4
    │   └── activity_recycler_view.xml  // 实验 5
    ├── menu/menu_main.xml              // 实验 3 菜单
    └── drawable/
        ├── ic_robot.xml                // 绿色机器人
        ├── ic_action_check_white.xml   // 白色勾
        ├── ic_action_delete_white.xml  // 白色垃圾桶
        ├── ic_action_back_white.xml    // 白色返回箭头
        └── lion.jpg / tiger.jpg / ...  // 动物图片
```

## 六、关键踩坑记录

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| 模拟器黑屏 | MaterialComponents 主题 + AppCompatActivity 不兼容 | 全局换 Theme.AppCompat |
| ActionBar 崩溃 | Theme.MaterialComponents 不是 AppCompat 后代 | Manifest 单独指定 DarkActionBar |
| ListView 选中背景无效 | selector_list_item.xml 覆盖了 setBackgroundColor | 删掉 selector，直接 setBackgroundColor |
| 系统 ActionMode 有返回箭头 | 系统强制插入，无法隐藏 | 改用 FrameLayout 自定义浮层 |
| Windows 中文路径 | Gradle 路径检查失败 | `android.overridePathCheck=true` |

## 七、构建与运行

```powershell
# 构建 Debug APK
.\gradlew.bat assembleDebug

# 安装到模拟器
adb install -r app\build\outputs\apk\debug\app-debug.apk

# 启动实验 3
adb shell am start -n com.example.myapplication/.Experiment3Activity
```

## 八、版本历史

| 日期 | 说明 |
|------|------|
| 2026-10-25 | 完成全部 5 个组件实验，ActionMode 改为自定义浮层方案 |
