package com.hunterwilhelm.offsetclocks

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.edit_dialog.view.*
import java.util.*


class EditDialog(private val clockName: String?) : DialogFragment() {

    companion object {

        const val TAG = "DialogWithData"

    }

    private lateinit var editText: EditText

    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_dialog, container, false)
    }


    private fun setupView(view: View) {
        clockName ?: return
        view.findViewById<TextInputEditText>(R.id.edit_dialog_edit_text).setText(clockName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        editText = view.findViewById(R.id.edit_dialog_edit_text)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        setupClickListeners(view)
        editText.requestFocus()
        editText.post {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    showKeyboard(editText)
                }
            }, 1000)
        }
    }

    private fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    private fun closeKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive)
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setGravity(Gravity.BOTTOM)

    }

    private fun setupClickListeners(view: View) {
        view.btnSubmit.setOnClickListener {
            viewModel.sendName(view.edit_dialog_edit_text.text.toString())
            dismiss()
        }
    }

}