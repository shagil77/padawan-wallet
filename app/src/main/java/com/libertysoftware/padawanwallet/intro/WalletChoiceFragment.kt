/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet.intro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.libertysoftware.padawanwallet.PadawanWalletApplication
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.main.HomeActivity
import org.bitcoindevkit.bdkjni.Types.ExtendedKeys
import timber.log.Timber

class WalletChoiceFragment : Fragment() {

    private lateinit var keys: ExtendedKeys

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet_choice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button2).setOnClickListener {
            val intent: Intent = Intent(this@WalletChoiceFragment.context, HomeActivity::class.java)
            generateWallet()
            // showSeedToast()
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.button3).setOnClickListener {
            showDevelopmentToast()
        }
    }

    fun showDevelopmentToast() {
        val text = "Currently in development..."
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(this@WalletChoiceFragment.context, text, duration)
        toast.show()
    }

    private fun generateWallet(): Unit {
        val app = requireActivity().application as PadawanWalletApplication

        // Timber.i("thunderlogs: keys value is: ${keys}")
        // Timber.i("thunderlogs: this.keys value is: ${this.keys}")
        // Timber.i("thunderlogs: this@WalletChoiceFragment.keys value is: ${this@WalletChoiceFragment.keys}")
        this.keys = app.generateExtendedKey(12)
        // Timber.i("thunderlogs: 2 keys value is: $keys")
        // Timber.i("thunderlogs: 2 this.keys value is: ${this.keys}")
        // Timber.i("thunderlogs: 2 this@WalletChoiceFragment.keys value is: ${this@WalletChoiceFragment.keys}")

        // save seedphrase to shared preferences
        val editor: SharedPreferences.Editor = this.requireActivity().getSharedPreferences("current_wallet", Context.MODE_PRIVATE)!!.edit()
        Timber.tag("THUNDERLOGS").i("The seed phrase is: ${keys.mnemonic}")
        editor.putString("seedphrase", keys.mnemonic)
        editor.commit()

        val descriptor: String = app.createDescriptor(keys)
        val changeDescriptor: String = app.createChangeDescriptor(keys)
        app.createWallet(descriptor, changeDescriptor)
    }
}
