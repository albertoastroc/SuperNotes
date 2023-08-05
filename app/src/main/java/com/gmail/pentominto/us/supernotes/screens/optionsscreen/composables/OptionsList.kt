package com.gmail.pentominto.us.supernotes.screens.optionsscreen.composables

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import com.gmail.pentominto.us.supernotes.screens.optionsscreen.OptionsScreenState
import com.gmail.pentominto.us.supernotes.screens.optionsscreen.OptionsScreenViewModel

@Composable
fun OptionsList(
    optionsState: OptionsScreenState,
    viewModel: OptionsScreenViewModel,
    context: Context
) {
    OptionsRowWithSwitch(
        title = "Enable categories",
        subTitle = "When disabled one uncategorized list will be shown",
        switchState = optionsState.categoriesOption
    ) {
        viewModel.categoriesPrefToggle()
    }

    OptionsRowWithSwitch(
        title = "Enable Trash folder",
        subTitle = "When disabled notes will be permanently deleted when swiped off the home screen",
        switchState = optionsState.trashEnabled
    ) {
        viewModel.trashFolderToggle()
    }

    OptionsRowWithAlertDialog(
        title = "Delete home screen notes",
        subTitle = "Deleting this way will not send notes to the Trash folder",
        message = "Are you sure you want to delete all notes?",
        yesButtonMessage = "Delete",
        noButtonMessage = "Cancel"
    ) {
        viewModel.deleteAllNotes()
    }
    OptionsRowWithAlertDialog(
        title = "Delete notes in Trash folder",
        subTitle = null,
        message = "Are you sure you want to delete all notes in Trash?",
        yesButtonMessage = "Delete",
        noButtonMessage = "Cancel"
    ) {
        viewModel.deleteAllTrashNotes()
    }

    OptionsRowWithAlertDialog(
        title = "Request deletion of data",
        subTitle = "Data collected is used to help diagnose crashes and analyze app performance",
        message = "This will setup an email with some information that is needed for deleting the data. ",
        yesButtonMessage = "Continue",
        noButtonMessage = "Cancel"
    ) {
        val uriText = "mailto:simplenotesacf@gmail.com" +
                "?subject=" + "Data deletion request" +
                "&body=" + "Delete data for ID ${optionsState.userId}"

        val emailIntent = Intent()
            .setData(Uri.parse(uriText))
            .setAction(Intent.ACTION_SENDTO)

        context.startActivity(emailIntent)
    }
}
