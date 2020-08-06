package com.anna.money

fun getSnakeCaseName(name: String) =
    StringBuilder().apply {
        for (char in name) {
            if (char.isUpperCase()) {
                if (length > 0) append('_')
                append(char.toLowerCase())
            } else append(char)
        }
    }.toString()
