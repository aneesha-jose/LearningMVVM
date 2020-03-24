package com.aneeshajose.trending.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by Aneesha Jose on 2020-03-22.
 * Base Dao with CRUD methods for Room Dao implementations
 *
 */

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: List<T>)

    @Update
    fun update(vararg obj: T)

    @Delete
    fun delete(vararg obj: T)
}