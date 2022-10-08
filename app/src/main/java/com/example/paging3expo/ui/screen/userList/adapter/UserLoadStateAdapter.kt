package com.example.paging3expo.ui.screen.userList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3expo.databinding.NetworkStateItemBinding

class UserLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<UserLoadStateAdapter.LoadStateViewHolder>(){

    class LoadStateViewHolder(
        val binding: NetworkStateItemBinding
    ):RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.apply {
            binding.apply {
                progressBarItem.isVisible = loadState is LoadState.Loading
                errorMsgItem.isVisible = loadState is LoadState.Error
                binding.retryBtn.isVisible = loadState is LoadState.Error
                if (loadState is LoadState.Error){
                    errorMsgItem.text = loadState.error.localizedMessage
                }
                retryBtn.setOnClickListener {
                    retry.invoke()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            NetworkStateItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }
}