package ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import utils.Margin
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@Composable
fun <T : Any> DataClassTable(
    modifier: Modifier = Modifier,
    data: List<T>
) {
    if (data.isEmpty()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Margin.m),
            text = "Нет данных",
            textAlign = TextAlign.Center
        )
        return
    }

    val kClass = data.first()::class
    val props: List<KProperty1<T, *>> = kClass.primaryConstructor!!
        .parameters
        .mapNotNull { param ->
            kClass.memberProperties.find { it.name == param.name } as? KProperty1<T, *>
        }

    val headers = props.map { it.name }

    Column(modifier = modifier) {
        // Заголовки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Margin.s),
        ) {
            headers.forEach { header ->
                Text(
                    text = header,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(Margin.s)
                )
            }
        }

        Divider()

        // Строки
        data.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Margin.s)
            ) {
                props.forEach { prop ->
                    Text(
                        text = prop.get(item).toString(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(Margin.s)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun Table(
    headers: Map<String, Float>,
    rows: List<List<String>>,
    onAction: (Action) -> Unit
) {
    SelectionContainer {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Заголовки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onPrimary),
            ) {
                headers.forEach { (header, weight) ->
                    Text(
                        text = header,
                        modifier = Modifier
                            .weight(weight)
                            .padding(8.dp),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Spacer(modifier = Modifier.width(96.dp))
            }

            // Данные
            LazyColumn {
                itemsIndexed(rows) { index, row ->
                    var hoveredId by remember { mutableStateOf(-1L) }
                    val backgroundColor = if (index % 2 == 0)
                        MaterialTheme.colors.onSecondary.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colors.onSecondary

                    Row(
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .fillMaxWidth()
                            .background(backgroundColor)
                            .combinedClickable(
                                onClick = {},
                                onDoubleClick = {
                                    onAction(Action.SingleView(row.first().toLongOrNull()))
                                }
                            )
                            .pointerMoveFilter(
                                onEnter = {
                                    hoveredId = row.first().toLongOrNull() ?: -1
                                    false
                                },
                                onExit = {
                                    hoveredId = -1
                                    false
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEachIndexed { index, cell ->
                            val weight = headers.values.toList()[index]
                            Box(modifier = Modifier.weight(weight)) {
                                Text(
                                    text = cell,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }
                        }

                        AnimatedVisibility(visible = hoveredId == -1L) {
                            Spacer(modifier = Modifier.width(96.dp)) // резервируем место, чтобы не прыгало
                        }
                        AnimatedVisibility(visible = hoveredId != -1L) {
                            Row {
                                IconButton(
                                    onClick = { onAction(Action.Edit(hoveredId)) }
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null)
                                }

                                IconButton(
                                    onClick = { onAction(Action.Delete(hoveredId)) }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}