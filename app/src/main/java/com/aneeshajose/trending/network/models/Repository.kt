package com.aneeshajose.trending.network.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Created by Aneesha Jose on 2020-03-22.
 * The Repository object with relevant information about it.
 */
@Parcelize
@Entity
data class Repository(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String?,
    val name: String?,
    val avatar: String?,
    val description: String?,
    val language: String?,
    val languageColor: String?,
    val stars: Long?,
    val forks: Long?
) : Parcelable