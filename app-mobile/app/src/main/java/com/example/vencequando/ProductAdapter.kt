package com.example.vencequando

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

// Classe ProductAdapter (Documentação Kotlin)
class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvDate: TextView = itemView.findViewById(R.id.tvExpirationDate)
        val viewStatus: ImageView = itemView.findViewById(R.id.viewStatusColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.tvName.text = product.name
        holder.tvDate.text = "Vence em: ${product.expirationDate}"

        when (product.status) {
            "VENCIDO" -> holder.viewStatus.setColorFilter(Color.RED)   // Já venceu
            "URGENTE" -> holder.viewStatus.setColorFilter(Color.RED)   // Vence em até 3 dias
            "ATENCAO" -> holder.viewStatus.setColorFilter(Color.YELLOW)// Vence em até 7 dias
            else -> holder.viewStatus.setColorFilter(Color.parseColor("#4CAF50")) // Verde
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}