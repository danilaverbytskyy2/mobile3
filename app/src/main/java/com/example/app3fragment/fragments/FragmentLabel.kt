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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app3fragment.R
import com.example.app3fragment.database.label.Label
import com.example.app3fragment.viewmodels.LabelViewModel

private const val ARG_TITLE = "ARG_TITLE"

class FragmentLabel : Fragment() {
    private lateinit var adapter: LabelAdapter;
    private var title: String? = null
    private lateinit var labelViewModel: LabelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = inflater.inflate(R.layout.fragment_labels, container, false)
        this.labelViewModel = ViewModelProvider(this)[LabelViewModel::class.java]
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarSectors)
        val toolbarTitle = view.findViewById<TextView>(R.id.menuSectorsTitle)
        toolbar.inflateMenu(R.menu.main_menu)
        val activity = this.requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbarTitle.text = this.title

        this.adapter = LabelAdapter()
        this.requireActivity().addMenuProvider(LocalMenuProvider(this.adapter, activity, this.requireContext(), this.labelViewModel), this.viewLifecycleOwner)

        //============================================

        val recyclerView = view.findViewById<RecyclerView>(R.id.sectorsView)
        recyclerView?.adapter = this.adapter
        recyclerView?.layoutManager = LinearLayoutManager(this.requireContext())
        this.labelViewModel.labels.observe(viewLifecycleOwner) { sectors ->
            this.adapter.submitList(sectors)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(menuTitle: String) =
            FragmentLabel().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, menuTitle)
                }
            }
    }

    class LocalMenuProvider(private val adapter: LabelAdapter, private val activity: AppCompatActivity, private val context: Context, private val labelViewModel: LabelViewModel) : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu)
            val entryItem = menu.findItem(R.id.menu_phone)
            entryItem.setVisible(false)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_add -> {
                    DialogHelper.showNameInDialog(this.context) { name ->
                        val newLabel = Label(name = name)
                        labelViewModel.addLabel(newLabel)
                    }
                    true
                }
                R.id.menu_edit -> {
                    DialogHelper.showNameInDialog(this.context) { name ->
                        if (this.adapter.selectedPosition >= 0 && this.adapter.selectedPosition < this.adapter.labels.size) {
                            labelViewModel.renameLabel(this.adapter.labels[this.adapter.selectedPosition], name)
                        }
                    }
                    true
                }
                R.id.menu_delete -> {
                    if (this.adapter.selectedPosition >= 0 && this.adapter.selectedPosition < this.adapter.labels.size) {
                        labelViewModel.removeLabel(this.adapter.labels[this.adapter.selectedPosition])
                    }
                    true
                }
                R.id.menu_entry -> {
                    val selected = adapter.selectedPosition
                    if (selected in adapter.labels.indices) {
                        val selectedSectorId = adapter.labels[selected].id
                        val fragment = FragmentArtist.newInstance(adapter.labels[selected].name + " - Артисты", selectedSectorId)
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

    object DialogHelper {
        fun showNameInDialog(context: Context, onConfirm: (String) -> Unit) {
            val input = EditText(context).apply {
                hint = "Name"
            }

            AlertDialog.Builder(context).setTitle("Data").setView(input).setPositiveButton("Enter") { _, _ ->
                val name = input.text.toString()
                if (name.isNotBlank()) {
                    onConfirm(name)
                } else {
                    Toast.makeText(context, "It must not be empty", Toast.LENGTH_SHORT).show()
                }
            }.setNegativeButton("Cancel", null).show()
        }
    }

    class LabelAdapter() : RecyclerView.Adapter<LabelAdapter.SectorViewHolder>() {
        var labels = emptyList<Label>()
        var selectedPosition = -1

        @SuppressLint("NotifyDataSetChanged")
        fun submitList(newList: List<Label>) {
            this.labels = newList
            notifyDataSetChanged()
        }

        inner class SectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viewlist, parent, false)
            return SectorViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: SectorViewHolder, position: Int) {
            val label = this.labels[position]
            holder.nameText.text = "${label.id} - ${label.name}"

            holder.itemView.setBackgroundColor(
                if (position == selectedPosition) {
                    Color.LTGRAY
                } else {
                    Color.TRANSPARENT
                }
            )
        }

        override fun getItemCount() = this.labels.size
    }
}