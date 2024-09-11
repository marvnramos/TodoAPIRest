package com.example.commons.dtos

interface IResponseDto<TData : ResDataDto<*>> {
    val status: String
    val message: String
    val data: TData
}
