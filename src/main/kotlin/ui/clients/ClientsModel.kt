package ui.clients

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import db.dao.Client
import db.reps.ClientRepository
import kotlinx.coroutines.flow.*
import ui.utils.Action
import ui.utils.ViewMode
import utils.Loading
import utils.logging.Log

class ClientsModel(
    val repository: ClientRepository
) : StateScreenModel<ClientsState>(ClientsState()) {

    val clients = mutableStateListOf<Client>()

    init {
        Log.i("Model Created")
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.Add -> TODO()
            is Action.Create -> TODO()
            is Action.Delete -> TODO()
            is Action.Edit -> {
                setViewMode(ViewMode.EDIT)
            }

            is Action.SingleView -> {
                setViewMode(ViewMode.SINGLE)
                action.id?.let { id ->
                    clients.indexOfFirst { it.id == id }
                }?.let {
                    mutableState.value = state.value.copy(currentClientIndex = it)
                }
                getClient(
                    clientId = action.id ?: clients[state.value.currentClientIndex].id
                )
            }

            is Action.TableView -> {
                setViewMode(ViewMode.TABLE)
                getClients()
            }

            is Action.Next -> {
                mutableState.value = state.value.copy(
                    currentClientIndex = state.value.currentClientIndex.inc().coerceAtMost(clients.size - 1)
                )
                getClient(clients[state.value.currentClientIndex].id)
            }

            is Action.Prev -> {
                mutableState.value = state.value.copy(
                    currentClientIndex = state.value.currentClientIndex.dec().coerceAtLeast(0)
                )
                getClient(clients[state.value.currentClientIndex].id)
            }
        }
    }

    fun getClients(){
        clients.clear()
        flow { emit(repository)}
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getAll()
            }
            .onEach {
                clients.addAll(it)
                mutableState.value = state.value.copy(loading = Loading.Success())
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }

    private fun getClient(clientId: Long) {
        flow { emit(repository) }
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getClientWithDeals(clientId)
            }
            .onEach {
                mutableState.value = state.value.copy(
                    client = it,
                    loading = Loading.Success()
                )
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }

    private fun setViewMode(mode: ViewMode) {
        mutableState.value = state.value.copy(viewMode = mode)
    }
}