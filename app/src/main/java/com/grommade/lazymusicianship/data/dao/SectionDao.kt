package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import com.grommade.lazymusicianship.data.entity.Section

@Dao
abstract class SectionDao: EntityDao<Section>() {
}