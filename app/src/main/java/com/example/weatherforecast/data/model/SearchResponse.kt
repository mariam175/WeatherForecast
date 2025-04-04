package com.example.weatherforecast.data.model

sealed class SearchResponse() {
    data object Loading:SearchResponse()
    data class Success(val data:List<SearchCity>):SearchResponse()
    data class Failure(val error:Throwable):SearchResponse()
}