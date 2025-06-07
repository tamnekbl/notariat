package ui.utils

enum class ViewMode {
    TABLE, SINGLE, EDIT;

    fun isEdit() = this == EDIT
}