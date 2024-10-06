package com.example.countryappk.service

import com.example.countryappk.data.entity.Country
import io.reactivex.Single
import retrofit2.http.GET

interface CountryApi {


    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")

    fun getCountries():Single<List<Country>>
}