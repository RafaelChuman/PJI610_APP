package br.univesp.pji610.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.R
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.GroupIoT
import br.univesp.pji610.databinding.ActivityGroupIotMgmtBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GroupIotMgmtActivity : AppCompatActivity() {

    private var groupIotId: String? = null

    private val groupIotDAO by lazy {
        DataSource.instance(this).groupIoTDAO()
    }

    private val binding by lazy {
        ActivityGroupIotMgmtBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_group_iot_mgmt)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getGroupIotId()

        lifecycleScope.launch {
            launch {
                getGroupIotFromDataSource()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_iot_mgmt_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.group_iot_mgmt_menu_save -> {
                save()
            }

            R.id.group_iot_mgmt_menu_remove -> {
                remove()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getGroupIotId() {
        groupIotId = intent.getStringExtra(NOTA_ID)
    }


    private suspend fun getGroupIotFromDataSource() {
        groupIotId?.let {
            groupIotDAO.getById(it)
                ?.filterNotNull()
                ?.collect { item ->
                    groupIotId = item.id
                    binding.activityGroupIotEditTextName.setText(item.name)
                }
        }

    }


    private fun remove() {
        lifecycleScope.launch {
            groupIotId?.let { id ->
                groupIotDAO.remove(id)
            }
            finish()
        }
    }

    private fun save() {

        val groupIoT = createNewGroupIot()

        lifecycleScope.launch {
            groupIotDAO.save(groupIoT)
            finish()
        }
    }


    private fun createNewGroupIot(): GroupIoT {
        val name = binding.activityGroupIotEditTextName.text.toString()
        val humidity = binding.activityGroupIotEditTextHumidity.text.toString()
        val temperature = binding.activityGroupIotEditTextTemperature.text.toString()
        val noBreak = binding.activityGroupIotEditTextNoBreak.text.toString()
        return groupIotId?.let { groupId ->
            GroupIoT(
                id = groupId,
                name = name,
                humidity = humidity.toDouble(),
                temperature = temperature.toDouble(),
                noBreak = noBreak.toDouble(),
                createdAt = LocalDateTime.now().toString()
            )
        } ?: GroupIoT(
            name = name,
            humidity = humidity.toDouble(),
            temperature = temperature.toDouble(),
            noBreak = noBreak.toDouble(),
            createdAt = LocalDateTime.now().toString()
        )
    }
}