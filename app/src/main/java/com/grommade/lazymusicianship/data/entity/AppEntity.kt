package com.grommade.lazymusicianship.data.entity

interface AppEntity {
    val id: Long

    val isNew: Boolean
        get() = id == 0L
}