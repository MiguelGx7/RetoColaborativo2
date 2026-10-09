package com.proyecto.retocolaborativo2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import model.LoginRequest
import model.RetrofitClient

class MainActivity : AppCompatActivity() {

    private var token: String? = null   // aquí guardaremos la "manilla"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etClave = findViewById<EditText>(R.id.etClave)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            hacerLogin(etUsuario.text.toString().trim(), etClave.text.toString())
        }

        // ¿ya hay sesión guardada? entonces pedimos directo los datos
        token = getSharedPreferences("sesion", MODE_PRIVATE).getString("token", null)
        if (token != null) {
            obtenerUsuario()
        }
    }

    // Oculta los campos y el botón de login
    private fun ocultarFormulario() {
        findViewById<EditText>(R.id.etUsuario).visibility = View.GONE
        findViewById<EditText>(R.id.etClave).visibility = View.GONE
        findViewById<Button>(R.id.btnLogin).visibility = View.GONE
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
                    token = resp.body()?.accessToken   // ← guardamos el token
                    // lo guardamos también en el teléfono para recordar la sesión
                    getSharedPreferences("sesion", MODE_PRIVATE)
                        .edit()
                        .putString("token", token)
                        .apply()
                    Log.d("API", "Token recibido: $token")
                    obtenerUsuario()                  // seguimos al GET
                } else {
                    Log.e("API", "Login falló: ${resp.code()}")
                    val mensaje = "Login falló: usuario o contraseña incorrectos"
                    findViewById<TextView>(R.id.tvResultado).text = mensaje
                    Toast.makeText(this@MainActivity, mensaje, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("API", "Error de red: ${e.message}")
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------- PASO B: GET protegido con el token ----------
    private fun obtenerUsuario() {
        val t = token ?: return              // si no hay token, no seguimos
        lifecycleScope.launch {
            try {
                // ojo: el formato es "Bearer " + token
                val resp = RetrofitClient.api.getCurrentUser("Bearer $t")
                if (resp.isSuccessful) {
                    val user = resp.body()
                    Log.d("API", "Hola ${user?.firstName} - ${user?.email}")
                    findViewById<TextView>(R.id.tvResultado).text =
                        "Hola ${user?.firstName}\n${user?.email}"
                    ocultarFormulario()
                }
                else {
                    Log.e("API", "Consulta falló: ${resp.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error: ${e.message}")
            }
        }
    }
}