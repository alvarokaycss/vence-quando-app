package com.example.vencequando

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvProducts = findViewById(R.id.rvProducts)
        fabAdd = findViewById(R.id.fabAdd)                  // Botão de Adicionar
        val btnExit = findViewById<ImageView>(R.id.btnExit) // Botão de Signout

        rvProducts.layoutManager = LinearLayoutManager(this)

        fabAdd.setOnClickListener {
            Toast.makeText(this, "Adicionar novo produto", Toast.LENGTH_SHORT).show()
        }

        btnExit.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarProdutosDoServidor()
    }

    private fun carregarProdutosDoServidor() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        service.getProducts().enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                if (response.isSuccessful) {
                    val listaReal = response.body()?.products

                    if (listaReal != null) {
                        rvProducts.adapter = ProductAdapter(listaReal)
                    }
                } else {
                    Toast.makeText(applicationContext, "Erro ao carregar lista", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Sem conexão com servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
