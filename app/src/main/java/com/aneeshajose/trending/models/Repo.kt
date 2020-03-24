package com.aneeshajose.trending.models

import android.os.Parcelable
import androidx.room.Entity
import com.aneeshajose.trending.common.TABLE_REPOSITORY
import kotlinx.android.parcel.Parcelize

/**
 * Created by Aneesha Jose on 2020-03-22.
 * The Repository object with relevant information about it.
 */
@Parcelize
@Entity(tableName = TABLE_REPOSITORY)
data class Repo(
    val author: String?,
    val name: String?,
    val avatar: String?,
    val description: String?,
    val language: String?,
    val languageColor: String?,
    val stars: Long?,
    val forks: Long?,
    val createdDate: Long = System.currentTimeMillis()
) : Parcelable