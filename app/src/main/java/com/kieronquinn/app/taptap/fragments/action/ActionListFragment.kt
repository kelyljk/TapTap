package com.kieronquinn.app.taptap.fragments.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kieronquinn.app.taptap.R
import com.kieronquinn.app.taptap.adapters.ActionAdapter
import com.kieronquinn.app.taptap.models.ActionInternal
import com.kieronquinn.app.taptap.models.TapAction
import com.kieronquinn.app.taptap.models.TapActionCategory
import com.kieronquinn.app.taptap.utils.dip

class ActionListFragment : Fragment() {

    private var toolbarListener: ((Boolean) -> Unit)? = null

    private var itemClickListener: ((ActionInternal) -> Unit)? = null

    private val category by lazy {
        arguments?.getSerializable("category") as TapActionCategory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_action_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        val actions = getActionsForCategory(category)
        val adapter = ActionAdapter(recyclerView.context, actions, true){
            itemClickListener?.invoke(actions[it.adapterPosition])
        }
        recyclerView.adapter = adapter
        recyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            toolbarListener?.invoke(recyclerView.computeVerticalScrollOffset() > 0)
        }
        recyclerView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, insets.systemWindowInsetBottom + v.context.dip(8))
            insets
        }
    }

    private fun getActionsForCategory(category: TapActionCategory): MutableList<ActionInternal> {
        return TapAction.values().filter { it.category == category && it.isAvailable }.map { ActionInternal(it, ArrayList()) }.toMutableList()
    }

    fun getToolbarTitle(): String {
        return getString(category.labelRes)
    }

    fun setToolbarListener(listener: (Boolean) -> Unit){
        this.toolbarListener = listener
    }

    fun setItemClickListener(listener: (ActionInternal) -> Unit){
        this.itemClickListener = listener
    }

}