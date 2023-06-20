package com.proyectoilerna.cheesecakeplace.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.proyectoilerna.cheesecakeplace.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {

    private lateinit var mBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Leemos email de la BD
        mBinding.profileEmail.text = FirebaseAuth.getInstance().currentUser?.email

        // Llevamos la referencia al nodo del usuario activo
        val databaseReference = FirebaseDatabase
            .getInstance("https://cheesecake-place-default-rtdb.europe-west1.firebasedatabase.app")
            .reference.child("Cakes").child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        databaseReference.child("userName").get().addOnSuccessListener {
            // Leemos el user name de la BD
            val userName: String = it.value.toString()
            mBinding.profileUsername.text = userName
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        // Comportamiento del botón Salir -> Cerrar Sesión -> Intent a la actividad Login
        mBinding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginScreen::class.java)
            activity?.startActivity(intent)
        }
    }
}