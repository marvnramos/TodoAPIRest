package com.example.commons.interfaces

import java.util.UUID

interface IEntityRepository<TEntity> {
    suspend fun getAll(): List<TEntity>
    suspend fun findById(id: UUID): TEntity
    suspend fun insert(entity: TEntity)
    suspend fun find(predicate: (TEntity) -> Boolean) : TEntity? //
    suspend fun replace(entity: TEntity)
    suspend fun delete(id: UUID)
}