package com.example.cryptolist

import ErrorMessageType
import LoaderStatus
import SharedPrefManager
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.cryptolist.helper.SafeClickListener
import com.example.cryptolist.viewmodel.MyBaseViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*


abstract class MyAppCompatActivity : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName
    protected val WAIT_TIME: Long = 500

    private var mBaseView: ViewGroup? = null
    private var mLoaderView: View? = null
    private var progressShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgress()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    //Initializing the progress view
    private fun initProgress() {
        mBaseView = this.findViewById(android.R.id.content)
        mLoaderView = View.inflate(this, R.layout.loader, null)
    }

    //Get the instance of sharedPreferenceManager
    val sharedPrefManager: SharedPrefManager
        get() {
            return SharedPrefManager.getInstance(this)
        }

    //To hide Keyboard
    fun hideKeyboard() {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus
        if (view != null)
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //To show keyboard
    fun showKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // check if no view has focus:
        val view = this.currentFocus
        if (view != null)
            inputManager.toggleSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
    }

    //To show snackbar
    protected fun showSnackbar(
        message: String?,
        errorMessageType: ErrorMessageType = ErrorMessageType.snackbar
    ) {
        val snackbarMessage = SpannableStringBuilder(message)
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content), snackbarMessage,
            Snackbar.LENGTH_LONG
        )
        snackbar.duration = 2000
        val snackBarView = snackbar.view
        var snackbarBg = R.color.purple_200
        var snackbarTextColor = R.color.onError
        if (errorMessageType == ErrorMessageType.snackbarError) {
            snackbarBg = R.color.purple_200
            snackbarTextColor = R.color.onError
        }
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, snackbarBg))
        val textView =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.maxLines = 4
        textView.setTextColor(ContextCompat.getColor(this, snackbarTextColor))
        snackbar.show()
    }

    //To show toast message
//    protected fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }

    private val defaultDialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
    }

    //To show alert dialog with 'ok' button alone
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //To show loader progress
    open fun showProgress() {
        hideKeyboard()
        if (!progressShown) {
            mBaseView!!.addView(mLoaderView)
            progressShown = true
        }
    }

    //To hide loader progress
    open fun hideProgress() {
        if (progressShown) {
            mBaseView!!.removeView(mLoaderView)
            progressShown = false
        }
    }


    //Call this method while setting up Viewmodel to init progress
    protected fun setUpLoader(viewModel: MyBaseViewModel) {
        viewModel.isLoading.observe(this, Observer {
            if (it.equals(LoaderStatus.loading))
                showProgress()
            else
                hideProgress()
        })

        viewModel.errorMediatorLiveData.observe(this, Observer {
            it?.let {
                var updatedErrorMessage: String? = null
                if (it.contains("_")) {
                    updatedErrorMessage = it.replace("_", " ")
                    showSnackbar(updatedErrorMessage.toLowerCase())
                } else {
                    updatedErrorMessage = it
                }


                onErrorCalled(updatedErrorMessage.lowercase(Locale.getDefault()))

            }
        })

        initObservers()

    }

    abstract fun initObservers()

    protected abstract fun onErrorCalled(it: String?)

    protected fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

}