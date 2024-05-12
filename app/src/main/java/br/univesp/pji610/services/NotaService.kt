package br.univesp.pji610.webclient.services

import br.univesp.pji610.webclient.model.NotaRequisicao
import br.univesp.pji610.webclient.model.NotaResposta
import retrofit2.Response
import retrofit2.http.*

interface NotaService {

    @GET("notas")
    suspend fun buscaTodas(): List<NotaResposta>

    @PUT("notas/{id}")
    suspend fun salva(@Path("id") id: String,
              @Body nota: NotaRequisicao): Response<NotaResposta>

    @DELETE("notas/{id}")
    suspend fun remove(@Path("id") id: String): Response<Void>

}