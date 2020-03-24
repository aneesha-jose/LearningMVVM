package com.aneeshajose.trending.localdata

import androidx.room.Dao
import androidx.room.Query
import com.aneeshajose.trending.base.BaseDao
import com.aneeshajose.trending.common.TABLE_REPOSITORY
import com.aneeshajose.trending.models.Repo

/**
 * Created by Aneesha Jose on 2020-03-24.
 */

@Dao
interface RepositoryDao : BaseDao<Repo>{

    @Query("SELECT * FROM $TABLE_REPOSITORY WHERE createdDate >= :validTimestamp")
    fun fetchValidRepos(validTimestamp : Long):List<Repo>

    @Query("DELETE FROM $TABLE_REPOSITORY")
    fun clearExistingData()
}