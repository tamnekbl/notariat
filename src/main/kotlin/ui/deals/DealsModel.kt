package ui.deals

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import db.dao.SimpleDeal
import db.reps.DealRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import utils.Loading
import utils.logging.Log

class DealsModel(
    val repository: DealRepository
) : StateScreenModel<DealsState>(DealsState()) {

    val simpleDeals = mutableStateListOf<SimpleDeal>()

    init {
        Log.i("Model Created")
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
}