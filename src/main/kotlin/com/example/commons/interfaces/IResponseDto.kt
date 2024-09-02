package com.example.commons.interfaces

import kotlinx.serialization.Serializable

interface IResponseDto<TEntity> {
    val status: String
    val message: String
    val data: TEntity
}
