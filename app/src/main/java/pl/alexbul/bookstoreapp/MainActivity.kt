package pl.alexbul.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import pl.alexbul.bookstoreapp.data.Book
import pl.alexbul.bookstoreapp.ui.theme.BookStoreAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  enableEdgeToEdge()
        setContent {
           MainScreen()
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
fun MainScreen() {
    val fb = Firebase.firestore
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
                    modifier = Modifier.fillMaxWidth().
                    padding(10.dp),
                ) {
                    Text(
                        text = book.name,
                        modifier = Modifier.fillMaxWidth().wrapContentWidth()
                        )
                }

            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier.fillMaxWidth().
            padding(10.dp),
            onClick = {
                fb.collection("books").document().set(Book (
                    "Raccon",
                    "raccoon",
                    "900",
                    "true",
                    "l"),)


            }) {
            Text(
            text = "Add book",
          //  modifier = modifier
        )}
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookStoreAppTheme {
        Greeting("Android")
    }
}