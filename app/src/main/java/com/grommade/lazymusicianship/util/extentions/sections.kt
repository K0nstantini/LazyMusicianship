package com.grommade.lazymusicianship.util.extentions

import com.grommade.lazymusicianship.data.entity.Section

fun List<Section>.hasNext(section: Section) =
    any { sameLevel(it, section) && it.order > section.order }

fun List<Section>.hasPrev(section: Section) =
    any { sameLevel(it, section) && it.order < section.order }

private fun sameLevel(s1: Section, s2: Section) =
    s1.parentId == s2.parentId

fun List<Section>.sameLevel(): Boolean {
    if (isEmpty()) {
        return true
    }
    val parentId = first().parentId
    return all { it.parentId == parentId }
}

fun List<Section>.hasChildren(section: Section) =
    any { it.parentId == section.id }

fun List<Section>.children(section: Section) =
    filter { it.parentId == section.id }


fun List<Section>.getParent(section: Section) =
    find { it.id == section.parentId }