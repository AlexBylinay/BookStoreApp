package pl.alexbul.bookstoreapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.database.core.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import pl.alexbul.bookstoreapp.data.Book
import pl.alexbul.bookstoreapp.ui.theme.BookStoreAppTheme
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  enableEdgeToEdge()
        setContent {
            val fb = Firebase.firestore
            val fs = Firebase.storage.reference.child("images")
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia()) {
                uri ->
                if (uri ==null) return@rememberLauncherForActivityResult
                val task = fs.child("cat.jpg").putBytes(bitmapToByteArrayFromPhone(this, uri))
                task.addOnSuccessListener {
                        uploadTask ->
                    uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener {uriTask ->
                        saveBook(fb, uriTask.result.toString() )}
                }
            }
           MainScreen{launcher.launch(PickVisualMediaRequest(
               mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
           ))}
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun MainScreen(Onclick: ()-> Unit  ) {
    val context = LocalContext.current
    val fb = Firebase.firestore
    val fs = Firebase.storage.reference.child("images")

    val list = remember {
        mutableStateOf(emptyList<Book>())
    }

    fb.collection("books").addSnapshotListener { sbapShot, exeptoin ->
        list.value = sbapShot?.toObjects(Book::class.java) ?: emptyList()

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Hello ",

            )


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        )
        {
            items(list.value){ book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically

                       ) {
                        AsyncImage(
                            model = book.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.height(70.dp).width(70.dp)
                        )


                        Text(
                            text = book.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                        )
                    }
                }

            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onClick = {

                val task = fs.child("cat.jpg").putBytes(bitmapToByteArray(context))
task.addOnSuccessListener {
        uploadTask ->
    uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener {uriTask ->
        saveBook(fb, uriTask.result.toString() )}
}
            }) {
            Text(
            text = "Add book",
          //  modifier = modifier
        )}
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onClick = {

             Onclick()
            }) {
            Text(
                text = "Open Photo",
                //  modifier = modifier
            )}
    }

}

private fun bitmapToByteArray( context: android.content.Context): ByteArray   {
     val bitMap = BitmapFactory.decodeResource(context.resources, R.drawable.cat)
val boas = ByteArrayOutputStream()
    bitMap.compress(Bitmap.CompressFormat.JPEG, 100, boas)
    return boas.toByteArray()
}

private fun bitmapToByteArrayFromPhone( context: android.content.Context, uri:Uri): ByteArray   {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitMap = BitmapFactory.decodeStream(inputStream)
    val boas = ByteArrayOutputStream()
    bitMap.compress(Bitmap.CompressFormat.JPEG, 100, boas)
    return boas.toByteArray()
}



private fun saveBook (fb: FirebaseFirestore, url: String){
    fb.collection("books").document().set(Book (
        "Raccon",
        "raccoon",
        "900",
        "true",
        url),)

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookStoreAppTheme {
        Greeting("Android")
    }
}