package org.text.note

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform