package org.kmp.playground.donate.button

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform