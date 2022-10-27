package com.godminq.notingtest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.godminq.noting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.locationIcon.setOnClickListener {
            if (isAllPermissionsGranted()) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
//                requestDangerousPermissions()
                requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
            }
        }
    }
    private fun isAllPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,  // 카메라 권한
            Manifest.permission.READ_EXTERNAL_STORAGE,  // 저장소 권한
        )
        private const val REQUEST_CODE_PERMISSIONS = 1001
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { permission ->
                when {
                    permission.value -> {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    }
                    shouldShowRequestPermissionRationale(permission.key) -> {
                        Toast.makeText(this, "Permission required to use app!", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                        openSettings()
                    }
                }
            }
        }

    private fun openSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", packageName, null)
        }.run(::startActivity)
    }
}