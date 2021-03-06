package solvro.spaceflights.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import solvro.spaceflights.R

class ToolbarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_toolbar, container, false)

        view.findViewById<ImageButton>(R.id.button).setOnClickListener {
            val builder: AlertDialog.Builder = this.let {
                AlertDialog.Builder(requireActivity(), R.style.AlertDialogCustom)
            }

            builder.setMessage(R.string.app_info_message)
                .setTitle(R.string.app_info)
                .setPositiveButton(R.string.ok, null)
            builder.create().show()
        }

        return view
    }
}