package com.example.app3fragment.fragments.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.app3fragment.R
import com.example.app3fragment.database.fan.Fan
import com.example.app3fragment.database.fan.FanUpdateRequest
import com.example.app3fragment.retro.RetroBase
import com.example.app3fragment.viewmodels.edit.ProgramEditViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_EDIT_ID = "ARG_EDIT_ID"
private const val ARG_COMPANY_ID = "ARG_COMPANY_ID"

class FanProgramEdit : Fragment() {
    private var editId: Int = -1
    private var companyId: Int = -1
    private lateinit var programEditViewModel: ProgramEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            editId = it.getInt(ARG_EDIT_ID)
            companyId = it.getInt(ARG_COMPANY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = inflater.inflate(R.layout.fragment_fan_edit, container, false)
        this.programEditViewModel = ViewModelProvider(this)[ProgramEditViewModel::class.java]
        return binding
    }

    private suspend fun loadAndFillProgramData(programId: Int, nameInput: EditText, descInput: EditText, phoneInput: EditText) {
        try {
            val program = RetroBase.RFIT_PROGRAM.getFanById(programId)
            withContext(Dispatchers.Main) {
                program.let {
                    nameInput.setText(it.name)
                    descInput.setText(it.description)
                    phoneInput.setText(it.developerPhone)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Err", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        params.setMargins(0, 0, 0, 0)
        view.layoutParams = params

        val nameInput = view.findViewById<EditText>(R.id.prog_name_in)
        val descInput = view.findViewById<EditText>(R.id.prog_desc_in)
        val phoneInput = view.findViewById<EditText>(R.id.prog_phone_in)
        val saveBtn = view.findViewById<Button>(R.id.buttonPrEntry)
        val cancelBtn = view.findViewById<Button>(R.id.buttonPrCancel)

        lifecycleScope.launch {
            if (editId > 0) {
                loadAndFillProgramData(editId, nameInput, descInput, phoneInput)
            }
        }

        cancelBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        saveBtn.setOnClickListener {
            val name = nameInput.text.toString()
            if (name.isBlank()) {
                Toast.makeText(context, "Enter Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (editId > 0) {
                this.programEditViewModel.updateProgram(FanUpdateRequest(
                    id = editId,
                    name = name,
                    description = descInput.text.toString(),
                    developerPhone = phoneInput.text.toString()
                )
                )
            } else {
                this.programEditViewModel.addProgram(Fan(
                    name = name,
                    description = descInput.text.toString(),
                    developerPhone = phoneInput.text.toString(),
                    artistId = companyId
                )
                )
            }
            Toast.makeText(context, "Action", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(editId: Int, companyId: Int) =
            FanProgramEdit().apply {
                arguments = Bundle().apply {
                    putInt(ARG_EDIT_ID, editId)
                    putInt(ARG_COMPANY_ID, companyId)
                }
            }
    }
}