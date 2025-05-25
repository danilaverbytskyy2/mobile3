package com.example.app3fragment.retro

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetroBase {
    companion object {
        private const val ADR = "http://localhost:8080/"

        val RFIT_SECTOR: LabelRetro by lazy {
            Retrofit.Builder().baseUrl(ADR).addConverterFactory(JacksonConverterFactory.create()).build().create(LabelRetro::class.java)
        }

        val RFIT_COMPANY: ArtistRetro by lazy {
            Retrofit.Builder().baseUrl(ADR).addConverterFactory(JacksonConverterFactory.create()).build().create(ArtistRetro::class.java)
        }

        val RFIT_PROGRAM: FanRetro by lazy {
            Retrofit.Builder().baseUrl(ADR).addConverterFactory(JacksonConverterFactory.create()).build().create(FanRetro::class.java)
        }
    }
}