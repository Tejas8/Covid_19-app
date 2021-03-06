package com.personal.covid_19.utils;

import com.personal.covid_19.model.*;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface utils {
    @GET("all")
    Observable<allcases> allcases();
    @GET("countries?sort=cases")
    Observable<List<countrydata>> allcountrydata();
    @GET("top-headlines?language=en&q=corona&apiKey=d658411b9a6c41d4b52e9ffe93b46d11&pagesize=100")
    Observable<newsresponse> newsresponse();
    @GET("top-headlines?country=in&q=corona&apiKey=1a54f1fa7c7741d28b862ba1a32875ef")
    Observable<newsresponse> indiannewsresponse();
    @GET("data.json")
    Observable<india_Data> allindiadata();
    @GET("states_daily.json")
    Observable<dailyrjstatus> rajasthandata();
    @GET("resources/resources.json")
    Observable<responseresource> resourceresponse();


}
