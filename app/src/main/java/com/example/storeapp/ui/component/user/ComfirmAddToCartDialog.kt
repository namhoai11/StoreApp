package com.example.storeapp.ui.component.user

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.storeapp.ui.theme.StoreAppTheme



@Composable
fun ConfirmAddToCartDialog(
    onDismiss: () -> Unit, // Thêm callback để đóng dialog
    message: String,
    navigateToCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
//    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
        },
        title = { Text(text = "Thành công") },
        text = { Text(text = message) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss() // Đóng dialog khi thoát
                }
            ) {
                Text(text = "Đóng")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    navigateToCart()
                    onDismiss()
                }
            ) {
                Text(text = "Đến giỏ hàng")
            }
        }
    )
}

@Composable
fun AlertDialog(
    onDismiss: () -> Unit,
    title:String,
    message: String,
    modifier: Modifier = Modifier,
    ) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
        },
        title = { Text(text = title) },
        text = { Text(text = message) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
//                    onDismiss() // Đóng dialog khi thoát
                }
            ) {
//                Text(text = "OK")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
//                    navigateToCart()
                    onDismiss()
                }
            ) {
                Text(text = "OK")
            }
        }
    )
}
@Composable
fun ConfirmRemovedDialog(
    onDismiss: () -> Unit, // Thêm callback để đóng dialog
    message: String,
    confirmRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
//    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
        },
        title = { Text(text = "Xóa Sản phẩm") },
        text = { Text(text = message) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss() // Đóng dialog khi thoát
                }
            ) {
                Text(text = "Hủy")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    confirmRemove()
                    onDismiss()
                }
            ) {
                Text(text = "Xác nhận")
            }
        }
    )
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinalScoreDialogPreview() {
    StoreAppTheme {
        ConfirmAddToCartDialog(
            {},
            "",
            {}
        )
    }
}