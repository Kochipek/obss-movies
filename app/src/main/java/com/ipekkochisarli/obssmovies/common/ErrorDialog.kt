package com.ipekkochisarli.obssmovies.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.ipekkochisarli.obssmovies.databinding.DialogErrorBinding

class ErrorDialog(
    private val message: String,
    private val onButtonClick: (() -> Unit)? = null,
) : DialogFragment() {
    private var _binding: DialogErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvErrorMessage.text = message
        binding.btnError.setOnClickListener {
            dismiss()
            onButtonClick?.invoke()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(
                android.graphics.Color.TRANSPARENT
                    .toDrawable(),
            )
        }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isDialogShown = false
    }

    companion object {
        private var isDialogShown = false
        const val TAG = "ErrorDialog"

        fun show(
            fragmentManager: androidx.fragment.app.FragmentManager,
            message: String,
            onButtonClick: (() -> Unit)? = null,
        ) {
            if (!isDialogShown) {
                isDialogShown = true
                ErrorDialog(message, onButtonClick).show(fragmentManager, TAG)
            }
        }
    }
}
