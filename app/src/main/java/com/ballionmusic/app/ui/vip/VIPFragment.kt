package com.ballionmusic.app.ui.vip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ballionmusic.app.databinding.VipFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.text.SpannableString
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.app.Dialog
import android.os.Handler
import android.os.Looper
import com.ballionmusic.app.R
import android.view.WindowManager
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.FrameLayout
import android.view.ViewGroup.LayoutParams
import androidx.core.content.ContextCompat
import android.view.ViewTreeObserver
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import android.widget.Toast
import com.ballionmusic.app.databinding.PurchasePopupBinding
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class VIPFragment : Fragment() {

    private var _binding: VipFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var vipStatusManager: com.ballionmusic.app.data.VIPStatusManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VipFragmentBinding.inflate(inflater, container, false)
        vipStatusManager = com.ballionmusic.app.data.VIPStatusManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()

        binding.btnBuyRenew.setOnClickListener {
            showPurchasePopup()
        }
    }

    private fun updateUI() {
        val hasVIP = vipStatusManager.isVIP()
        if (hasVIP) {
            binding.btnBuyRenew.text = "Renew"
            binding.tvVipStatus.text = "VIP expires in: ${getFormattedExpirationTime()}"
        } else {
            binding.btnBuyRenew.text = "Buy"
            binding.tvVipStatus.text = "Become VIP for 29 UAH/month"
        }
    }

    private fun getFormattedExpirationTime(): String {
        val expirationTime = vipStatusManager.getVIPExpiration()
        val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        return if (vipStatusManager.isVIP()) {
            if (expirationTime == 0L) "Lifetime" else sdf.format(java.util.Date(expirationTime))
        } else {
            "N/A"
        }
    }

    private fun showPurchasePopup() {
        val dialog = PurchasePopupDialogFragment.newInstance(getFormattedExpirationTime())
        dialog.show(childFragmentManager, "PurchasePopup")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class PurchasePopupDialogFragment : DialogFragment() {

    private var expirationDate: String? = null

    companion object {
        fun newInstance(expirationDate: String): PurchasePopupDialogFragment {
            val fragment = PurchasePopupDialogFragment()
            val args = Bundle()
            args.putString("expirationDate", expirationDate)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        expirationDate = arguments?.getString("expirationDate")
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val binding = PurchasePopupBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        // Set blurred background
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)

        binding.tvCardNumber.text = "5168 7520 2354 6115"
        binding.tvExpiration.text = "VIP expires on: $expirationDate"

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        // Set dialog width and height
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)

        return dialog
    }
}
