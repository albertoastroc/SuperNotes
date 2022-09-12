package com.gmail.pentominto.us.supernotes

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.data.Category


@Composable
fun Drawer(
    drawerOptionsList : List<MenuItem>,
    categoriesList : List<Category>,
    onSettingClick : (MenuItem) -> Unit,
    onCategoryClick : (String) -> Unit,
    itemTextStyle : TextStyle = TextStyle(fontSize = 18.sp),
) {

    LazyColumn(

    ) {

        item {
            DrawerHeader()
        }
        items(drawerOptionsList) { item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSettingClick(item)
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

        item { Divider() }

//        item {
//            Row(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = "Categories",
//                    style = itemTextStyle,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }

//        items(categoriesList) { category ->
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable {
//
//                    }
//                    .padding(16.dp)
//            ) {
//
//                Icon(
//                    painterResource(id = R.drawable.ic_baseline_circle_24),
//                    contentDescription = null,
//                    tint = Pine
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(
//                    text = category.categoryTitle,
//                    modifier = Modifier.weight(1f),
//                    style = itemTextStyle
//                )
//
//            }
//
//        }

    }
}

@Composable
fun DrawerHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    )
    {
        Image(
            painterResource(id = R.drawable.ic_iconexported),
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            contentDescription = null
        )

    }
    Divider()
}