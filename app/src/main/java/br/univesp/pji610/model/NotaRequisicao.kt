package br.univesp.pji610.webclient.model

data class NotaRequisicao(
    val titulo: String,
    val descricao: String,
    val imagem: String? = null
)