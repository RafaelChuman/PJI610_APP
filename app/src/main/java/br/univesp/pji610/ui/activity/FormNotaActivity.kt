package br.univesp.pji610.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.R
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.Nota
import br.univesp.pji610.databinding.ActivityFormNotaBinding
import br.univesp.pji610.extensions.loadImageFromPath
import br.univesp.pji610.repository.NotaRepository
import br.univesp.pji610.ui.dialog.ImageDialog
import br.univesp.pji610.webclient.NotaWebClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FormNotaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormNotaBinding.inflate(layoutInflater)
    }
    private var imagem: MutableStateFlow<String?> = MutableStateFlow(null)
    private val repository by lazy {
        NotaRepository(
            DataSource.instance(this).notaDao(),
            NotaWebClient()
        )
    }
    private var notaId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.activityFormNotaToolbar)
        configuraImagem()
        tentaCarregarIdDaNota()
        lifecycleScope.launch {
            launch {
                tentaBuscarNota()
            }
            launch {
                configuraCarregamentoDeImagem()
            }
        }

    }

    private suspend fun configuraCarregamentoDeImagem() {
        val imagemNota = binding.activityFormNotaImagem
        imagem.collect { imagemNova ->
            imagemNota.visibility =
                if (imagemNova.isNullOrBlank())
                    GONE
                else {
                    imagemNota.loadImageFromPath(imagemNova)
                    VISIBLE
                }
        }
    }

    private suspend fun tentaBuscarNota() {
        notaId?.let { id ->
            repository.buscaPorId(id)
                .filterNotNull()
                .collect { notaEncontrada ->
                    notaId = notaEncontrada.id
                    imagem.value = notaEncontrada.imagem
                    binding.activityFormNotaTitulo.setText(notaEncontrada.titulo)
                    binding.activityFormNotaDescricao.setText(notaEncontrada.descricao)
                }
        }

    }

    private fun tentaCarregarIdDaNota() {
        notaId = intent.getStringExtra(NOTA_ID)
    }

    private fun configuraImagem() {
        binding.activityFormNotaAdicionarImagem.setOnClickListener {
            ImageDialog(this)
                .show(imagem.value) { imageLoaded ->
                    binding.activityFormNotaImagem
                        .loadImageFromPath(imageLoaded)
                    imagem.value = imageLoaded
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.form_nota_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.form_nota_menu_salvar -> {
                salva()
            }

            R.id.form_nota_menu_remover -> {
                remove()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun remove() {
        lifecycleScope.launch {
            notaId?.let { id ->
                repository.remove(id)
            }
            finish()
        }
    }

    private fun salva() {
        val nota = criaNota()
        lifecycleScope.launch {
            repository.salva(nota)
            finish()
        }
    }

    private fun criaNota(): Nota {
        val titulo = binding.activityFormNotaTitulo.text.toString()
        val descricao = binding.activityFormNotaDescricao.text.toString()
        return notaId?.let { id ->
            Nota(
                id = id,
                titulo = titulo,
                descricao = descricao,
                imagem = imagem.value
            )
        } ?: Nota(
            titulo = titulo,
            descricao = descricao,
            imagem = imagem.value
        )
    }

}
