package br.univesp.pji610.webclient

import br.univesp.pji610.webclient.services.NotaService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitInicializador {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.15.56:3333/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val notaService = retrofit.create(NotaService::class.java)

}