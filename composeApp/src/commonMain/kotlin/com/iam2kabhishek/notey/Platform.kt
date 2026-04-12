package com.iam2kabhishek.notey

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform