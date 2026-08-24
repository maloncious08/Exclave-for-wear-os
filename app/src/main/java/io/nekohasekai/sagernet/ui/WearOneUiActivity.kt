package io.nekohasekai.sagernet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.nekohasekai.sagernet.R

class WearOneUiActivity : AppCompatActivity() {

    // 用于记录代理的开关状态
    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 绑定 Day 1 创建的圆屏布局
        setContentView(R.layout.activity_wear_oneui)

        val btnToggleVpn = findViewById<LinearLayout>(R.id.btn_toggle_vpn)
        val tvStatusTitle = findViewById<TextView>(R.id.tv_status_title)
        val tvStatusSubtitle = findViewById<TextView>(R.id.tv_status_subtitle)
        val btnSelectNode = findViewById<LinearLayout>(R.id.btn_select_node)
        val btnUpdateSub = findViewById<TextView>(R.id.btn_update_sub)

        // 1. 核心大开关点击事件
        btnToggleVpn.setOnClickListener {
            isConnected = !isConnected
            if (isConnected) {
                // UI 切换为蓝色开启状态
                tvStatusTitle.text = "已连接代理"
                tvStatusSubtitle.text = "运行良好"
                btnToggleVpn.setBackgroundResource(R.drawable.bg_wear_pill_connected)
                // 备注: 后续接入原版 startVpn(this) 方法
            } else {
                // UI 切换为暗色关闭状态
                tvStatusTitle.text = "已断开连接"
                tvStatusSubtitle.text = "点击启动代理"
                btnToggleVpn.setBackgroundResource(R.drawable.bg_wear_pill_disconnected)
                // 备注: 后续接入原版 stopVpn() 方法
            }
        }

        // 2. 节点选择按钮点击事件
        btnSelectNode.setOnClickListener {
            // Day 3 我们将在这里接入手表专属底部分组弹窗
            // 临时测试：长按或点击暂时跳转回原版 MainActivity (按需测试)
            startActivity(Intent(this, MainActivity::class.java))
        }

        // 3. 更新订阅按钮点击事件
        btnUpdateSub.setOnClickListener {
            tvStatusSubtitle.text = "正在更新订阅..."
        }
    }
}
