package com.example.desertclicker

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desertclicker.data.DataSource
import com.example.desertclicker.model.Dessert
import com.example.desertclicker.model.DessertViewModel
import com.example.desertclicker.ui.theme.DesertClickerTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onStart() {
        super.onStart()
        Log.d("TAG", " on Start Called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesertClickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                        DesertClickerApp()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", " on Resume Called")
    }
}


@Composable
fun DesertClickerApp( dessertViewModel: DessertViewModel = viewModel()) {
     val dessertUiState by dessertViewModel.uiState.collectAsState()
    //var revenue by rememberSaveable { mutableIntStateOf(0) }
    //var dessertsSold by rememberSaveable { mutableIntStateOf(0) }
    //var index by rememberSaveable {
        //mutableStateOf(0)
   // }
    //var dessert by rememberSaveable {
      //  mutableStateOf(desserts[0])
    //}
    Scaffold(
        topBar = { DesertTopApp(
            context = LocalContext.current,
            modifier = Modifier.background(color = MaterialTheme.colorScheme.primary),
            dessertsSold = dessertUiState.dessertSold,
            revenue = dessertUiState.revenue )
        }
    ) {

        DesertClicker(
            modifier = Modifier.padding(it),
            imageResource = dessertUiState.dessert!!.imageId ,
            dessertsSold = dessertUiState.dessertSold,
            revenue = dessertUiState.revenue,
            onClick = {
                      val updatedRevenue = dessertUiState.revenue + (dessertUiState.dessert?.price?:0)
                      val dessertSold = dessertUiState.dessertSold+ 1
                      dessertViewModel.updateUiState(updatedRevenue,dessertSold)
            },
        )
    }
}
fun shareSoldDessertInfo(intentContext: Context, dessertSold:Int,revenue:Int){
    val sendIntent = Intent().apply{
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_TEXT,  intentContext.getString( R.string.share_text, dessertSold, revenue))
      type ="text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    try{
        ContextCompat.startActivity(intentContext, shareIntent, null)
    }catch(e:ActivityNotFoundException ){
            Toast.makeText(
                intentContext," Sharing not Available ", Toast.LENGTH_LONG
            )
    }

}
@Composable
fun DesertClicker(modifier :Modifier = Modifier, onClick : ()->Unit, imageResource :Int , dessertsSold: Int,revenue: Int){
    Box(modifier = Modifier){

        Image(
            painterResource(id = R.drawable.bakery_back),
            contentDescription = null,
            modifier = Modifier,
            contentScale = ContentScale.Crop
        )
        Column {
            Box( modifier = Modifier
                .weight(1f)
                .fillMaxWidth()){
                Image(
                    painter = painterResource(imageResource),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(150.dp)
                        .width(150.dp)
                        .clickable { onClick() },
                )
            }
            // Surface(Modifier.padding(10.dp)){  Text("DessertSold " )}
            DesertInfo(
                dessertsSold = dessertsSold,
                revenue = revenue,
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
            )
        }
    }
}

@Composable
fun DesertInfo(modifier :Modifier,dessertsSold:Int, revenue:Int){
    Column (modifier = modifier){
        Spacer(modifier = modifier.height(10.dp))
        DessertSold(
            modifier = Modifier.padding(8.dp),
            revenue = dessertsSold,
        )
        Spacer(modifier = Modifier.height(20.dp))
        RevenueInfo(
            modifier = Modifier.padding(8.dp),
            revenue = revenue
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun DessertSold(modifier: Modifier , revenue:Int){
    Box(modifier = modifier
        .padding(4.dp)
        .fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text =" Dessert Sold" , style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSecondaryContainer )
            Text( "$revenue", textAlign = TextAlign.Right , style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}
@Composable
fun RevenueInfo(modifier: Modifier,revenue: Int ){
    Box(modifier = modifier
        .padding(4.dp)
        .fillMaxWidth()){

        Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text =" Total Revenue", style = MaterialTheme.typography.titleLarge  )
            Text( "$$revenue", textAlign = TextAlign.Right, style = MaterialTheme.typography.titleLarge)
        }
    }
}
@Composable
fun DesertTopApp(modifier :Modifier ,context: Context, dessertsSold: Int, revenue: Int){

    Surface(modifier = modifier
        .padding(10.dp)
        .fillMaxWidth()) {
        Row( modifier = modifier, horizontalArrangement =Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Dessert Clicker" , style = MaterialTheme.typography.titleLarge ,color = MaterialTheme.colorScheme.onPrimary)
            IconButton(onClick ={ shareSoldDessertInfo(context, dessertSold =  dessertsSold, revenue) }){
                  Icon(
                      imageVector = Icons.Filled.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onPrimary
                  )
            }
        }
    }
}

