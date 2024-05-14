package br.univesp.pji610.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.databinding.ActivityGroupIotBinding
import br.univesp.pji610.extensions.RedirectTo
import br.univesp.pji610.ui.recyclerview.GroupIotRecycleView
import kotlinx.coroutines.launch

class GroupIotActivity : AuthBaseActivity() {

    private val binding by lazy {
        ActivityGroupIotBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        GroupIotRecycleView(this)
    }

    private val groupIoTDao by lazy {
        DataSource.instance(this).groupIoTDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        configRecyclerView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getAllGroupIoTOfUser("")
            }
        }
    }


    private suspend fun getAllGroupIoTOfUser(userId: String) {
        groupIoTDao.getAll()
            .collect { items ->
                binding.activityGroupIotTextView.visibility =
                    if (items.isEmpty()) {
                        binding.activityGroupIotRecyclerview.visibility = GONE
                        VISIBLE
                    } else {
                        binding.activityGroupIotRecyclerview.visibility = VISIBLE
                        adapter.update(items)
                        GONE
                    }
            }
    }

    fun setFab(view: View) {
        RedirectTo(GroupIotMgmtActivity::class.java)
    }

    private fun configRecyclerView() {
        binding.activityGroupIotRecyclerview.adapter = adapter
        adapter.groupIotOnClickEvent = { groupIot ->
            RedirectTo(GroupIotMgmtActivity::class.java) {
                putExtra(GROUP_IOT_ID, groupIot.id)
            }
        }
    }
}