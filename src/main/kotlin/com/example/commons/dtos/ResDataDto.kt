package com.example.commons.dtos

import kotlinx.serialization.Serializable

@Serializable
sealed class ResDataDto<out TEntity> {
    @Serializable
    data class Single<TEntity>(val entity: TEntity) : ResDataDto<TEntity>()

    @Serializable
    data class Multiple<TEntity>(val entities: List<TEntity>) : ResDataDto<TEntity>()
}
