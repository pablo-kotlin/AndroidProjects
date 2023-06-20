package com.proyectoilerna.cheesecakeplace.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyectoilerna.cheesecakeplace.databinding.ActivityLoginScreenBinding
import com.proyectoilerna.cheesecakeplace.entities.User

class LoginScreen : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginScreenBinding

    private lateinit var mDataBaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupAuth() // Construcción de la función Login - SignIn

        // Reset de la vista de todos los botones
        mBinding.btnBack.setOnClickListener {
            mBinding.tilUsername.visibility = View.INVISIBLE
            mBinding.btnSave.visibility = View.INVISIBLE
            mBinding.btnLogin.visibility = View.VISIBLE
            mBinding.btnBack.visibility = View.INVISIBLE
            mBinding.btnSignIn.visibility = View.VISIBLE
        }
    }

    private fun setupAuth() {

        // Usuario pulsa botón de Crear Cuenta
        mBinding.btnSignIn.setOnClickListener {
            mBinding.btnSignIn.visibility = View.INVISIBLE
            mBinding.btnBack.visibility = View.VISIBLE
            mBinding.tilUsername.visibility = View.VISIBLE
            mBinding.btnLogin.visibility = View.INVISIBLE
            mBinding.btnSave.visibility = View.VISIBLE
            mBinding.btnSave.setOnClickListener {
                // Comprobamos que no ha dejado ningún campo en blanco
                if (mBinding.etEmail.text!!.isNotEmpty() && mBinding.etPassword.text!!.isNotEmpty()
                    && mBinding.etUsername.text!!.isNotEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        mBinding.etEmail.text.toString(),
                        mBinding.etPassword.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            saveUser(FirebaseAuth.getInstance().currentUser!!.uid,
                                mBinding.etUsername.text.toString(),
                                mBinding.etEmail.text.toString())
                            showHome()
                        } else { // Mostraremos este error en caso de que ya esté registrado
                            Toast.makeText(this, "El correo electrónico ya está registrado",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                } else { // Mostraremos este error si no ha rellenado algún campo
                    Toast.makeText(this, "Debe rellenar todos los campos",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        mBinding.btnLogin.setOnClickListener {
            // Comprobamos que no ha dejado ningún campo en blanco
            if (mBinding.etEmail.text!!.isNotEmpty() && mBinding.etPassword.text!!.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    mBinding.etEmail.text.toString(),
                    mBinding.etPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome()
                    } else { // Mostraremos este error si la BD no relaciona email y contraseña
                        Toast.makeText(this, "E-mail/Contraseña incorrecto",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            } else { // Mostraremos este error si no ha rellenado algún campo
                Toast.makeText(this, "Debe rellenar todos los campos",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Intent para acceder a la pantalla principal
    private fun showHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Función para guardar el usuario en la BD
    private fun saveUser(id: String, userName: String, email: String)
    {
        val user = User(id = id, userName = userName, email = email)
        // Llevamos la referencia al nodo del usuario activo
        mDataBaseReference = FirebaseDatabase
            .getInstance(
                "https://cheesecake-place-default-rtdb.europe-west1.firebasedatabase.app/")
            .reference.child("Cakes").child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        mDataBaseReference.setValue(user)
    }
}