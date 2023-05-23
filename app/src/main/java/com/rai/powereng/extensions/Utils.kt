package com.rai.powereng.extensions

import android.app.AlertDialog
import android.content.Context
import androidx.navigation.NavController
import com.rai.powereng.R

fun getLevel(score: Int): Int {
    val starsPoint = 100
    val level = (score / starsPoint) + 1
    return if (level <= 20) {
        level
    } else {
        20
    }
}

fun getTimeSting(elapsedSeconds: Long): String {
    val elapsedMinutes = elapsedSeconds / 60
    val remainingSeconds = elapsedSeconds % 60
    return String.format("%d:%02d", elapsedMinutes, remainingSeconds)
}

object ConfirmationDialogUtils {
    fun showConfirmationDialog(context: Context, navController: NavController) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.confirm_exit_title)
        builder.setMessage(R.string.confirm_exit_message)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            navController.popBackStack()
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}