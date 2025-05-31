package ui.utils

sealed class Action {
    class SingleView(val id: Long? = null) : Action()
    class TableView() : Action()
    class Edit(val id: Long) : Action()
    class Delete(val id: Long) : Action()
    class Create() : Action()
    class Add() : Action()
    class Next() : Action()
    class Prev() : Action()
}