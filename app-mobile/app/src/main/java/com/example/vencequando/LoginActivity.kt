package com.example.vencequando

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString();
            val password = etPassword.text.toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this, "Preencha todos os campos", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Configuração do Retrofit (Client HTTP)
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()

            val api = retrofit.create(ApiService::class.java)

            val request = LoginRequest(email, password)

            api.login(request).enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(
                    call: retrofit2.Call<LoginResponse>,
                    response: retrofit2.Response<LoginResponse>
                ){
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        Toast.makeText(applicationContext, "Sucesso: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()

                        val intent = android.content.Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Falha no Login (Senha Incorreta)", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable){
                    Toast.makeText(applicationContext, "Falha no Login", Toast.LENGTH_SHORT).show()

                }
            })
        }
    }
}