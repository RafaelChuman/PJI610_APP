package br.univesp.pji610.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.R
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.IoT_GroupIoT
import br.univesp.pji610.databinding.ActivityIotActivityBinding
import br.univesp.pji610.extensions.RedirectTo
import br.univesp.pji610.ui.recyclerview.IoTRecycleView
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class IoTActivity : AuthBaseActivity() {

    private val adapter = IoTRecycleView(context = this)
    private val binding by lazy {
        ActivityIotActivityBinding.inflate(layoutInflater)
    }
    private val ioTDao by lazy {
       DataSource.instance(this).iotTDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_iot_activity)

        configRecyclerView()
        configuraFab()
        lifecycleScope.launch {
            launch {
                user.filterNotNull()
                    .collect { usr ->
                        getAllIoTOfUser(usr.id)
                    }
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.iot_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.iot_menu_logout -> {
                lifecycleScope.launch {
                    logout()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun getAllIoTOfUser(userId: String) {
        ioTDao.getAllByUser(userId).collect { iot ->
            adapter.update(iot)
        }
    }

    private fun configuraFab() {
        val fab = binding.activityIotActivityFloatingButton
        fab.setOnClickListener {
            RedirectTo(FormNotaActivity::class.java)
        }
    }



    private fun configRecyclerView() {
        val recyclerView = binding.activityIotActivityRecyclerView
        recyclerView.adapter = adapter
        adapter.ioTOnClickEvent = {
            val intent = Intent(
                this,
                IoT_GroupIoT::class.java
            ).apply {
                putExtra(IOT_ID, it.ioT.id)
            }
            startActivity(intent)
        }
    }

}