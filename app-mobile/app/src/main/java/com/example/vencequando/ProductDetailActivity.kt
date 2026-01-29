package com.example.vencequando

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDetailActivity : AppCompatActivity() {

    private var productId: Int = -1

    private val editProductLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            finish() // Fecha a tela de detalhes se a edição foi bem-sucedida
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val tvAppBarTitle = findViewById<TextView>(R.id.tvAppBarTitle)
        val tvProductName = findViewById<TextView>(R.id.tvDetailProductName)
        val tvProductCategory = findViewById<TextView>(R.id.tvDetailProductCategory)
        val tvExpirationDate = findViewById<TextView>(R.id.tvDetailExpirationDate)
        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        tvAppBarTitle.text = "Detalhes do Remédio"

        ivBack.setOnClickListener {
            finish() // Fecha a tela e volta para a anterior
        }

        productId = intent.getIntExtra("PRODUCT_ID", -1)
        val productName = intent.getStringExtra("PRODUCT_NAME")
        val productCategory = intent.getStringExtra("PRODUCT_CATEGORY")
        val productExpirationDate = intent.getStringExtra("PRODUCT_EXPIRATION_DATE")

        if (productId != -1) {
            tvProductName.text = productName
            tvProductCategory.text = productCategory
            tvExpirationDate.text = "Data de Validade: ${productExpirationDate}"
        } else {
            Toast.makeText(this, "Erro ao carregar os detalhes do produto.", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditProductActivity::class.java).apply {
                putExtra("PRODUCT_ID", productId)
                putExtra("PRODUCT_NAME", productName)
                putExtra("PRODUCT_CATEGORY", productCategory)
                putExtra("PRODUCT_EXPIRATION_DATE", productExpirationDate)
            }
            editProductLauncher.launch(intent)
        }

        btnDelete.setOnClickListener {
            deleteProduct(productId)
        }
    }

    private fun deleteProduct(id: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        service.deleteProduct(id).enqueue(object : Callback<DeleteProductResponse> {
            override fun onResponse(call: Call<DeleteProductResponse>, response: Response<DeleteProductResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Erro ao excluir produto.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeleteProductResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Falha na comunicação com o servidor.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}