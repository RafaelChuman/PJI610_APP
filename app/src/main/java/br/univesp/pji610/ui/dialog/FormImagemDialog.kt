package br.univesp.pji610.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.univesp.pji610.databinding.FormImagemBinding
import br.univesp.pji610.extensions.loadImageFromPath

class FormImagemDialog(private val context: Context) {

    fun mostra(
        urlPadrao: String? = null,
        quandoImagemCarragada: (imagem: String) -> Unit
    ) {
        FormImagemBinding.inflate(LayoutInflater.from(context)).apply {

                urlPadrao?.let {
                    formImagemImageview.loadImageFromPath(it)
                    formImagemUrl.setText(it)
                }

                formImagemBotaoCarregar.setOnClickListener {
                    val url = formImagemUrl.text.toString()
                    formImagemImageview.loadImageFromPath(url)
                }

                AlertDialog.Builder(context)
                    .setView(root)
                    .setPositiveButton("Confirmar") { _, _ ->
                        val url = formImagemUrl.text.toString()
                        quandoImagemCarragada(url)
                    }
                    .setNegativeButton("Cancelar") { _, _ ->

                    }
                    .show()
            }


    }

}