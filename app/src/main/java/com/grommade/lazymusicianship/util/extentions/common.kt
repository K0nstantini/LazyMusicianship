package com.grommade.lazymusicianship.util.extentions

fun String.toTime() =
    dropLast(3).toInt() * 60 + this.drop(3).toInt()

fun Int.toStrTime(): String {
    return (this / 60).toString().padStart(2, '0') + ':' +
            (this % 60).toString().padStart(2, '0')
}