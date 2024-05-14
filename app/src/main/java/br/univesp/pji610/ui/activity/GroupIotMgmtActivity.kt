package br.univesp.pji610.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.R
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.GroupIoT
import br.univesp.pji610.database.model.RescueGroup
import br.univesp.pji610.databinding.ActivityGroupIotMgmtBinding
import br.univesp.pji610.extensions.ToastMessage
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GroupIotMgmtActivity : AuthBaseActivity() {

    private var groupIotId: String? = null

    private val groupIotDAO by lazy {
        DataSource.instance(this).groupIoTDAO()
    }

    private val rescueGroupDAO by lazy {
        DataSource.instance(this).rescueGroupDAO()
    }

    private val binding by lazy {
        ActivityGroupIotMgmtBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.activityGroupIotMgmtToolbar)

        setContentView(binding.root)

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
        groupIotId = intent.getStringExtra(GROUP_IOT_ID)
    }


    private suspend fun getGroupIotFromDataSource() {
        groupIotId?.let {
            groupIotDAO.getById(it)
                ?.filterNotNull()
                ?.collect { item ->
                    groupIotId = item.id
                    binding.activityGroupIotMgmtEditTextName.setText(item.name)
                    binding.activityGroupIotMgmtEditTextTemperature.setText(item.temperature.toString())
                    binding.activityGroupIotMgmtEditTextHumidity.setText(item.humidity.toString())
                    binding.activityGroupIotMgmtEditTextNoBreak.setText(item.noBreak.toString())
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

        val groupIoTCreated = createNewGroupIot()

        lifecycleScope.launch {
            groupIotDAO.save(groupIoTCreated)
            Log.i("Group IoT Mgmt", "Save Group IoT: $groupIoTCreated")

            val rescueGroup = createNewRescueGroup(groupIoTCreated)
            Log.i("Group IoT Mgmt", "Save Rescue Group: $rescueGroup")

            finish()
        }
    }


    private fun createNewGroupIot(): GroupIoT {
        val name = binding.activityGroupIotMgmtEditTextName.text.toString()
        val humidity = binding.activityGroupIotMgmtEditTextHumidity.text.toString()
        val temperature = binding.activityGroupIotMgmtEditTextTemperature.text.toString()
        val noBreak = binding.activityGroupIotMgmtEditTextNoBreak.text.toString()
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


    private fun createNewRescueGroup(groupIotCreated: GroupIoT): RescueGroup {
        val groupIotIdCreated = groupIotCreated.id


        var rescueGroup = RescueGroup(
            userId = "",
            groupId = "",
            createdAt = LocalDateTime.now().toString()
        )

        Log.i("Group IoT Mgmt", "createNewRescueGroup: $user")
        Log.i("Group IoT Mgmt", "createNewRescueGroup: ${user.value?.toString()}")

        user.value?.let {
            rescueGroup = RescueGroup(
                userId = it.id,
                groupId = groupIotIdCreated,
                createdAt = LocalDateTime.now().toString()
            )
        }


        return rescueGroup
    }
}