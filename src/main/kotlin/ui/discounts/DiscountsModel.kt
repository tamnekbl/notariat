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
            is Action.Save -> {
                saveDiscountWithChanges(discountId = state.value.discount.id)
                setViewMode(ViewMode.SINGLE)
            }
            is Action.Create -> TODO()
            is Action.Delete -> TODO()
            is Action.SetViewMode -> {
                setViewMode(action.viewMode)
            }

            is Action.LoadSingle -> {
                action.id?.let { id ->
                    discounts.indexOfFirst { it.id == id }
                }?.let {
                    mutableState.value = state.value.copy(currentDiscountIndex = it)
                }
                updateDiscount(discounts[state.value.currentDiscountIndex])
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
                updateDiscount(discounts[state.value.currentDiscountIndex])
                getDiscount(discounts[state.value.currentDiscountIndex].id)
            }
        }
    }

    fun updateDiscount(discount: Discount) {
        mutableState.value = state.value.copy(
            discount = discount
        )
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
                    discountFull = it,
                    loading = Loading.Success()
                )
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }

    private fun saveDiscountWithChanges(discountId: Long) {
        val discount = state.value.discount
        flow { emit(repository) }
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.update(discountId, discount)
            }
            .onEach {
                if (it) {
                    mutableState.value = state.value.copy(
                        discountFull = state.value.discountFull?.copy(discount = discount),
                        loading = Loading.Success()
                    )
                    discounts[state.value.currentDiscountIndex] = discount
                } else
                    throw RuntimeException("bad update discount")
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)
    }

    private fun setViewMode(mode: ViewMode) {
        mutableState.value = state.value.copy(viewMode = mode)
    }
}
