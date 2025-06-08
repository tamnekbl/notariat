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
            is Action.Save -> {
                saveServiceWithChanges(serviceId = state.value.service.id)
                setViewMode(ViewMode.SINGLE)
            }
            is Action.Create -> TODO()
            is Action.Delete -> TODO()
            is Action.SetViewMode -> {
                setViewMode(action.viewMode)
            }

            is Action.LoadSingle -> {
                action.id?.let { id ->
                    services.indexOfFirst { it.id == id }
                }?.let {
                    mutableState.value = state.value.copy(currentServiceIndex = it)
                }
                updateService(services[state.value.currentServiceIndex])
                getService(
                    serviceId = action.id ?: services[state.value.currentServiceIndex].id
                )
            }

            is Action.TableView -> {
                setViewMode(ViewMode.TABLE)
                getServices()
            }

            is Action.PrevNext -> {
                mutableState.value = state.value.copy(
                    currentServiceIndex = (state.value.currentServiceIndex + action.delta).coerceIn(
                        0,
                        services.size - 1
                    )
                )
                updateService(services[state.value.currentServiceIndex])
                getService(services[state.value.currentServiceIndex].id)
            }
        }
    }

    fun updateService(service: Service) {
        mutableState.value = state.value.copy(
            service = service
        )
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
                    serviceFull = it,
                    loading = Loading.Success()
                )
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }

    private fun saveServiceWithChanges(serviceId: Long) {
        val service = state.value.service
        flow { emit(repository) }
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.update(serviceId, service)
            }
            .onEach {
                if (it) {
                    mutableState.value = state.value.copy(
                        serviceFull = state.value.serviceFull?.copy(service = service),
                        loading = Loading.Success()
                    )
                    services[state.value.currentServiceIndex] = service
                } else
                    throw RuntimeException("bad update service")
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }

    private fun setViewMode(mode: ViewMode) {
        mutableState.value = state.value.copy(viewMode = mode)
    }
}
