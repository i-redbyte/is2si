package ru.is2si.sisi.base.network

interface NetworkSettings {
    val baseUrl: String

    val readTimeout: Long
    val connectionTimeout: Long

    val detectLocationTimeout: Long

    val logRequests: Boolean
}