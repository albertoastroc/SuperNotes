package com.gmail.pentominto.us.supernotes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.ui.theme.Pine

@Composable
fun DrawerHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    )
    {
        Image(
            painterResource(id = R.drawable.ic_iconexported),
            modifier = Modifier
                .background(Color.Red)
                .height(420.dp)
                .width(420.dp),
            contentDescription = null
        )

    }
    Divider()
}

@Composable
fun DrawerBodyMain(
    drawerOptionsList : List<MenuItem>,
    modifier : Modifier = Modifier,
    itemTextStyle : TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick : (MenuItem) -> Unit
) {

    LazyColumn(modifier) {
        items(drawerOptionsList) { item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
            ) {

                Icon(
                    imageVector = item.icon,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f),
                    style = itemTextStyle
                )

            }

        }
    }
}

@Composable
fun DrawerBodyCategories(
    categoriesList : List<String>,
    modifier : Modifier = Modifier,
    onItemClick : (String) -> Unit,
    itemTextStyle : TextStyle = TextStyle(fontSize = 18.sp),
) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text = "Categories"
        , fontSize = 18.sp)
    }
    LazyColumn(modifier){

        items(categoriesList) { category ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(category)
                    }
                    .padding(16.dp)
            ) {

                Icon(
                    painterResource(id = R.drawable.ic_baseline_circle_24),
                    contentDescription = null,
                    tint = Pine
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = category,
                    modifier = Modifier.weight(1f),
                    style = itemTextStyle
                )

            }

        }

    }



}