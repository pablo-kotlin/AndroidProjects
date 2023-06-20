package com.proyectoilerna.cheesecakeplace.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.proyectoilerna.cheesecakeplace.R
import com.proyectoilerna.cheesecakeplace.databinding.FragmentMyCakesBinding
import com.proyectoilerna.cheesecakeplace.databinding.ItemCakeBinding
import com.proyectoilerna.cheesecakeplace.entities.Cake

class MyCakesFragment: Fragment() {

    private lateinit var mBinding: FragmentMyCakesBinding

    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Cake, CakeHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMyCakesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.progressBar.visibility = View.VISIBLE

        // Construimos una consulta en el nodo "listOfCakes" en base al usuario activo
        val query = FirebaseDatabase
            .getInstance("https://cheesecake-place-default-rtdb.europe-west1.firebasedatabase.app")
            .reference.child("Cakes").child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("listOfCakes")

        // Construimos la Vista Recycler con el item Cake en base a nuestra consulta
        val options = FirebaseRecyclerOptions.Builder<Cake>()
            .setQuery(query, Cake::class.java).build()

        // Usamos los métodos de Firebase para el Adapter del RecyclerView
        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Cake, CakeHolder>(options){

            private lateinit var mContext: Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeHolder {

                    mContext = parent.context

                    val viewCake = LayoutInflater.from(context)
                        .inflate(R.layout.item_cake, parent, false)
                    return CakeHolder(viewCake)
                }

            override fun onBindViewHolder(holder: CakeHolder, position: Int, model: Cake) {
                val cake = getItem(position)

                with(holder){

                    setListener(cake, mContext)

                    binding.itemCakeRestaurant.text = cake.name
                    binding.itemCakeCity.text = cake.city
                    binding.itemCakePostcode.text = cake.postalCode
                    binding.ratingStars.rating = cake.stars

                    Glide.with(mContext)
                        .load(cake.photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding.itemCakePhoto)
                }
            }

            // La vista actualizará los datos con la información en tiempo real de la BD
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChanged() {
                super.onDataChanged()
                mBinding.progressBar.visibility = View.GONE
                notifyDataSetChanged()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
            }
        }

        mLayoutManager = LinearLayoutManager(context)

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    private fun deleteCake(cake: Cake){
        val databaseReference = FirebaseDatabase
            .getInstance("https://cheesecake-place-default-rtdb.europe-west1.firebasedatabase.app")
            .reference.child("Cakes").child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("listOfCakes")
        databaseReference.child(cake.id).removeValue()
    }

    // Dentro de la clase Restaurant del ViewHolder, manejamos los eventos
    inner class CakeHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemCakeBinding.bind(view)
        fun setListener(cake: Cake, context: Context){
            // El usuario pulsa en icono Borrar
            binding.buttonDelete.setOnClickListener {
                // Cuadro de diálogo para confirmar el borrado
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.dialog_delete_title)
                    .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                        // Acción si confirma -> Borrar
                        deleteCake(cake)
                    }.setNegativeButton(R.string.dialog_delete_cancel, null)
                    .show()
            }
        }
    }
}