package com.lugares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN=9001
    }

    //Cliente de autenticación de google
    private lateinit var googleSignInClient: GoogleSignInClient

    //Variable para acceder a la autent
    private lateinit var auth: FirebaseAuth

    //Variable para acceder a los elementos de activity_main.xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Se establece el enlace con la vista xml mediante el objeto binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se inicializa Firebase y se asigna el objeto para autenticación
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        binding.btRegister.setOnClickListener { haceRegistro() }
        binding.btLogin.setOnClickListener { haceLogin() }

        //Opciones para hacer la autenticacion en google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_idr))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        binding.btGoogle.setOnClickListener { googleSignIn()}
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener (this) { task ->
                if (task.isSuccessful) { //Se logró autenticar en Firebase AUTH
                    actualiza(auth.currentUser)
                } else { //No se logrí autenticar en Firebase
                    actualiza(null)
                }
            }
    }

    //Este método ejecuta cuando regresa de hacer la autenticación en google...
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) { //Si entre de fijo viene de la solicitud de aut en Google
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(cuenta.idToken!!)
            } catch (e: ApiException) {

            }
        }
    }

    private fun haceLogin() {
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Se usa la función para crear un usuario por medio de correo y contraseña
        auth.signInWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    actualiza(user)
                } else {
                    Toast.makeText(baseContext,
                        getString(R.string.msg_fallo_login),
                        Toast.LENGTH_SHORT).show()
                    actualiza(null)
                }
            }
    }

    private fun haceRegistro() {
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Se usa la función para crear un usuario por medio de correo y contraseña
        auth.createUserWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    actualiza(user)
                } else {
                    Toast.makeText(baseContext,
                        getString(R.string.msg_fallo_registro),
                        Toast.LENGTH_SHORT).show()
                    actualiza(null)
                }
            }
    }

    private fun actualiza(user: FirebaseUser?) {
        if (user!=null) {
            // paso a la pantalla principal
            val intent = Intent(this,Principal::class.java)
            startActivity(intent)
        }
    }

    //Verifica si el ususario se encuentra autenticado y si es así pasa a la siguiente
    //pantalla sin pedir autenticación
    public override fun onStart() {
        super.onStart()
        //obtenemos el usuario actual... si existe...
        val usuario = auth.currentUser
        actualiza(usuario)
    }
}