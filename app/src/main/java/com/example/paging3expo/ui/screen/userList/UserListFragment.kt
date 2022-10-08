package com.example.paging3expo.ui.screen.userList

import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3expo.databinding.FragmentUserListBinding
import com.example.paging3expo.ui.screen.userList.adapter.UserListAdapter
import com.example.paging3expo.ui.screen.userList.adapter.UserLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private lateinit var userListAdapter: UserListAdapter
    private lateinit var binding: FragmentUserListBinding
    private val viewModel: UserListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObserver()
        binding.scrollUp.setOnClickListener {
            lifecycleScope.launch {
                binding.recyclerView.scrollToPosition(0)
                delay(100)
                binding.scrollUp.toggle(false)
            }
        }
    }

    private fun initView() {
        userListAdapter = UserListAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = userListAdapter.withLoadStateFooter(
                footer = UserLoadStateAdapter{retry()}
            )
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val scrolledPostion =
                        (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()

                    if(scrolledPostion != null){
                        if (scrolledPostion >= 1){
                            binding.scrollUp.toggle(true)
                        }else{
                            binding.scrollUp.toggle(false)
                        }
                    }
                }
            }
            )
        }
    }

    private fun subscribeObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    uiState.user?.let { userListAdapter.submitData(it) }
                }
            }
        }
    }

    private fun retry() {
        userListAdapter.retry()
    }

}

fun View.toggle(show: Boolean) {
    val transition: Transition = Slide(Gravity.BOTTOM)
    transition.duration = 200
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup?, transition)
    this.isVisible = show
}