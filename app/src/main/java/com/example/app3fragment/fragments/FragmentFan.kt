package com.example.app3fragment.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app3fragment.R
import com.example.app3fragment.database.fan.Fan
import com.example.app3fragment.fragments.edit.FragmentProgramEdit
import com.example.app3fragment.viewmodels.FanViewModel
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity

private const val ARG_TITLE = "ARG_TITLE"
private const val ARG_COMPANY_ID = "ARG_COMPANY_ID"

class FanViewModelFactory(private val companyId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FanViewModel::class.java)) {
            return FanViewModel(companyId) as T
        }
        throw IllegalArgumentException("Error")
    }
}

class FragmentProgram : Fragment() {
    private lateinit var adapter: FanAdapter;
    private var title: String? = null
    private var companyId: Int = -1
    private lateinit var fanViewModel: FanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            companyId = it.getInt(ARG_COMPANY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = inflater.inflate(R.layout.fragment_programs, container, false)
        val factory = FanViewModelFactory(this.companyId)
        this.fanViewModel = ViewModelProvider(this, factory)[FanViewModel::class.java]
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbarPrograms)
        val toolbarTitle = view.findViewById<TextView>(R.id.menuProgramsTitle)
        toolbar.inflateMenu(R.menu.main_menu)
        val activity = this.requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbarTitle.text = this.title

        this.adapter = FanAdapter()
        this.requireActivity().addMenuProvider(LocalMenuProvider(this.requireActivity(), companyId, this.adapter, activity, this.requireContext(), this.fanViewModel), this.viewLifecycleOwner)

        //============================================

        val recyclerView = view.findViewById<RecyclerView>(R.id.programsView)
        recyclerView?.adapter = this.adapter
        recyclerView?.layoutManager = LinearLayoutManager(this.requireContext())
        this.fanViewModel.programs.observe(viewLifecycleOwner) { programs ->
            this.adapter.submitList(programs)
        }

        this.fanViewModel.fetch()
    }

    companion object {
        @JvmStatic
        fun newInstance(menuTitle: String, companyId: Int) =
            FragmentProgram().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, menuTitle)
                    putInt(ARG_COMPANY_ID, companyId)
                }
            }
    }

    class LocalMenuProvider(private val activityO: FragmentActivity, private val compId: Int, private val adapter: FanAdapter, private val activity: AppCompatActivity, private val context: Context, private val fanViewModel: FanViewModel) :
        MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu)
            val entryItem = menu.findItem(R.id.menu_entry)
            entryItem.setVisible(false)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_add -> {
                    val fragment = FragmentProgramEdit.newInstance(-1, companyId = compId)
                    activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, fragment).addToBackStack(null).commit()
                    true
                }
                R.id.menu_edit -> {
                    val selected = adapter.selectedPosition
                    if (selected in adapter.fans.indices) {
                        val prog = adapter.fans[selected]
                        val fragment = FragmentProgramEdit.newInstance(prog.id, prog.artistId)
                        activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, fragment).addToBackStack(null).commit()
                    } else {
                        Toast.makeText(context, "Choose an item", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.menu_delete -> {
                    if (this.adapter.selectedPosition >= 0 && this.adapter.selectedPosition < this.adapter.fans.size) {
                        fanViewModel.removeFan(this.adapter.fans[this.adapter.selectedPosition])
                    }
                    true
                }
                R.id.menu_entry -> {
                    val selected = adapter.selectedPosition
                    if (selected in adapter.fans.indices) {
                        val prog = adapter.fans[selected]
                        val fragment = FragmentProgramEdit.newInstance(prog.id, prog.artistId)
                        activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, fragment).addToBackStack(null).commit()
                    } else {
                        Toast.makeText(context, "Choose an item", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.menu_phone -> {
                    if (this.adapter.selectedPosition >= 0 && this.adapter.selectedPosition < this.adapter.fans.size) {
                        val progr = this.adapter.fans[this.adapter.selectedPosition];
                        if (!progr.developerPhone.isEmpty()) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                makePhoneCall(progr.developerPhone)
                            } else {
                                ActivityCompat.requestPermissions(activityO, arrayOf(Manifest.permission.CALL_PHONE), 1)
                            }
                        }
                    }
                    true
                }
                else -> false
            }
        }

        private fun makePhoneCall(phoneNum: String) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = "tel:$phoneNum".toUri()
            context.startActivity(intent)
        }
    }

    class FanAdapter() : RecyclerView.Adapter<FanAdapter.ProgramViewHolder>() {
        var fans = emptyList<Fan>()
        var selectedPosition = -1

        @SuppressLint("NotifyDataSetChanged")
        fun submitList(newList: List<Fan>) {
            this.fans = newList
            notifyDataSetChanged()
        }

        inner class ProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameText: TextView = itemView.findViewById(R.id.sectorName)

            init {
                itemView.setOnClickListener {
                    val previousSelected = selectedPosition
                    selectedPosition = this.adapterPosition
                    notifyItemChanged(previousSelected)
                    notifyItemChanged(selectedPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viewlist, parent, false)
            return ProgramViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
            val program = this.fans[position]
            holder.nameText.text = "${program.id} - ${program.name}"

            holder.itemView.setBackgroundColor(
                if (position == selectedPosition) {
                    Color.LTGRAY
                } else {
                    Color.TRANSPARENT
                }
            )
        }

        override fun getItemCount() = this.fans.size
    }
}