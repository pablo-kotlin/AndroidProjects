package com.proyectoilerna.cheesecakeplace.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.proyectoilerna.cheesecakeplace.R
import com.proyectoilerna.cheesecakeplace.databinding.ActivityHomeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityHomeBinding

    private lateinit var mActiveFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setBottomMenu() // Definimos la funcionalidad del Bottom Navigation Menu

        // Botón Añadir Tarta -> Intent para la actividad correspondiente
        mBinding.buttonAddBusiness.setOnClickListener {
            val intent = Intent(this, AddCakeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setBottomMenu() {
        val mFragmentManager = supportFragmentManager

        val myCakesFragment = MyCakesFragment()
        val searchFragment = SearchFragment()
        val profileFragment = ProfileFragment()

        mActiveFragment = myCakesFragment // Fragmento por defecto

        mFragmentManager.beginTransaction()
            .add(R.id.container_fragment, profileFragment, ProfileFragment::class.java.name)
            .hide(profileFragment).commit()

        mFragmentManager.beginTransaction()
            .add(R.id.container_fragment, searchFragment, SearchFragment::class.java.name)
            .hide(searchFragment).commit()

        mFragmentManager.beginTransaction()
            .add(R.id.container_fragment, myCakesFragment, MyCakesFragment::class.java.name)
            .commit()

        // Listener activo para determinar qué fragmento inyectar
        mBinding.navigationMenu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_my_sites -> { // Fragmento Mis sitios
                    mBinding.buttonAddBusiness.show()
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(myCakesFragment)
                        .commit()
                    mActiveFragment = myCakesFragment
                    true
                }
                R.id.item_explore -> { // Fragmento Explorar
                    mBinding.buttonAddBusiness.hide()
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(searchFragment)
                        .commit()
                    mActiveFragment = searchFragment
                    true
                }
                R.id.item_profile -> { // Fragmento Mi perfil
                    mBinding.buttonAddBusiness.hide()
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(profileFragment)
                        .commit()
                    mActiveFragment = profileFragment
                    true
                }
                else -> false
            }
        }
    }
}