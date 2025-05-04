package ui.main


import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import utils.logging.Log


class MainModel() : ScreenModel{

    private var execution: Job = Job()

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
}