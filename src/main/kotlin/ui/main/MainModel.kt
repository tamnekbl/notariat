package ui.main


import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import db.Client
import db.Queries
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import utils.logging.Log


class MainModel() : ScreenModel{

    private var execution: Job = Job()
    val clients = mutableStateListOf<Client>()

    override fun onDispose() {
        super.onDispose()
        execution.cancel()
    }

    fun start(){
        execution.cancel()
        execution = flow { (0..1000).forEach { emit(it) } }
            .onEach {
                Log.i(it.toString())
                delay(1000)
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }

    fun getClients(){
        flow { emit(Queries().getAll()) }
            .onEach { clients.addAll(it) }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)

    }
}