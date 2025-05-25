package com.example.app3fragment.retro

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetroBase {
    companion object {
        private const val ADR = "http://localhost:8080/"

        val RFIT_SECTOR: SectorRetro by lazy {
            Retrofit.Builder().baseUrl(ADR).addConverterFactory(JacksonConverterFactory.create()).build().create(SectorRetro::class.java)
        }

        val RFIT_COMPANY: CompanyRetro by lazy {
            Retrofit.Builder().baseUrl(ADR).addConverterFactory(JacksonConverterFactory.create()).build().create(CompanyRetro::class.java)
        }

        val RFIT_PROGRAM: ProgramRetro by lazy {
            Retrofit.Builder().baseUrl(ADR).addConverterFactory(JacksonConverterFactory.create()).build().create(ProgramRetro::class.java)
        }
    }
}