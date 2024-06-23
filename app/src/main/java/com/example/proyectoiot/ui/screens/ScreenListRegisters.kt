package com.example.proyectoiot.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.proyectoiot.R
import com.example.proyectoiot.ui.pagging.PagingItemCard
import com.example.proyectoiot.ui.pagging.PagingViewData
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.Flow


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenListRegisters(
    navController: NavController,
    viewModel: PagingViewData
){
    val FontKaushan = FontFamily(Font(R.font.kaushan_script_regular))
    val FontMontserrat = FontFamily(Font(R.font.montserrat_wght))
    var clicked by remember { mutableStateOf(true) }

    Box(){
        Column( verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
        ) {
            Spacer(modifier = Modifier.height(height = 30.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Registros",
                    fontSize = 32.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontKaushan,
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(16.dp)
                        )
                        ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 20.dp)

                        ){
                            Text(
                                text = if(clicked)"LUM   " else "MOV  ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = FontMontserrat,
                            )
                            Switch(
                                checked = clicked,
                                onCheckedChange = { newCheckedState ->
                                    clicked = newCheckedState
                                    },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color.LightGray,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color.LightGray
                                ),
                            )
                        }
                    }
                }
            }

            val data = viewModel.sensorPagingFlow.collectAsLazyPagingItems()
            val state by viewModel.isLoading.collectAsState()
            val refreshState = rememberSwipeRefreshState(isRefreshing  = state)
            val context = LocalContext.current

            LaunchedEffect(key1 = data.loadState) {
                if (data.loadState.refresh is LoadState.Error) {
                    Toast.makeText(
                        context,
                        "Error: " + (data.loadState.refresh as LoadState.Error).error.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp, bottom = 55.dp)
            ) {

                if (data.loadState.refresh is LoadState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp),
                        color = Color.White,
                        strokeWidth = 8.dp,
                    )
                } else {

                    SwipeRefresh(
                        state = refreshState,
                        onRefresh = viewModel::pauseFlow,
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                            items(data) { pagingObject ->
                                if (pagingObject != null) {
                                    PagingItemCard(pagingObject, clicked)
                                }
                            }

                            item {
                                if (data.loadState.append is LoadState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        color = Color.White,
                                        strokeWidth = 7.dp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
