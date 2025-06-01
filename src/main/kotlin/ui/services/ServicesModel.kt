package ui.services

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import db.dao.Service
import db.reps.ServiceRepository
import kotlinx.coroutines.flow.*
import ui.utils.Action
import ui.utils.ViewMode
import utils.Loading
import utils.logging.Log

class ServicesModel(
    val repository: ServiceRepository
) : StateScreenModel<ServicesState>(ServicesState()) {

    val services = mutableStateListOf<Service>()

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
                    services.indexOfFirst { it.id == id }
                }?.let {
                    mutableState.value = state.value.copy(currentServiceIndex = it)
                }
                getService(
                    serviceId = action.id ?: services[state.value.currentServiceIndex].id
                )
            }

            is Action.TableView -> {
                setViewMode(ViewMode.TABLE)
                getServices()
            }

            is Action.Next -> {
                mutableState.value = state.value.copy(
                    currentServiceIndex = state.value.currentServiceIndex.inc().coerceAtMost(services.size - 1)
                )
                getService(services[state.value.currentServiceIndex].id)
            }

            is Action.Prev -> {
                mutableState.value = state.value.copy(
                    currentServiceIndex = state.value.currentServiceIndex.dec().coerceAtLeast(0)
                )
                getService(services[state.value.currentServiceIndex].id)
            }
        }
    }
    
    fun getServices(){
        services.clear()
        flow { emit(repository)}
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getAll()
            }
            .onEach {
                services.addAll(it)
                mutableState.value = state.value.copy(loading = Loading.Success())
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)

    }

    private fun getService(serviceId: Long) {
        flow { emit(repository) }
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getWithDeals(serviceId)
            }
            .onEach {
                mutableState.value = state.value.copy(
                    service = it,
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
