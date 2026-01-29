package com.example.vencequando

import android.app.Activity
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

class EditProductActivity : AppCompatActivity() {

    private var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val tvAppBarTitle = findViewById<TextView>(R.id.tvAppBarTitle)
        val etProductName = findViewById<EditText>(R.id.etProductName)
        val etProductCategory = findViewById<EditText>(R.id.etProductCategory)
        val etExpirationDate = findViewById<EditText>(R.id.etExpirationDate)
        val btnSave = findViewById<Button>(R.id.btnSave)

        tvAppBarTitle.text = "Editar Produto"

        ivBack.setOnClickListener {
            finish() // Fecha a tela e volta para a anterior
        }

        productId = intent.getIntExtra("PRODUCT_ID", -1)
        val productName = intent.getStringExtra("PRODUCT_NAME")
        val productCategory = intent.getStringExtra("PRODUCT_CATEGORY")
        val productExpirationDate = intent.getStringExtra("PRODUCT_EXPIRATION_DATE")

        if (productId != -1) {
            etProductName.setText(productName)
            etProductCategory.setText(productCategory)
            etExpirationDate.setText(productExpirationDate)
        } else {
            Toast.makeText(this, "Erro ao carregar os dados do produto.", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnSave.setOnClickListener {
            val name = etProductName.text.toString()
            val category = etProductCategory.text.toString()
            val expirationDate = etExpirationDate.text.toString()

            if (name.isNotEmpty() && expirationDate.isNotEmpty()) {
                updateProduct(productId, name, category, expirationDate)
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProduct(id: Int, name: String, category: String, expirationDate: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        val request = UpdateProductRequest(name, category, expirationDate)

        service.updateProduct(id, request).enqueue(object : Callback<UpdateProductResponse> {
            override fun onResponse(call: Call<UpdateProductResponse>, response: Response<UpdateProductResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Erro ao atualizar produto.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateProductResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Falha na comunicação com o servidor.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}