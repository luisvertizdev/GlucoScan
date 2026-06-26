package com.luisvertiz.nutriscan.error

sealed class ErrorHandler: Exception() {
    class UnknownError : ErrorHandler()
    class EmailNotVerifiedError : ErrorHandler()
    class InvalidCredentialsError : ErrorHandler()
    class InvalidUserError : ErrorHandler()
    class EmailAlreadyRegisteredError : ErrorHandler()
}