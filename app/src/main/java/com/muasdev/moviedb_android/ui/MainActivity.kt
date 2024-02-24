package com.muasdev.moviedb_android.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.muasdev.moviedb_android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val listGenre = listOf("28", "199", "200", "8628632873")

        binding.apply {
            btnFilterTest.setOnClickListener {
                viewModel.onEvent(MainEvent.FilterMoviesByGenre(listGenre.randomOrNull()))
            }
        }

        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {  state ->
                    if (state.isGenresLoading) {
                        //
                    }
                    if (state.isDiscoverLoading) {
                        //
                    }
                    if (state.genres != null) {
                        //
                    }
                    if (!state.errorMessage.isNullOrEmpty()) {
                        Toast.makeText(this@MainActivity, state.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    if (state.discoverMovies != null) {
                        //
                    }
                }
            }
        }
    }
}