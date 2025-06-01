package ui.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import utils.Margin
import utils.Size

@Composable
fun Dialog1(
    onDismiss: () -> Unit,
    title: String = "",
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .width(Size.h5)
                .padding(horizontal = Margin.m)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (title.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 0.dp, bottom = 0.dp),
                        text = title,
                        style = MaterialTheme.typography.body1
                    )
                }

                content()
            }
        }
    }
}


/*
@Composable
fun DialogButtons(
    onDismissText: Int = android.R.string.cancel,
    onSubmitText: Int = android.R.string.ok,
    onDismiss: (() -> Unit)? = null,
    onSubmit: (() -> Unit)? = null,
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Margin.m)
    ) {
        content()

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (onDismiss != null)
                TextButton(
                    modifier = Modifier,
                    onClick = { onDismiss() }
                ) {
                    Text(stringResource(id = onDismissText))
                }
            if (onSubmit != null) {
                Spacer(modifier = Modifier.size(Margin.m))
                TextButton(
                    modifier = Modifier,
                    onClick = { onSubmit() }
                ) {
                    Text(stringResource(id = onSubmitText))
                }
            }
        }
    }
}*/
