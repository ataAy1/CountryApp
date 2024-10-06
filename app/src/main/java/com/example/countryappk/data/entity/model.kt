package com.example.countryappk.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Country(

    @ColumnInfo("name")
    @SerializedName(value = "name")
    val countryName: String?,

    @ColumnInfo("region")
    @SerializedName(value = "region")
    val countryRegion: String?,

    @ColumnInfo("capital")
    @SerializedName(value = "capital")
    val countryCapital: String?,

    @ColumnInfo("currency")
    @SerializedName(value = "currency")
    val countryCurrency: String?,

    @ColumnInfo("language")
    @SerializedName(value = "language")
    val countryLanguage: String?,

    @ColumnInfo("flag")
    @SerializedName(value = "flag")
    val imageUrl: String? )

{
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}



