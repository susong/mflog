package com.mf.log.demo

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mf.log.LogUtils
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_log_v).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_d).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_i).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_w).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_e).setOnClickListener(this)
        LogUtils.init("/mf/log/android/", true, false, true)
        requestPermissions()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_log_v -> LogUtils.v("this is verbose msg")
            R.id.btn_log_i -> LogUtils.i("this is info msg")
            R.id.btn_log_w -> LogUtils.w("this is warn msg")
            R.id.btn_log_e -> LogUtils.e("this is error msg", NullPointerException())
            else -> LogUtils.d("this is debug msg")
        }
    }

    private fun requestPermissions() {
        val permissionList = mutableListOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissionList.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
        PermissionX.init(this)
            .permissions(permissionList)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                if (beforeRequest) {
                    scope.showRequestReasonDialog(
                        deniedList,
                        "即将申请的权限是程序必须依赖的权限", "我已明白"
                    )
                } else {
                    scope.showRequestReasonDialog(
                        deniedList,
                        "即将重新申请的权限是程序必须依赖的权限", "我已明白"
                    )
                }
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "您需要去应用程序设置当中手动开启权限", "去设置"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                LogUtils.d(
                    "allGranted:$allGranted grantedList:$grantedList " +
                            "deniedList:$deniedList"
                )
                if (allGranted) {
                    onAllPermissionsGranted()
                }
            }
    }

    private fun onAllPermissionsGranted() {
    }
}