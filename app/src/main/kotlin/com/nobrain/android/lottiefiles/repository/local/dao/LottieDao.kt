package com.nobrain.android.lottiefiles.repository.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.nobrain.android.lottiefiles.repository.local.entities.Lottie
import io.reactivex.Flowable
import io.reactivex.Observable


@Dao
interface LottieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg item: Lottie)

    @Delete
    fun delete(vararg item: Lottie)

    @Query("SELECT * FROM lottie")
    fun getAll(): LiveData<List<Lottie>>

    @Query("SELECT * FROM lottie ORDER BY id desc")
    fun getAllOnRx(): Flowable<List<Lottie>>

    @Query("SELECT * FROM lottie WHERE starred = 1")
    fun getStarred(): List<Lottie>

    @Query("SELECT COUNT(*) FROM lottie WHERE id = :id")
    fun getCount(id:String) : Int

    @Query("SELECT * FROM lottie WHERE title LIKE :keyword")
    fun query(keyword: String) : Flowable<List<Lottie>>
}