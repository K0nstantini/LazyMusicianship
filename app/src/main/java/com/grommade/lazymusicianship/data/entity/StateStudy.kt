package com.grommade.lazymusicianship.data.entity

abstract class StateStudy {
    abstract val id: Long
    abstract val name: String
    abstract val considerBeat: Boolean
    abstract val considerTimes: Boolean
}