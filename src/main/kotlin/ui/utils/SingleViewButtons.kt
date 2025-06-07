package ui.utils

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import utils.Margin

@Composable
fun ColumnScope.SingleViewButtons(
    isEditing: Boolean,
    prevPageEnabled: Boolean,
    nextPageEnabled: Boolean,
    onAction: (Action) -> Unit
) {
    DisableSelection {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(Margin.m),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая панель — удаление
            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .wrapContentWidth(Alignment.Start),
                horizontalArrangement = Arrangement.spacedBy(Margin.s),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    enabled = !isEditing,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.error),
                    onClick = { }
                ) {
                    Text("Удалить")
                }
            }

            // Центр — PageController
            PageController(
                canNext = nextPageEnabled,
                canPrev = prevPageEnabled
            ) {
                onAction(Action.PrevNext(it))
            }

            // Правая панель с кнопками редактирования
            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .wrapContentWidth(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(Margin.s),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isEditing) {
                    Button(
                        onClick = { onAction(Action.Save()) }
                    ) {
                        Text("Сохранить")
                    }
                    OutlinedButton(
                        onClick = { onAction(Action.SetViewMode(ViewMode.SINGLE)) }
                    ) {
                        Text("Отменить")
                    }
                } else {
                    OutlinedButton(
                        onClick = { onAction(Action.SetViewMode(ViewMode.EDIT)) }
                    ) {
                        Text("Редактировать")
                    }
                }
            }
        }
    }
}