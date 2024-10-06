package com.example.countryappk.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countryappk.data.entity.Country
import com.example.countryappk.service.CountryApi
import com.example.countryappk.service.CountryApiService
import com.example.countryappk.service.CountryDatabase
import com.example.countryappk.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async.Schedule

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryApiService = CountryApiService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData() {
        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromSqlite()
        } else {
            getDataFromApi()

        }
        /* val country = Country("Turkey", "Asia", "Ankara", "Try", "Turkish", "www.ss.com")
         val countr2 = Country("Amer", "ZA", "ASD", "ADSQQ", "Turkish", "www.ss.com")
         val country3 = Country("İtalya", "DA", "ASD", "QQ", "Turkish", "www.ss.com")

         val countryList = arrayListOf<Country>(country, countr2, country3)
         countries.value = countryList
         countryError.value = false
         countryLoading.value = false*/
    }


    fun refreshFromAPI() {
        getDataFromApi()
    }

    private fun getDataFromSqlite() {
        countryLoading.value = true

        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(), "Countries from sqlite", Toast.LENGTH_LONG).show()
        }
    }

    private fun getDataFromApi() {
        countryLoading.value = true

        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(t: List<Country>) {
                        storeSqlite(t)
                        /*countries.value=t
                            countryError.value=false
                            countryLoading.value=false
                            room öncesi
                            */
                        Toast.makeText(getApplication(), "Countries from API", Toast.LENGTH_LONG)
                            .show()


                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()

                    }

                }
                ))
    }

    private fun showCountries(countryList: List<Country>) {
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }

    private fun storeSqlite(list: List<Country>) {
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt()
                i = i + 1
            }
            showCountries(list)
        }

        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}

