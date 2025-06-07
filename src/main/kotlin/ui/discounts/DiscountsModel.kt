package ui.discounts

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import db.dao.Discount
import db.reps.DiscountRepository
import kotlinx.coroutines.flow.*
import ui.utils.Action
import ui.utils.ViewMode
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
                    discounts.indexOfFirst { it.id == id }
                }?.let {
                    mutableState.value = state.value.copy(currentDiscountIndex = it)
                }
                getDiscount(
                    discountId = action.id ?: discounts[state.value.currentDiscountIndex].id
                )
            }

            is Action.TableView -> {
                setViewMode(ViewMode.TABLE)
                getDiscounts()
            }

            is Action.PrevNext -> {
                mutableState.value = state.value.copy(
                    currentDiscountIndex = (state.value.currentDiscountIndex + action.delta).coerceIn(
                        0,
                        discounts.size - 1
                    )
                )
                getDiscount(discounts[state.value.currentDiscountIndex].id)
            }
        }
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

    private fun getDiscount(discountId: Long) {
        flow { emit(repository) }
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getWithDeals(discountId)
            }
            .onEach {
                mutableState.value = state.value.copy(
                    discount = it,
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
