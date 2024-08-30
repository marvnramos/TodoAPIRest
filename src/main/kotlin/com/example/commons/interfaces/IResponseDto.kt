package com.example.commons.interfaces

interface IResponseDto<T> {
    val status: String
    val message: String
    val data: T
}