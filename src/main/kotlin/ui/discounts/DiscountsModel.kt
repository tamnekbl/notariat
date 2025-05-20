package ui.discounts

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import db.dao.Discount
import db.reps.DiscountRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import utils.Loading
import utils.logging.Log

class DiscountsModel(
    val repository: DiscountRepository
) : StateScreenModel<DiscountsState>(DiscountsState()) {

    //private var execution: Job = Job()
    val discounts = mutableStateListOf<Discount>()

    init {
        Log.i("Model Created")
    }

    fun getDiscounts(){
        discounts.clear()
        flow { emit(repository)}
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getAll()
            }
            .onEach {
                discounts.addAll(it)
                mutableState.value = state.value.copy(loading = Loading.Success())
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)

    }
}
