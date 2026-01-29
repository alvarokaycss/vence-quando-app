package com.example.vencequando

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

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
            "VENCIDO" -> holder.viewStatus.setColorFilter(Color.RED)
            "URGENTE" -> holder.viewStatus.setColorFilter(Color.RED)
            "ATENCAO" -> holder.viewStatus.setColorFilter(Color.YELLOW)
            else -> holder.viewStatus.setColorFilter(Color.parseColor("#4CAF50"))
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id)
                putExtra("PRODUCT_NAME", product.name)
                putExtra("PRODUCT_CATEGORY", product.category)
                putExtra("PRODUCT_EXPIRATION_DATE", product.expirationDate)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}