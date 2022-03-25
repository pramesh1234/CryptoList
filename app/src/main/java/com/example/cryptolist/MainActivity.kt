package com.example.cryptolist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptolist.adapter.CryptoListAdapter
import com.example.cryptolist.databinding.ActivityMainBinding
import com.example.cryptolist.model.CryptoModel
import com.example.cryptolist.viewmodel.MainViewModel
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : MyAppCompatActivity() {
    lateinit var adapter: CryptoListAdapter
    val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpLoader(viewModel)
        adapter = CryptoListAdapter(this,ArrayList<CryptoModel>())
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getCryptoList()
        val delay:Long = 10000
        val period:Long = 10000
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                viewModel.getCryptoList()
            }
        }, delay, period)
    }

    override fun initObservers() {
        viewModel.cryptoListLiveData.observe(this) {
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
            adapter.updateList(it)
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it)
    }
}