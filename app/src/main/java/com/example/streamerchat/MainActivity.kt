package com.example.streamerchat

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.streamerchat.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val streamViewModel: StreamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe LiveData untuk update UI
        streamViewModel.responseLiveData.observe(this) { response ->
            //Log.d("CEK1", "Response received: $response")
            binding.responseTextView.text = response
        }

        // Ketika tombol start diklik, mulai streaming
        binding.startButton.setOnClickListener {
            binding.responseTextView.text = ""
            streamViewModel.streamResponse("Berikan tips kesehatan diabetes")
        }
    }
}