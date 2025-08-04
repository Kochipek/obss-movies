package com.ipekkochisarli.obssmovies.common

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.databinding.ViewCustomSearchBinding

class CustomSearchView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : FrameLayout(context, attrs, defStyleAttr) {
        private val binding: ViewCustomSearchBinding =
            ViewCustomSearchBinding.inflate(
                LayoutInflater.from(context),
                this,
                true,
            )

        var onQueryTextChangeListener: ((String) -> Unit)? = null
        var onSearchActionListener: ((String) -> Unit)? = null
        var onCancelClickListener: (() -> Unit)? = null

        init {
            attrs?.let {
                val a = context.obtainStyledAttributes(it, R.styleable.CustomSearchView)

                val hint = a.getString(R.styleable.CustomSearchView_CustomSearchView_hint)
                val textColor =
                    a.getColor(R.styleable.CustomSearchView_CustomSearchView_textColor, Color.WHITE)
                val hintColor =
                    a.getColor(R.styleable.CustomSearchView_CustomSearchView_hintColor, Color.GRAY)

                a.recycle()

                hint?.let { binding.edtSearch.hint = it }
                binding.edtSearch.setTextColor(textColor)
                binding.edtSearch.setHintTextColor(hintColor)
            }

            setupListeners()
        }

        private fun setupListeners() {
            with(binding) {
                imgClean.setOnClickListener {
                    edtSearch.text?.clear()
                    setBorderWhenFocus(false)
                    tvCancel.isVisible = false
                }

                tvCancel.setOnClickListener {
                    edtSearch.text?.clear()
                    setBorderWhenFocus(false)
                    tvCancel.isVisible = false
                    onCancelClickListener?.invoke()
                }

                edtSearch.setOnFocusChangeListener { _, hasFocus ->
                    setBorderWhenFocus(hasFocus)
                    if (!hasFocus) {
                        tvCancel.isVisible = false
                    } else if (edtSearch.text?.isNotEmpty() == true) {
                        tvCancel.isVisible = true
                    }
                }

                edtSearch.doAfterTextChanged { text ->
                    val hasText = !text.isNullOrEmpty()
                    imgClean.isVisible = hasText
                    tvCancel.isVisible = hasText && edtSearch.isFocused

                    onQueryTextChangeListener?.invoke(text.toString())
                }

                edtSearch.setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                    ) {
                        val query = edtSearch.text.toString()
                        if (query.isNotBlank()) {
                            onSearchActionListener?.invoke(query)
                            edtSearch.clearFocus()
                            tvCancel.isVisible = false
                            setBorderWhenFocus(false)
                        }
                        true
                    } else {
                        false
                    }
                }
            }
        }

        private fun setBorderWhenFocus(isFocus: Boolean) {
            binding.llSearch.setBackgroundResource(
                if (isFocus) R.drawable.search_border else R.drawable.bg_search,
            )
        }
    }
