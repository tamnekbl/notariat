package db

interface CRUD {
    fun <T> create(data: T)
    fun <T> read(id: Int) : T
    fun update()
    fun delete()
}