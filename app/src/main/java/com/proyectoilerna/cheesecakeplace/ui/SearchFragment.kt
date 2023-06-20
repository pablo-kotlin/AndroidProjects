package com.proyectoilerna.cheesecakeplace.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.proyectoilerna.cheesecakeplace.databinding.FragmentSearchBinding

class SearchFragment: Fragment() {
    private lateinit var mBinding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Comportamiento del botón Buscar -> Búsqueda Localidad
        mBinding.buttonSearch.setOnClickListener {
            val city = mBinding.inputSearchCity.text.toString().trim().uppercase()
            val intent = Intent(activity, ExploreActivity::class.java).apply {
                // Conservamos el valor de "city" para llevarlo a la actividad Explore
                putExtra("city", city)
            }
            if (city.isEmpty()){ // Mostraremos este error si el usuario no ha introducido nada
                Snackbar.make(mBinding.root, "Por favor, escriba el nombre de una ciudad",
                    Snackbar.LENGTH_SHORT).show()
            } else startActivity(intent)
        }
    }
}