package com.pahnal.mystoryapp.presentation.story_features.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pahnal.mystoryapp.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    class LoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.retryButton.setOnClickListener { retry.invoke() }
            }

        fun bind(loadState: LoadState) {
            with(binding) {
                if (loadState is LoadState.Error) {
                    errorMsg.text = loadState.error.localizedMessage
                    retryButton.isVisible = true
                }

                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible = loadState is LoadState.Error

            }
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }
}