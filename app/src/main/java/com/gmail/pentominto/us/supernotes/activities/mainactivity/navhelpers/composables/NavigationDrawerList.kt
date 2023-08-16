package com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.MenuItem

/**
 *
 * Creates a list of the options to be shown in the nav drawer
 *
 */

@Composable
fun NavigationDrawerList(
    drawerOptionsList : List<MenuItem>,
    onSettingClick : (Int) -> Unit,
    itemTextStyle : TextStyle = TextStyle(fontSize = 18.sp)
) {
    LazyColumn {
        item {
            DrawerHeader()
        }
        items(drawerOptionsList) { item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSettingClick(item.id)
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f),
                    style = itemTextStyle,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }

        item { Divider() }
    }
}

@Composable
fun DrawerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = R.drawable.app_icon),
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            contentDescription = null
        )
    }
    Divider()
}
