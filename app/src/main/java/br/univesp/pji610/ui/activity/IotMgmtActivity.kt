package br.univesp.pji610.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.R
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.IoT
import br.univesp.pji610.databinding.ActivityIotMgmtBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class IotMgmtActivity : AppCompatActivity() {

    private var iotID: String? = null

    private val iotDAO by lazy {
        DataSource.instance(this).iotTDAO()
    }

    private val groupIoTDAO by lazy {
        DataSource.instance(this).groupIoTDAO()
    }

    private val binding by lazy {
        ActivityIotMgmtBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_iot_mgmt)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        getIotID()
        lifecycleScope.launch {
            launch {
                getIotFromDataSource()
            }
            launch {
                setGroupOnSpinner()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.iot_mgmt_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.iot_mgmt_menu_save -> {
                save()
            }

            R.id.iot_mgmt_menu_remove -> {
                remove()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getIotID() {
        iotID = intent.getStringExtra(NOTA_ID)
    }


    private suspend fun getIotFromDataSource() {
        iotID?.let { id ->
            iotDAO.getById(id)
                ?.filterNotNull()
                ?.collect { iot ->
                    iotID = iot.id
                    binding.activityIotEditTextName.setText(iot.name)
                }
        }

    }

    private suspend fun setGroupOnSpinner() {
        val spinner = binding.activityIotMgmtSpinnerGroup
        val group = groupIoTDAO.getName()
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, group)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
    }


    private fun remove() {
        lifecycleScope.launch {
            iotID?.let { id ->
                iotDAO.remove(id)
            }
            finish()
        }
    }

    private fun save() {

        val iot = createNewIot()

        lifecycleScope.launch {
            iotDAO.save(iot)
            finish()
        }
    }


    private fun createNewIot(): IoT {
        val name = binding.activityIotEditTextName.text.toString()
        val groupId = binding.activityIotMgmtSpinnerGroup.id.toString()
        return iotID?.let { id ->
            IoT(
                id = id,
                name = name,
                groupId = groupId,
                createdAt = LocalDateTime.now().toString()
            )
        } ?: IoT(
            name = name,
            groupId = groupId,
            createdAt = LocalDateTime.now().toString()
        )
    }
}