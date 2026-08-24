package io.nekohasekai.sagernet.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.nekohasekai.sagernet.R
import io.nekohasekai.sagernet.bg.BaseService
import io.nekohasekai.sagernet.bg.VpnService
import io.nekohasekai.sagernet.database.DataStore
import io.nekohasekai.sagernet.fmt.internal.ProxyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WearOneUiActivity : AppCompatActivity() {

    private lateinit var btnToggleVpn: LinearLayout
    private lateinit var tvStatusTitle: TextView
    private lateinit var tvStatusSubtitle: TextView
    private lateinit var btnSelectNode: LinearLayout
    private lateinit var tvCurrentNode: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wear_oneui)

        // 1. 绑定 UI 控件
        btnToggleVpn = findViewById(R.id.btn_toggle_vpn)
        tvStatusTitle = findViewById(R.id.tv_status_title)
        tvStatusSubtitle = findViewById(R.id.tv_status_subtitle)
        btnSelectNode = findViewById(R.id.btn_select_node)
        tvCurrentNode = findViewById(R.id.tv_current_node)

        // 2. 监听代理开关点击事件，调用原版底层方法
        btnToggleVpn.setOnClickListener {
            if (BaseService.ExpectedState == BaseService.State.Connected) {
                // 如果当前是连接状态，就停止代理
                VpnService.stopVpn()
            } else {
                // 如果当前是断开状态，就启动代理
                VpnService.startVpn(this)
            }
            updateUiState()
        }

        // 3. 监听节点选择按钮点击，弹出原版节点列表弹窗
        btnSelectNode.setOnClickListener {
            showNodeSelectionDialog()
        }
        
        // 初始加载一次状态和节点名称
        updateUiState()
        updateCurrentNodeName()
    }

    // 每次回到界面时，刷新状态（防止在后台被杀）
    override fun onResume() {
        super.onResume()
        updateUiState()
        updateCurrentNodeName()
    }

    // 根据真实的底层服务状态，改变按钮颜色和文字
    private fun updateUiState() {
        if (BaseService.ExpectedState == BaseService.State.Connected) {
            tvStatusTitle.text = "已连接"
            tvStatusSubtitle.text = "安全代理中"
            btnToggleVpn.setBackgroundResource(R.drawable.bg_wear_pill_connected)
        } else {
            tvStatusTitle.text = "已断开"
            tvStatusSubtitle.text = "点击启动"
            btnToggleVpn.setBackgroundResource(R.drawable.bg_wear_pill_disconnected)
        }
    }

    // 从数据库异步读取当前选中的节点名称
    private fun updateCurrentNodeName() {
        lifecycleScope.launch(Dispatchers.IO) {
            val currentProfileId = DataStore.profileId
            val currentProfile = DataStore.getProfileById(currentProfileId)
            withContext(Dispatchers.Main) {
                tvCurrentNode.text = currentProfile?.name ?: "未选择节点"
            }
        }
    }

    // 弹出一个简单的节点选择列表
    private fun showNodeSelectionDialog() {
        lifecycleScope.launch(Dispatchers.IO) {
            val profiles = DataStore.allProfiles
            val names = profiles.map { it.name }.toTypedArray()
            
            withContext(Dispatchers.Main) {
                androidx.appcompat.app.AlertDialog.Builder(this@WearOneUiActivity)
                    .setTitle("选择节点")
                    .setItems(names) { _, which ->
                        val selectedProfile = profiles[which]
                        DataStore.profileId = selectedProfile.id
                        updateCurrentNodeName()
                        // 如果正在连接，切换节点后自动重启服务
                        if (BaseService.ExpectedState == BaseService.State.Connected) {
                            VpnService.reloadVpn()
                        }
                    }
                    .show()
            }
        }
    }
}
