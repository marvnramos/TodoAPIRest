package com.example.commons.models

import kotlinx.serialization.Serializable

@Serializable
sealed class ResData<out TEntity> {
    @Serializable
    data class Single<TEntity>(val entity: TEntity) : ResData<TEntity>()

    @Serializable
    data class Multiple<TEntity>(val entities: List<TEntity>) : ResData<TEntity>()
}
