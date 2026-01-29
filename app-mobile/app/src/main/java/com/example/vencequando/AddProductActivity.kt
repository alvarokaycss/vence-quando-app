package com.example.vencequando

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val tvAppBarTitle = findViewById<TextView>(R.id.tvAppBarTitle)
        val etProductName = findViewById<EditText>(R.id.etProductName)
        val etProductCategory = findViewById<EditText>(R.id.etProductCategory)
        val etExpirationDate = findViewById<EditText>(R.id.etExpirationDate)
        val btnSave = findViewById<Button>(R.id.btnSave)

        tvAppBarTitle.text = "Adicionar Remédio"

        ivBack.setOnClickListener {
            finish() // Fecha a tela e volta para a anterior
        }

        btnSave.setOnClickListener {
            val name = etProductName.text.toString()
            val category = etProductCategory.text.toString()
            val expirationDate = etExpirationDate.text.toString()

            if (name.isNotEmpty() && expirationDate.isNotEmpty()) {
                addProduct(name, category, expirationDate)
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addProduct(name: String, category: String, expirationDate: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        val request = AddProductRequest(name, category, expirationDate)

        service.addProduct(request).enqueue(object : Callback<AddProductResponse> {
            override fun onResponse(call: Call<AddProductResponse>, response: Response<AddProductResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Produto adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Erro ao adicionar produto.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Falha na comunicação com o servidor.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}