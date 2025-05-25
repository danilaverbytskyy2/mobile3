package com.example.app3fragment.fragments

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app3fragment.R
import com.example.app3fragment.database.company.Company
import com.example.app3fragment.viewmodels.CompanyViewModel

private const val ARG_TITLE = "ARG_TITLE"
private const val ARG_SECTOR_ID = "ARG_SECTOR_ID"

class CompanyViewModelFactory(private val sectorId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompanyViewModel::class.java)) {
            return CompanyViewModel(sectorId) as T
        }
        throw IllegalArgumentException("Error")
    }
}

class FragmentCompany : Fragment() {
    private lateinit var adapter: CompanyAdapter;
    private var title: String? = null
    private var sectorId: Int = -1
    private lateinit var companyViewModel: CompanyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            sectorId = it.getInt(ARG_SECTOR_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = inflater.inflate(R.layout.fragment_companies, container, false)
        val factory = CompanyViewModelFactory(this.sectorId)
        this.companyViewModel = ViewModelProvider(this, factory)[CompanyViewModel::class.java]
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarCompanies)
        val toolbarTitle = view.findViewById<TextView>(R.id.menuCompaniesTitle)
        toolbar.inflateMenu(R.menu.main_menu)
        val activity = this.requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbarTitle.text = this.title

        this.adapter = CompanyAdapter()
        this.requireActivity().addMenuProvider(LocalMenuProvider(this.adapter, activity, this.requireContext(), this.companyViewModel, this.sectorId), this.viewLifecycleOwner)

        //============================================

        val recyclerView = view.findViewById<RecyclerView>(R.id.companiesView)
        recyclerView?.adapter = this.adapter
        recyclerView?.layoutManager = LinearLayoutManager(this.requireContext())
        this.companyViewModel.companies.observe(viewLifecycleOwner) { companies ->
            this.adapter.submitList(companies)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(menuTitle: String, sectorId: Int) =
            FragmentCompany().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, menuTitle)
                    putInt(ARG_SECTOR_ID, sectorId)
                }
            }
    }

    class LocalMenuProvider(private val adapter: CompanyAdapter, private val activity: AppCompatActivity, private val context: Context, private val companyViewModel: CompanyViewModel, private val sectorId: Int) : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu)
            val entryItem = menu.findItem(R.id.menu_phone)
            entryItem.setVisible(false)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_add -> {
                    FragmentSector.DialogHelper.showNameInDialog(this.context) { name ->
                        val newCompany = Company(name = name, sectorId = sectorId)
                        companyViewModel.addCompany(newCompany)
                    }
                    true
                }
                R.id.menu_edit -> {
                    FragmentSector.DialogHelper.showNameInDialog(this.context) { name ->
                        if (this.adapter.selectedPosition >= 0 && this.adapter.selectedPosition < this.adapter.companies.size) {
                            companyViewModel.renameCompany(this.adapter.companies[this.adapter.selectedPosition], name)
                        }
                    }
                    true
                }
                R.id.menu_delete -> {
                    if (this.adapter.selectedPosition >= 0 && this.adapter.selectedPosition < this.adapter.companies.size) {
                        companyViewModel.removeCompany(this.adapter.companies[this.adapter.selectedPosition])
                    }
                    true
                }
                R.id.menu_entry -> {
                    val selected = adapter.selectedPosition
                    if (selected in adapter.companies.indices) {
                        val selectedCompanyId = adapter.companies[selected].id
                        val fragment = FragmentProgram.newInstance(adapter.companies[selected].name + " - Programs", selectedCompanyId)
                        activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, fragment).addToBackStack(null).commit()
                    } else {
                        Toast.makeText(context, "Choose an item", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    class CompanyAdapter : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {
        var companies = emptyList<Company>()
        var selectedPosition = -1

        @SuppressLint("NotifyDataSetChanged")
        fun submitList(newList: List<Company>) {
            this.companies = newList
            notifyDataSetChanged()
        }

        inner class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viewlist, parent, false)
            return CompanyViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
            val company = this.companies[position]
            holder.nameText.text = "${company.id} - ${company.name}"

            holder.itemView.setBackgroundColor(
                if (position == selectedPosition) {
                    Color.LTGRAY
                } else {
                    Color.TRANSPARENT
                }
            )
        }

        override fun getItemCount() = this.companies.size
    }
}