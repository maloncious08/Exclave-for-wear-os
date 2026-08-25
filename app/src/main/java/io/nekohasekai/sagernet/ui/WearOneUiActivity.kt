package io.nekohasekai.sagernet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.nekohasekai.sagernet.R
import io.nekohasekai.sagernet.SagerNet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class WearOneUiActivity : AppCompatActivity() {

    private var isConnected = false
    private lateinit var btnToggleVpn: LinearLayout
    private lateinit var tvStatusTitle: TextView
    private lateinit var tvStatusSubtitle: TextView
    private lateinit var btnSelectNode: LinearLayout
    private lateinit var tvCurrentNode: TextView
    private lateinit var btnPingTest: TextView
    private lateinit var btnOpenSettings: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wear_oneui)

        btnToggleVpn = findViewById(R.id.btn_toggle_vpn)
        tvStatusTitle = findViewById(R.id.tv_status_title)
        tvStatusSubtitle = findViewById(R.id.tv_status_subtitle)
        btnSelectNode = findViewById(R.id.btn_select_node)
        tvCurrentNode = findViewById(R.id.tv_current_node)
        btnPingTest = findViewById(R.id.btn_ping_test)
        btnOpenSettings = findViewById(R.id.btn_open_settings)

        tvCurrentNode.text = "点击切换节点"

        // 1. 核心代理开关
        btnToggleVpn.setOnClickListener {
            isConnected = !isConnected
            if (isConnected) {
                tvStatusTitle.text = "已连接"
                tvStatusSubtitle.text = "安全代理中"
                btnToggleVpn.setBackgroundResource(R.drawable.bg_wear_pill_connected)
                runCatching { SagerNet.startService() }
            } else {
                tvStatusTitle.text = "已断开"
                tvStatusSubtitle.text = "点击启动代理"
                btnToggleVpn.setBackgroundResource(R.drawable.bg_wear_pill_disconnected)
                runCatching { SagerNet.stopService() }
            }
        }

        // 2. 选择节点：拉起内置节点选择器
        btnSelectNode.setOnClickListener {
            runCatching {
                startActivity(Intent(this, ProfileSelectActivity::class.java))
            }
        }

        // 3. 真实网络延迟测试 (Google 204)
        btnPingTest.setOnClickListener {
            runLatencyTest()
        }

        // 4. 打开原版完整设置
        btnOpenSettings.setOnClickListener {
            runCatching {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun runLatencyTest() {
        btnPingTest.text = "测速中..."
        lifecycleScope.launch(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            val isSuccess = runCatching {
                val url = URL("http://www.gstatic.com/generate_204")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 3500
                    readTimeout = 3500
                    instanceFollowRedirects = false
                    useCaches = false
                }
                conn.connect()
                val code = conn.responseCode
                conn.disconnect()
                code in 200..299
            }.getOrDefault(false)

            val elapsed = System.currentTimeMillis() - startTime
            withContext(Dispatchers.Main) {
                if (isSuccess) {
                    btnPingTest.text = "${elapsed}ms"
                } else {
                    btnPingTest.text = "超时"
                }
            }
        }
    }
}
