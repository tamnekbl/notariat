package ui.deals

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import db.dao.SimpleDeal
import db.reps.DealRepository
import kotlinx.coroutines.flow.*
import ui.utils.Action
import ui.utils.ViewMode
import utils.Loading
import utils.logging.Log

class DealsModel(
    val repository: DealRepository
) : StateScreenModel<DealsState>(DealsState()) {

    val simpleDeals = mutableStateListOf<SimpleDeal>()

    init {
        Log.i("Model Created")
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.Save -> TODO()
            is Action.Create -> TODO()
            is Action.Delete -> TODO()
            is Action.SetViewMode -> {
                setViewMode(ViewMode.EDIT)
            }

            is Action.LoadSingle -> {
                setViewMode(ViewMode.SINGLE)
                action.id?.let { id ->
                    simpleDeals.indexOfFirst { it.id == id }
                }?.let {
                    mutableState.value = state.value.copy(currentDealIndex = it)
                }
                getDeal(
                    dealId = action.id ?: simpleDeals[state.value.currentDealIndex].id
                )
            }

            is Action.TableView -> {
                setViewMode(ViewMode.TABLE)
                getDeals()
            }

            is Action.PrevNext -> {
                mutableState.value = state.value.copy(
                    currentDealIndex = (state.value.currentDealIndex + action.delta).coerceIn(0, simpleDeals.size - 1)
                )
                getDeal(simpleDeals[state.value.currentDealIndex].id)
            }
        }
    }

    fun getDeals(){
        simpleDeals.clear()
        flow { emit(repository)}
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getAll()
            }
            .onEach {
                simpleDeals.addAll(it)
                mutableState.value = state.value.copy(loading = Loading.Success())
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)

    }

    private fun getDeal(dealId: Long) {
        flow { emit(repository) }
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getById(dealId)
            }
            .onEach {
                mutableState.value = state.value.copy(
                    deal = it,
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