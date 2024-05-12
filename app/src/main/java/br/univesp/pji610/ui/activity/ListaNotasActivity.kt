package br.univesp.pji610.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.databinding.ActivityListaNotasBinding
import br.univesp.pji610.extensions.RedirectTo
import br.univesp.pji610.repository.NotaRepository
import br.univesp.pji610.ui.recyclerview.adapter.ListaNotasAdapter
import br.univesp.pji610.webclient.NotaWebClient
import kotlinx.coroutines.launch

class ListaNotasActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityListaNotasBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        ListaNotasAdapter(this)
    }
    private val repository by lazy {
        NotaRepository(
            DataSource.instance(this).notaDao(),
            NotaWebClient()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraFab()
        configuraRecyclerView()
        lifecycleScope.launch {
            launch {
                sincroniza()
            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                buscaNotas()
            }
        }
    }

    private suspend fun sincroniza() {
        repository.sincroniza()
    }

    private fun configuraFab() {
        binding.activityListaNotasFab.setOnClickListener {
            Intent(this, FormNotaActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun configuraRecyclerView() {
        binding.activityListaNotasRecyclerview.adapter = adapter
        adapter.quandoClicaNoItem = { nota ->
            RedirectTo(FormNotaActivity::class.java) {
                putExtra(NOTA_ID, nota.id)
            }
        }
    }

    private suspend fun buscaNotas() {
        repository.buscaTodas()
            .collect { notasEncontradas ->
                binding.activityListaNotasMensagemSemNotas.visibility =
                    if (notasEncontradas.isEmpty()) {
                        binding.activityListaNotasRecyclerview.visibility = GONE
                        VISIBLE
                    } else {
                        binding.activityListaNotasRecyclerview.visibility = VISIBLE
                        adapter.atualiza(notasEncontradas)
                        GONE
                    }
            }
    }
}