package ui.utils

sealed class Action {
    class LoadSingle(val id: Long? = null) : Action()
    class TableView() : Action()
    class SetViewMode(val viewMode: ViewMode) : Action()
    class Delete(val id: Long) : Action()
    class Create() : Action()
    class Save() : Action()
    class PrevNext(val delta: Int) : Action()
}