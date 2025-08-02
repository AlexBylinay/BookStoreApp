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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun LoginScreen() {

    val aut = Firebase.auth
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val text = remember { mutableStateOf("Hello") }



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
               )  { text.value = "Ful"}
           })
       { Text (text = "Sing Up")}

        Spacer(modifier = Modifier.height(15.dp))

        Text(text.value, fontSize=28.sp, fontFamily= FontFamily.Serif)

}


}
private fun signUp (aut: FirebaseAuth, email:String,password:String, OnClick:() -> Unit,
                    OnClick2:()-> Unit)
{
    aut.createUserWithEmailAndPassword(email,password).
    addOnCompleteListener{
        if(it.isSuccessful) {OnClick() }
        else { OnClick2() }
    }

}