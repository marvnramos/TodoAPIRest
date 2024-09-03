package com.example.commons.interfaces

import com.example.commons.models.ResData

interface IResponseDto<TData : ResData<*>> {
    val status: String
    val message: String
    val data: TData
}
