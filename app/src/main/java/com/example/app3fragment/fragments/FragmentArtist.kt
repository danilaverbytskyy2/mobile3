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
import com.example.app3fragment.database.artist.Artist
import com.example.app3fragment.viewmodels.ArtistViewModel

private const val ARG_TITLE = "ARG_TITLE"
private const val ARG_SECTOR_ID = "ARG_SECTOR_ID"

class ArtistViewModelFactory(private val sectorId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
            return ArtistViewModel(sectorId) as T
        }
        throw IllegalArgumentException("Error")
    }
}

class FragmentArtist : Fragment() {
    private lateinit var adapter: ArtistAdapter;
    private var title: String? = null
    private var sectorId: Int = -1
    private lateinit var artistViewModel: ArtistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            sectorId = it.getInt(ARG_SECTOR_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = inflater.inflate(R.layout.fragment_artists, container, false)
        val factory = ArtistViewModelFactory(this.sectorId)
        this.artistViewModel = ViewModelProvider(this, factory)[ArtistViewModel::class.java]
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

        this.adapter = ArtistAdapter()
        this.requireActivity().addMenuProvider(LocalMenuProvider(this.adapter, activity, this.requireContext(), this.artistViewModel, this.sectorId), this.viewLifecycleOwner)

        //============================================

        val recyclerView = view.findViewById<RecyclerView>(R.id.companiesView)
        recyclerView?.adapter = this.adapter
        recyclerView?.layoutManager = LinearLayoutManager(this.requireContext())
        this.artistViewModel.companies.observe(viewLifecycleOwner) { companies ->
            this.adapter.submitList(companies)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(menuTitle: String, sectorId: Int) =
            FragmentArtist().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, menuTitle)
                    putInt(ARG_SECTOR_ID, sectorId)
                }
            }
    }

    class LocalMenuProvider(private val adapter: ArtistAdapter, private val activity: AppCompatActivity, private val context: Context, private val artistViewModel: ArtistViewModel, private val sectorId: Int) : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu)
            val entryItem = menu.findItem(R.id.menu_phone)
            entryItem.setVisible(false)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_add -> {
                    FragmentLabel.DialogHelper.showNameInDialog(this.context) { name ->
                        val newArtist = Artist(name = name, labelId = sectorId)
                        artistViewModel.addArtist(newArtist)
                    }
                    true
                }
                R.id.menu_edit -> {
                    FragmentLabel.DialogHelper.showNameInDialog(this.context) { name ->
                        if (this.adapter.selectedPosition >= 0 && this.adapter.selectedPosition < this.adapter.companies.size) {
                            artistViewModel.renameArtist(this.adapter.companies[this.adapter.selectedPosition], name)
                        }
                    }
                    true
                }
                R.id.menu_delete -> {
                    if (this.adapter.selectedPosition >= 0 && this.adapter.selectedPosition < this.adapter.companies.size) {
                        artistViewModel.removeArtist(this.adapter.companies[this.adapter.selectedPosition])
                    }
                    true
                }
                R.id.menu_entry -> {
                    val selected = adapter.selectedPosition
                    if (selected in adapter.companies.indices) {
                        val selectedCompanyId = adapter.companies[selected].id
                        val fragment = FragmentProgram.newInstance(adapter.companies[selected].name + " - Фанаты", selectedCompanyId)
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

    class ArtistAdapter : RecyclerView.Adapter<ArtistAdapter.CompanyViewHolder>() {
        var companies = emptyList<Artist>()
        var selectedPosition = -1

        @SuppressLint("NotifyDataSetChanged")
        fun submitList(newList: List<Artist>) {
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