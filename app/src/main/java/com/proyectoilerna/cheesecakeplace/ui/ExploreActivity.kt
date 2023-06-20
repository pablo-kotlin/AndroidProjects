package com.proyectoilerna.cheesecakeplace.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.proyectoilerna.cheesecakeplace.R
import com.proyectoilerna.cheesecakeplace.databinding.ActivityExploreBinding
import com.proyectoilerna.cheesecakeplace.databinding.ItemRestaurantBinding
import com.proyectoilerna.cheesecakeplace.entities.Restaurant

class ExploreActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityExploreBinding

    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Restaurant, RestaurantHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Recuperamos del fragmento Search el valor de "city"
        val bundle: Bundle? = intent.extras
        val city: String? = bundle?.getString("city")
        val title = "LISTADO DE RESTAURANTES EN $city"
        mBinding.exploreTitle.text = title

        // Construimos una consulta en el nodo "Restaurants" en base a la ciudad predeterminada
        val query = city?.let {
            FirebaseDatabase
                .getInstance("https://cheesecake-place-default-rtdb.europe-west1.firebasedatabase.app")
                .reference.child("Cakes").child("Restaurants")
                    // Ordenamos el resultado de la búsqueda, limitado a 15 resultados
                    // NOTA: Firebase solo permite ordenar de menor a mayor
                    // El rating está almacenado para que la BD lo ordene de mayor a menor
                .child(it).orderByChild("rating").limitToLast(15)
        }

        // Construimos la Vista Recycler con el item Restaurant en base a nuestra consulta
        val options = FirebaseRecyclerOptions.Builder<Restaurant>()
            .setQuery(query!!, Restaurant::class.java).build()

        // Usamos los métodos de Firebase para el Adapter del RecyclerView
        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Restaurant, RestaurantHolder>(options) {

            private lateinit var mContext: Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHolder {

                mContext = parent.context
                val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_restaurant, parent, false)
                return RestaurantHolder(view)
            }

            override fun onBindViewHolder(holder: RestaurantHolder, position: Int, model: Restaurant) {
                val restaurant = getItem(position)

                with(holder) {

                    setListener(restaurant)

                    binding.restName.text = restaurant.name
                    binding.restCity.text = restaurant.city
                    binding.restPostalCode.text = restaurant.postalCode
                        val rating: Float = 5 - restaurant.rating
                    binding.restRatingStars.rating = rating
                    binding.restRatingNumber.text = rating.toString()
                        val votes: String = "Nº votos: " + restaurant.votes.toString()
                    binding.restNumVotes.text = votes
                }
            }

            // La vista actualizará los datos con la información en tiempo real de la BD
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChanged() {
                super.onDataChanged()
                notifyDataSetChanged()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
            }
        }

        mLayoutManager = LinearLayoutManager(this)

        mBinding.recyclerViewMain.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    // Dentro de la clase Restaurant del ViewHolder, manejamos los eventos
    inner class RestaurantHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemRestaurantBinding.bind(view)
        internal fun setListener(restaurant: Restaurant) {
            // El usuario pulsa sobre la tarjeta de un restaurante
            binding.restCardView.setOnClickListener {
                // Cuadro de diálogo para confirmar Intent
                MaterialAlertDialogBuilder(this@ExploreActivity)
                    .setTitle(R.string.website_access)
                    .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                        if (restaurant.urlWebsite.isEmpty()) {
                            // No hay datos del sitio Web
                            Toast.makeText(this@ExploreActivity,
                                R.string.error_website, Toast.LENGTH_SHORT).show()
                        } else {
                            // Consta el sitio Web -> Intent para abrir la página
                            val websiteIntent = Intent().apply {
                                action = Intent.ACTION_VIEW
                                data = Uri.parse(restaurant.urlWebsite)
                            }
                            startActivity(websiteIntent)
                        }
                    }.setNegativeButton(R.string.dialog_delete_cancel, null)
                    .show()
            }
        }
    }
}