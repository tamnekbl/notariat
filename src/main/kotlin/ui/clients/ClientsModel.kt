package ui.clients

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import db.dao.Client
import db.reps.ClientRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import utils.Loading
import utils.logging.Log

class ClientsModel(
    val repository: ClientRepository
) : StateScreenModel<ClientsState>(ClientsState()) {

    //private var execution: Job = Job()
    val clients = mutableStateListOf<Client>()

    init {
        Log.i("Model Created")
    }

    /*fun start(){
        flow { (0..1000).forEach { emit(it) } }
            .onEach {
                Log.i(it.toString())
                delay(1000)
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }*/

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
}