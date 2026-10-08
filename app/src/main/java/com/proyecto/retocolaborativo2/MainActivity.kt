package com.proyecto.retocolaborativo2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import model.LoginRequest
import model.RetrofitClient

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hacerLogin("emilys", "emilyspass")
    }

    // ---------- PASO A: POST de login ----------
    private fun hacerLogin(usuario: String, clave: String) {
        // lifecycleScope.launch = ejecuta en una corrutina (sin congelar la app)
        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.api.login(
                    LoginRequest(usuario, clave)
                )
                if (resp.isSuccessful) {
                    Log.d("API", "Token recibido: ${resp.body()?.accessToken}")
                } else {
                    Log.e("API", "Login falló: ${resp.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error de red: ${e.message}")
            }
        }
    }
}