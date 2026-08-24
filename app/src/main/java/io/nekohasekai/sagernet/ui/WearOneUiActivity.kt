package io.nekohasekai.sagernet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.nekohasekai.sagernet.R
import io.nekohasekai.sagernet.SagerNet

class WearOneUiActivity : AppCompatActivity() {

    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wear_oneui)

        val btnToggleVpn = findViewById<LinearLayout>(R.id.btn_toggle_vpn)
        val tvStatusTitle = findViewById<TextView>(R.id.tv_status_title)
        val tvStatusSubtitle = findViewById<TextView>(R.id.tv_status_subtitle)
        val btnSelectNode = findViewById<LinearLayout>(R.id.btn_select_node)
        val tvCurrentNode = findViewById<TextView>(R.id.tv_current_node)
        val btnUpdateSub = findViewById<TextView>(R.id.btn_update_sub)

        // 1. 核心开关：直接调用 SagerNet 底层标准服务
        btnToggleVpn.setOnClickListener {
            isConnected = !isConnected
            if (isConnected) {
                tvStatusTitle.text = "已连接"
                tvStatusSubtitle.text = "安全代理中"
                btnToggleVpn.setBackgroundResource(R.drawable.bg_wear_pill_connected)
                runCatching { SagerNet.startService() }
            } else {
                tvStatusTitle.text = "已断开"
                tvStatusSubtitle.text = "点击启动"
                btnToggleVpn.setBackgroundResource(R.drawable.bg_wear_pill_disconnected)
                runCatching { SagerNet.stopService() }
            }
        }

        // 2. 节点选择：直接拉起应用内置的节点选择页面
        btnSelectNode.setOnClickListener {
            runCatching {
                startActivity(Intent(this, ProfileSelectActivity::class.java))
            }
        }

        // 3. 更新订阅 / 详细设置：拉起主管理页面
        btnUpdateSub?.setOnClickListener {
            runCatching {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
