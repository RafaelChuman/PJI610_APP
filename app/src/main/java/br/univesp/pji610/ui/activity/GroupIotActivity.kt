package br.univesp.pji610.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.R
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.GroupIoT
import br.univesp.pji610.databinding.ActivityGroupIotBinding
import br.univesp.pji610.extensions.RedirectTo
import br.univesp.pji610.ui.recyclerview.GroupIotRecycleView
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class GroupIotActivity : AuthBaseActivity() {

    private val adapter = GroupIotRecycleView(context = this)

    private val binding by lazy {
        ActivityGroupIotBinding.inflate(layoutInflater)
    }

    private val groupIoTDao by lazy {
        DataSource.instance(this).groupIoTDAO()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_group_iot)

        configRecyclerView()

        setFab()

        lifecycleScope.launch {
            launch {
                user.filterNotNull()
                    .collect { usr ->
                        getAllGroupIoTOfUser(usr.id)
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


    private suspend fun getAllGroupIoTOfUser(userId: String) {
        groupIoTDao.getAllByUser(userId).collect { groupIot ->
            adapter.update(groupIot)
        }
    }


    private fun setFab() {
        val fab = binding.activityGroupIotFab
        fab.setOnClickListener {
            RedirectTo(GroupIotMgmtActivity::class.java)
        }
    }

    private fun configRecyclerView() {
        binding.activityGroupIotRecyclerview.adapter = adapter
        adapter.groupIotOnClickEvent = {
            val intent = Intent(
                this,
                GroupIoT::class.java
            ).apply {
                putExtra(GROUP_IOT_ID, it.id)
            }
            startActivity(intent)
        }
    }
}