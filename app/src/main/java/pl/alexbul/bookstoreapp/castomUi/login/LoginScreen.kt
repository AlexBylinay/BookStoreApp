package pl.alexbul.bookstoreapp.castomUi.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun LoginScreen() {

    val aut = Firebase.auth
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val text = remember { mutableStateOf("Hello") }
    val userEmail = remember { mutableStateOf("Null") }
    val userPassword = remember { mutableStateOf("Null") }


    userEmail.value = aut.currentUser?.email.toString()




    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        TextField(value = emailState.value,
            onValueChange = { emailState.value = it })


        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = passwordState.value,
            onValueChange = { passwordState.value = it })

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {

                signUp(
                    aut, emailState.value, passwordState.value,
                    OnClick = { text.value = "Success" }
                ) { text.value = "Ful" }
            })
        { Text(text = "Sing Up") }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {

                signIn(
                    aut, emailState.value, passwordState.value,
                    OnClick = { text.value = "Success" },
                    onClick2 = {userEmail.value= emailState.value}
                ) { text.value = "Ful" }


            })
        { Text(text = "Sing In") }

        Spacer(modifier = Modifier.height(15.dp))


        Button(
            onClick = {

                signOut(aut) { userEmail.value = "null" }
            })
        { Text(text = "Sing Out") }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                deleteAccount(aut, emailState.value, passwordState.value,
                    onClick = { text.value = "Success" }
                ) { text.value = "Ful" }
                userEmail.value = aut.currentUser?.email.toString()
            })
        { Text(text = "Delete Account") }

        Spacer(modifier = Modifier.height(15.dp))

        Text(text.value, fontSize = 28.sp, fontFamily = FontFamily.Serif)

        Spacer(modifier = Modifier.height(15.dp))

        Text(userEmail.value, fontSize = 20.sp, fontFamily = FontFamily.Default)

    }


}

private fun signUp(
    aut: FirebaseAuth, email: String, password: String, OnClick: () -> Unit,
    OnClick2: () -> Unit
) {
    aut.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
        if (it.isSuccessful) {
            OnClick()
        } else {
            OnClick2()
        }
    }


}

private fun signIn(
    aut: FirebaseAuth, email: String, password: String, OnClick: () -> Unit,
    onClick2: () -> Unit, onClick3: () -> Unit
) {
    aut.signInWithEmailAndPassword(email, password).addOnCompleteListener {
        if (it.isSuccessful) {
            OnClick()
            onClick2()

        } else {
            onClick3()
        }
    }
}

private fun signOut(aut: FirebaseAuth, onClick: () -> Unit) {
    aut.signOut()
    onClick()

}

private fun deleteAccount(
    aut: FirebaseAuth, email: String, password: String, onClick: () -> Unit,
    OnClick2: () -> Unit
) {
    val credential = EmailAuthProvider.getCredential(email,password)
    aut.currentUser?.reauthenticate(credential)?.addOnCompleteListener {
        if (it.isSuccessful) {
            aut.currentUser?.delete()
            onClick()
        } else {
            OnClick2()
        }


    }
}
