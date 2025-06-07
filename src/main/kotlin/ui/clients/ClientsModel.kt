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

    fun updateClient(client: Client) {
        mutableState.value = state.value.copy(
            client = client
        )
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.Save -> {
                saveClientWithChanges(clientId = state.value.client.id)
                setViewMode(ViewMode.SINGLE)
            }
            is Action.Create -> TODO()
            is Action.Delete -> TODO()
            is Action.SetViewMode -> {
                setViewMode(action.viewMode)
            }

            is Action.LoadSingle -> {
                action.id?.let { id ->
                    clients.indexOfFirst { it.id == id }
                }?.let {
                    mutableState.value = state.value.copy(currentClientIndex = it)
                }
                updateClient(clients[state.value.currentClientIndex])
                getClientWithDeals(clientId = action.id ?: clients[state.value.currentClientIndex].id)
            }

            is Action.TableView -> {
                setViewMode(ViewMode.TABLE)
                getClients()
            }

            is Action.PrevNext -> {
                mutableState.value = state.value.copy(
                    currentClientIndex = (state.value.currentClientIndex + action.delta).coerceIn(0, clients.size - 1)
                )
                getClientWithDeals(clients[state.value.currentClientIndex].id)
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

    private fun getClientWithDeals(clientId: Long) {
        flow { emit(repository) }
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getClientWithDeals(clientId)
            }
            .onEach {
                mutableState.value = state.value.copy(
                    clientFull = it,
                    loading = Loading.Success()
                )
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }

    private fun saveClientWithChanges(clientId: Long) {
        val client = state.value.client
        flow { emit(repository) }
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.update(clientId, client)
            }
            .onEach {
                if (it) {
                    mutableState.value = state.value.copy(
                        clientFull = state.value.clientFull?.copy(client = client),
                        loading = Loading.Success()
                    )
                    clients[state.value.currentClientIndex] = client
                } else
                    throw RuntimeException("bad update client")
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }

    private fun setViewMode(mode: ViewMode) {
        mutableState.value = state.value.copy(viewMode = mode)
    }
}