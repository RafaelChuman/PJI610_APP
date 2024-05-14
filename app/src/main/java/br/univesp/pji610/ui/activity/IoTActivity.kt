package br.univesp.pji610.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.databinding.ActivityIotBinding
import br.univesp.pji610.extensions.RedirectTo
import br.univesp.pji610.ui.recyclerview.IoTRecycleView
import kotlinx.coroutines.launch

class IoTActivity : AuthBaseActivity() {

    private val binding by lazy {
        ActivityIotBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        IoTRecycleView(this)
    }

    private val ioTDao by lazy {
        DataSource.instance(this).iotTDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        configRecyclerView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getAllIoTOfUser("")
            }
        }
    }

//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.iot_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.iot_menu_logout -> {
//                lifecycleScope.launch {
//                    logout()
//                }
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private suspend fun getAllIoTOfUser(userId: String) {
        ioTDao.getAll()
            .collect { items ->
                binding.activityIotActivityTextView.visibility =
                    if (items.isEmpty()) {
                        binding.activityIotActivityRecyclerView.visibility = GONE
                        VISIBLE
                    } else {
                        binding.activityIotActivityRecyclerView.visibility = VISIBLE
                        adapter.update(items)
                        GONE
                    }
            }
    }

    fun setFab(view: View) {
        RedirectTo(IotMgmtActivity::class.java)
    }

    private fun configRecyclerView() {
        binding.activityIotActivityRecyclerView.adapter = adapter
        adapter.iotOnClickEvent = { iot ->
            RedirectTo(IotMgmtActivity::class.java) {
                putExtra(IOT_ID, iot.id)
            }
        }
    }

}