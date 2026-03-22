package org.kekmacska.gamelibrary.providers

object Validators{
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    val passwordRegex = """^(?=.*[A-Za-z]).{8,}$""".toRegex()
}