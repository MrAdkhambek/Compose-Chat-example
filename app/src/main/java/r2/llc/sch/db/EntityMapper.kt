package r2.llc.sch.db


interface EntityMapper<T, R> {
    fun mapTR(from: T): R
    fun mapRT(from: R): T
}