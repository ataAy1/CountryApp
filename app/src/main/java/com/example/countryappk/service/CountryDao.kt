package com.example.countryappk.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.countryappk.data.entity.Country

@Dao
interface CountryDao {

    //Data Access Object

    @Insert
    suspend fun insertAll(vararg countries: Country): List<Long>

    @Query("Select * from country")
    suspend fun getAllCountries(): List<Country>

    @Query("Select * from country Where uuid=:countryId")
    suspend fun getCountry(countryId: Int): Country

    @Query("Delete  from country")
    suspend fun deleteAllCountries()
}
