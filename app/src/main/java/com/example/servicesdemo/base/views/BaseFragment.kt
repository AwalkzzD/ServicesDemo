package com.example.servicesdemo.base.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.servicesdemo.base.utils.DialogUtils
import com.example.servicesdemo.base.viewmodel.BaseViewModel
import com.example.servicesdemo.base.viewmodel.ViewModelFactory

abstract class BaseFragment<VB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes private val layoutId: Int, private val viewModelClass: Class<VM>
) : Fragment() {

    private lateinit var binding: VB
    private lateinit var viewModel: VM

    protected val fragmentBinding: VB get() = binding
    protected val fragmentViewModel: VM get() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel()
        setUpView()

    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModel(): VM {
        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(
                requireActivity(), ViewModelFactory.getInstance(requireActivity().application)
            )[viewModelClass]
        }
        return viewModel
    }

    fun getActivityViewModel(): BaseViewModel {
        return ((activity as BaseActivity<*, *>).getViewModel())
    }

    fun showToast(message: String, duration: Int) {
        (activity as BaseActivity<*, *>).showToast(message, duration)
    }

    fun handleProgressDialog() {
        val progressDialog = DialogUtils.getProgressDialog(requireActivity())

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        }
    }

    open fun setUpView() = Unit

}