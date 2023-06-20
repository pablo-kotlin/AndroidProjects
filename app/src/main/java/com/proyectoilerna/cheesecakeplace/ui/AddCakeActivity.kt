package com.proyectoilerna.cheesecakeplace.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.proyectoilerna.cheesecakeplace.R
import com.proyectoilerna.cheesecakeplace.databinding.ActivityAddCakeBinding
import com.proyectoilerna.cheesecakeplace.entities.Cake
import com.proyectoilerna.cheesecakeplace.entities.Restaurant

class AddCakeActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityAddCakeBinding

    private lateinit var mStorageReference: StorageReference
    private lateinit var mDataBaseReference: DatabaseReference

    private var mPhotoSelectedUri: Uri? = null

    private val databaseUrl: String = "https://cheesecake-place-default-rtdb.europe-west1.firebasedatabase.app/"

    private var galleryResult =
        // Preparamos la devolución de nuestra llamada para seleccionar una fotografía
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // Si el resultado es positivo, cargaremos la fotografía en el Image View
                mPhotoSelectedUri = it.data?.data
                mBinding.buttonUploadImg.setImageURI(mPhotoSelectedUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddCakeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        title = "Crear Nueva CheeseCake"

        // El usuario pulsa sobre el botón de carga de fotografía
        mBinding.buttonUploadImg.setOnClickListener {
            openGallery()
        }

        // El usuario pulsa sobre el botón de carga de Guardar reseña
        mBinding.buttonAddCakeSave.setOnClickListener {
            saveAddedCake()
        }

        mStorageReference = FirebaseStorage.getInstance().reference
        mDataBaseReference =
            FirebaseDatabase.getInstance(databaseUrl)
                .reference.child("Cakes")
    }

    // Intent para consultar la uri de una fotografía del dispositivo
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)
    }


    private fun saveAddedCake() {
        mBinding.progressBar.visibility = View.VISIBLE // Mostrar ProgressBar

        // Generamos id aleatorio para el almacenamiento de la reseña
        val key = mDataBaseReference.push().key!!
        // Ubicamos el nodo con el id para almacenar la fotografía de la tarta
        val storageReference = mStorageReference.child("Cakes")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child(key)

        val cakeName: String = mBinding.addCakePlaceName.text.toString().trim().uppercase()
        val cakeCity: String = mBinding.addCakePlaceCity.text.toString().trim().uppercase()
        val cakePostCode: String = mBinding.addCakePostalCode.text.toString().trim()
        val cakeWebsite: String = mBinding.addCakePlaceWebsite.text.toString().trim()
        val cakeStars: Float = mBinding.ratingStarsCake.rating

        if (mPhotoSelectedUri != null) { // Comprobamos que hay una fotografía seleccionada
            storageReference.putFile(mPhotoSelectedUri!!)
                .addOnSuccessListener { it ->
                    it.storage.downloadUrl.addOnSuccessListener {
                        // Comprobamos que los campos obligatorios no están vacíos
                        if (checkMandatoryFields(cakeName, cakeCity)) {
                            // Guardamos la tarta -> Nueva Reseña
                            saveCake(
                                key,
                                it.toString(),
                                cakeName,
                                cakePostCode,
                                cakeCity,
                                cakeStars
                            )
                            saveRestaurant(key, cakeName, cakePostCode, cakeCity, cakeWebsite, cakeStars)

                            mBinding.progressBar.visibility = View.GONE // Ocultar ProgressBar

                        } else { // Mostraremos este error si no ha rellenado algún campo obligatorio
                            Toast.makeText(this,
                                "Por favor, revise los campos obligatorios",
                                Toast.LENGTH_LONG).show()
                            mBinding.inputPlaceCity.error = getString(R.string.mandatory_field)
                            mBinding.inputPlaceName.error = getString(R.string.mandatory_field)
                            mBinding.progressBar.visibility = View.GONE // Ocultar ProgressBar
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error. Inténtelo otra vez", Toast.LENGTH_LONG)
                        .show()
                    mBinding.progressBar.visibility = View.GONE // Ocultar ProgressBar
                }
        } else { // Mostraremos este error si ha seleccionado ninguna fotografía
            Toast.makeText(this, "No ha seleccionado ninguna fotografía",
                Toast.LENGTH_LONG).show()
            mBinding.progressBar.visibility = View.GONE // Ocultar ProgressBar
        }
    }

    // Función Guardar Restaurante
    private fun saveRestaurant(
        key: String, name: String, postalCode: String, city: String, urlWebsite: String, currentVote: Float
    ) {
        var numVotes = 1F
        var rating = 0F
        val restaurant = Restaurant(
            id = key,
            name = name,
            postalCode = postalCode,
            city = city,
            urlWebsite = urlWebsite,
            votes = numVotes,
            rating = rating
        )
        // Nos ubicamos en el nodo "Cakes"
        mDataBaseReference =
            FirebaseDatabase.getInstance(databaseUrl)
                .reference.child("Cakes")

        val restaurantListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Si el restaurante ya existe, actualizamos datos, pero no lo creamos
                if (dataSnapshot.child("Restaurants").child(city).child(name).exists()) {
                    numVotes = dataSnapshot.child("Restaurants").child(city).child(name)
                        .child("votes").value.toString().toFloat()
                    rating = dataSnapshot.child("Restaurants").child(city).child(name)
                        .child("rating").value.toString().toFloat()
                    // Hacemos un tratamiento de los datos para que en el RecyclerView estén ordenados
                    // Guardamos el valor opuesto para que se presenten correctamente
                    rating = 5 - rating
                    rating = (rating * numVotes + currentVote) / (numVotes + 1)
                    rating = 5 - rating
                    numVotes += 1
                    // Actualizamos el número de votos
                    mDataBaseReference.child("Restaurants").child(city).child(name)
                        .child("votes").setValue(numVotes)
                    // Actualizamos el rating
                    mDataBaseReference.child("Restaurants").child(city).child(name)
                        .child("rating").setValue(rating)
                    // Intent -> Volvemos a la Main Activity
                    val intent = Intent(this@AddCakeActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Si el restaurante no existe, lo creamos en la BD
                    restaurant.rating = 5 - currentVote
                    mDataBaseReference.child("Restaurants").child(city).child(name)
                        .setValue(restaurant)
                    // Intent -> Volvemos a la Main Activity
                    val intent = Intent(this@AddCakeActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "onCancelled", databaseError.toException())
            }
        }
        mDataBaseReference.addListenerForSingleValueEvent(restaurantListener)
    }

    // Función Guardar Tarta -> Nueva Reseña
    private fun saveCake(
        key: String, url: String, name: String, postalCode: String, city: String, stars: Float
    ) {
        val cake = Cake(
            id = key,
            name = name,
            photoUrl = url,
            postalCode = postalCode,
            city = city,
            stars = stars
        )
        // Nos ubicamos en el nodo "Cakes" -> "Usuarios" -> "Usuario Activo" -> "listOfCakes"
        mDataBaseReference = FirebaseDatabase
            .getInstance(databaseUrl)
            .reference.child("Cakes").child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("listOfCakes")
        mDataBaseReference.child(key).setValue(cake)
    }

    // Función para comprobar si los campos "cakeName" y "cakeCity" están vacíos
    private fun checkMandatoryFields(cakeName: String, cakeCity: String): Boolean {
        var checkFields = true
        if (cakeName.isEmpty() || cakeCity.isEmpty()) {
            checkFields = false
        }
        return checkFields
    }
}