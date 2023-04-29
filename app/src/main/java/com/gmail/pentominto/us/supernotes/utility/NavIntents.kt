package com.gmail.pentominto.us.supernotes.utility

import android.content.Intent
import android.net.Uri

object NavIntents {

    fun getPlaystoreIntent() : Intent {
        return Intent()
            .setData(Uri.parse("market://details?id=com.gmail.pentominto.us.supernotes"))
            .setAction(Intent.ACTION_VIEW)
            .addFlags(
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
    }

    fun getPrivacyPolicyIntent() : Intent {
        return Intent()
            .setAction(Intent.ACTION_VIEW)
            .setData(
                Uri.parse("https://www.termsfeed.com/live/debf4da5-b65b-4420-a444-9e172bf8b813")
            )
    }
}
