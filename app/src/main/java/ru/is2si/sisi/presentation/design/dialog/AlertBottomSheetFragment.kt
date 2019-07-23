package ru.is2si.sisi.presentation.design.dialog

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.SystemClock
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat.requireViewById
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_base_bottom_dialog.*
import ru.is2si.sisi.BuildConfig.APPLICATION_ID
import ru.is2si.sisi.R
import ru.is2si.sisi.base.DelegationAdapter
import ru.is2si.sisi.base.AdapterMultiSelectManager
import ru.is2si.sisi.base.AdapterSelectManager
import ru.is2si.sisi.base.extension.hide
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.base.extension.show

open class AlertBottomSheetFragment : DialogFragment() {

    protected open val layoutId: Int = 0

    private var isDismissed: Boolean = false

    private val title: String?
        get() = arguments?.getString(ARG_TITLE)
    private val message: String?
        get() = arguments?.getString(ARG_MESSAGE)

    private val items: List<String>
        get() = arguments?.getStringArrayList(ARG_ITEMS) ?: listOf()
    private val checkedItem: Int
        get() = arguments?.getInt(ARG_CHECKED_ITEM, -1) ?: -1

    private val multiItems: List<MultiItem<*>>
        get() = arguments?.getParcelableArrayList(ARG_MULTI_ITEMS) ?: listOf()
    private val selectedItems: List<MultiItem<*>>
        get() = arguments?.getParcelableArrayList(ARG_SELECTED_ITEMS) ?: listOf()

    private val hideScrollIndicator: Boolean
        get() = arguments?.getBoolean(ARG_HIDE_SCROLL_INDICATOR) ?: false
    private val cancelable: Boolean
        get() = arguments?.getBoolean(ARG_CANCELABLE, true) ?: true

    private var multiSelectManager: AdapterMultiSelectManager? = null

    protected var contentContainer: ViewGroup? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = cancelable
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(
                R.layout.fragment_base_bottom_dialog,
                container,
                false
        ) as ViewGroup
        if (layoutId != 0) {
            val content = inflater.inflate(layoutId, root, false)
            contentContainer = requireViewById(root, R.id.vgContainer)
            if (content.layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                val vgSheet = requireViewById<NestedScrollView>(root, R.id.vgSheet)
                vgSheet.isFillViewport = true
                vgSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            contentContainer?.addView(content)
        }
        val items = items
        val multiItems = multiItems
        if (hideScrollIndicator || cancelable.not() || items.isNotEmpty() || multiItems.isNotEmpty()) {
            root.findViewById<View>(R.id.vScrollIndicator).hide()
        }
        val tvTitle = root.findViewById<TextView>(R.id.tvTitle)
        if (title == null) {
            tvTitle.hide()
        } else {
            tvTitle.text = title
            tvTitle.show()
        }
        val tvMessage = root.findViewById<TextView>(R.id.tvMessage)
        if (message == null) {
            tvMessage.hide()
        } else {
            tvMessage.text = message
            tvMessage.show()
        }
        val rvItems = root.findViewById<RecyclerView>(R.id.rvItems)
        if (items.isNotEmpty() && multiItems.isNotEmpty())
            throw IllegalArgumentException("Set one of them - items or multiItems")
        when {
            items.isNotEmpty() -> {
                val adapter = DelegationAdapter()
                val selectManager = AdapterSelectManager(adapter)
                selectManager.selectedItem = items.getOrNull(checkedItem)
                val context = requireContext()
                val radioItemDelegate = RadioItemDelegate(context, selectManager) {
                    if (dialog?.isShowing == true) {
                        safeDismiss()
                        val item = adapter.items[it] as String
                        val data = Intent().putExtra(EXTRA_SELECTION_RESULT, item)
                        emitResult(data)
                    }
                }
                adapter.delegatesManager
                        .addDelegate(ScrollIndicatorDelegate(context))
                        .addDelegate(radioItemDelegate)
                adapter.items = listOf(ScrollIndicatorDelegate.Item()) + items
                rvItems.setHasFixedSize(true)
                rvItems.layoutManager = LinearLayoutManager(context)
                rvItems.adapter = adapter
                rvItems.show()
            }
            multiItems.isNotEmpty() -> {
                val adapter = DelegationAdapter()
                multiSelectManager = AdapterMultiSelectManager(adapter)
                selectedItems.forEach { multiSelectManager?.selectItem(it, true) }
                val context = requireContext()
                val checkboxItemDelegate = CheckboxItemDelegate(context, multiSelectManager!!)
                adapter.delegatesManager
                        .addDelegate(ScrollIndicatorDelegate(context))
                        .addDelegate(checkboxItemDelegate)
                adapter.items = listOf(ScrollIndicatorDelegate.Item()) + multiItems
                rvItems.setHasFixedSize(true)
                rvItems.layoutManager = LinearLayoutManager(context)
                rvItems.adapter = adapter
                rvItems.show()
            }
            else -> rvItems.hide()
        }
        val vgControlButtons = root.findViewById<View>(R.id.vgControlButtons)
        val okText = arguments?.getString(ARG_OK_TEXT)
        val cancelText = arguments?.getString(ARG_CANCEL_TEXT)
        if (okText == null && cancelText == null) {
            vgControlButtons.hide()
            if (multiItems.isNotEmpty())
                throw IllegalArgumentException("Multi items require control buttons")
        } else {
            vgControlButtons.show()
        }
        val tvOk = root.findViewById<TextView>(R.id.tvOk)
        if (okText == null) {
            tvOk.hide()
        } else {
            tvOk.text = okText
            tvOk.show()
        }
        val tvCancel = root.findViewById<TextView>(R.id.tvCancel)
        if (cancelText == null) {
            tvCancel.hide()
        } else {
            tvCancel.text = cancelText
            tvCancel.show()
        }
        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vgSheet = requireViewById<NestedScrollView>(view, R.id.vgSheet)
        val bottomSheetBehavior = from(vgSheet)
        setupBottomSheet(bottomSheetBehavior)
        setupButtons(view)
        vgSheet.post {
            val maxHeight = view.height - vgControlButtons.height
            val alertBottomSheetBehaviour = bottomSheetBehavior as AlertBottomSheetBehaviour
            when {
                onContentMaxHeightProcess(maxHeight, alertBottomSheetBehaviour) -> Unit
                rvItems.height > maxHeight -> {
                    rvItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            val verticalScrollOffset = rvItems.computeVerticalScrollOffset()
                            alertBottomSheetBehaviour.enableStartNestedScroll = verticalScrollOffset == 0
                        }
                    })
                    rvItems.layoutParams.height = maxHeight
                    rvItems.requestLayout()
                }
                else -> alertBottomSheetBehaviour.enableStartNestedScroll = true
            }
            bottomSheetBehavior.state = STATE_EXPANDED
        }
    }

    protected open fun onContentMaxHeightProcess(
            maxHeight: Int,
            alertBottomSheetBehaviour: AlertBottomSheetBehaviour<*>
    ): Boolean = false

    private fun setupBottomSheet(bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>) {
        if (cancelable) {
            bottomSheetBehavior.peekHeight = 0
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.skipCollapsed = true
        } else {
            bottomSheetBehavior.isHideable = false
        }
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            private val cancelable = this@AlertBottomSheetFragment.cancelable
            private var firstExpanded = false

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (isDismissed) {
                    val minOffset = 0.1F
                    if (bottomSheetBehavior.isHideable && slideOffset <= -minOffset) {
                        safeDismiss()
                    } else if (slideOffset <= minOffset) {
                        safeDismiss()
                    }
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_HIDDEN) dismiss()
                if (cancelable.not()) {
                    if (newState == STATE_DRAGGING)
                        bottomSheetBehavior.state = STATE_EXPANDED
                    if (firstExpanded.not() && newState == STATE_EXPANDED)
                        view?.post { firstExpanded = true }
                    if (firstExpanded && bottomSheetBehavior.isHideable && newState == STATE_EXPANDED)
                        bottomSheetBehavior.state = STATE_HIDDEN
                }
            }
        })
    }

    private fun setupButtons(view: View) {
        tvCancel.onClick {
            onControlClick(ControlResult.CANCEL)
            safeDismiss()
        }
        tvOk.onClick {
            onControlClick(ControlResult.OK)
            safeDismiss()
        }
        if (cancelable)
            view.postDelayed({ view.onClick { safeDismiss() } }, 400)
        else
            view.setOnClickListener { }
    }

    protected open fun onControlClick(which: ControlResult) {
        preventSheetDragging()
        view?.post {
            val data = Intent().putExtra(EXTRA_RESULT, which.name)
            if (multiSelectManager != null) {
                val items = multiSelectManager!!.getSelectedItems()
                        .map { it as MultiItem<*> }
                data.putParcelableArrayListExtra(EXTRA_SELECTION_ITEM_RESULT, ArrayList(items))
            }
            emitResult(data)
        }
    }

    private fun emitResult(data: Intent?) {
        data?.apply { arguments?.getBundle(ARG_DATA)?.let(::putExtras) }
        if (targetFragment == null)
            (activity as? Callback)?.onAlertDialogResult(targetRequestCode, RESULT_OK, data)
        else
            targetFragment?.onActivityResult(targetRequestCode, RESULT_OK, data)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.setOnKeyListener { _, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                if (event.action != KeyEvent.ACTION_DOWN) {
                    if (cancelable && vgSheet != null && from(vgSheet).state == STATE_EXPANDED) {
                        onControlClick(ControlResult.CANCEL)
                        safeDismiss()
                    }
                }
                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }
    }

    private fun safeDismiss() {
        preventSheetDragging()
        isDismissed = true
        val bottomSheetBehavior = from(vgSheet ?: return)
        if (cancelable) {
            bottomSheetBehavior.state = STATE_HIDDEN
        } else {
            bottomSheetBehavior.peekHeight = 0
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.state = STATE_HIDDEN
        }
    }

    private fun preventSheetDragging() {
        val vBlock = view?.findViewById<View>(R.id.vBlock)
        vBlock?.show()
        val vgSheet = view?.findViewById<View>(R.id.vgSheet) ?: return
        fakeTouchView(vgSheet)
        val bottomSheetBehavior = from(vgSheet) as? AlertBottomSheetBehaviour<*> ?: return
        bottomSheetBehavior.enableStartNestedScroll = true
        bottomSheetBehavior.enableInterceptTouchEvent = false
    }

    private fun fakeTouchView(view: View) {
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + 100
        val x = 0.0f
        val y = 0.0f
        val metaState = 0
        val motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        )
        view.dispatchTouchEvent(motionEvent)
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_OK_TEXT = "ok_text"
        private const val ARG_CANCEL_TEXT = "cancel_text"
        private const val ARG_CANCELABLE = "cancelable"
        private const val ARG_HIDE_SCROLL_INDICATOR = "hide_scroll_indicator"

        private const val ARG_DATA = "data"

        private const val ARG_ITEMS = "items"
        private const val ARG_CHECKED_ITEM = "checked_item"

        private const val ARG_MULTI_ITEMS = "multi_items"
        private const val ARG_SELECTED_ITEMS = "selected_items"

        private const val EXTRA_RESULT = "$APPLICATION_ID.extra.EXTRA_RESULT"
        private const val EXTRA_SELECTION_RESULT = "$APPLICATION_ID.extra.SELECTION_RESULT"
        private const val EXTRA_SELECTION_ITEM_RESULT = "$APPLICATION_ID.extra.SELECTION_ITEM_RESULT"

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withTitle(text: String): T {
            if (arguments == null)
                arguments = Bundle()
            requireArguments().putString(ARG_TITLE, text)
            return this
        }

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withMessage(text: String): T {
            if (arguments == null)
                arguments = Bundle()
            requireArguments().putString(ARG_MESSAGE, text)
            return this
        }

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withOkText(text: String): T {
            if (arguments == null)
                arguments = Bundle()
            requireArguments().putString(ARG_OK_TEXT, text)
            return this
        }

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withCancelText(text: String): T {
            if (arguments == null)
                arguments = Bundle()
            requireArguments().putString(ARG_CANCEL_TEXT, text)
            return this
        }

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withCancelable(cancelable: Boolean): T {
            if (arguments == null)
                arguments = Bundle()
            requireArguments().putBoolean(ARG_CANCELABLE, cancelable)
            return this
        }

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withHideScrollIndicator(hideScrollIndicator: Boolean): T {
            if (arguments == null)
                arguments = Bundle()
            requireArguments().putBoolean(ARG_HIDE_SCROLL_INDICATOR, hideScrollIndicator)
            return this
        }

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withTarget(fragment: Fragment?, requestCode: Int): T {
            setTargetFragment(fragment, requestCode)
            return this
        }

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withData(dataAction: Bundle.() -> Unit): T {
            if (arguments == null)
                arguments = Bundle()
            val data = Bundle()
            dataAction.invoke(data)
            requireArguments().putBundle(ARG_DATA, data)
            return this
        }

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withSingleChoiceItems(
                items: List<String>,
                checkedItem: Int
        ): T {
            if (arguments == null)
                arguments = Bundle()
            requireArguments().apply {
                putStringArrayList(ARG_ITEMS, ArrayList(items))
                putInt(ARG_CHECKED_ITEM, checkedItem)
            }
            return this
        }

        @JvmStatic
        fun <T : AlertBottomSheetFragment> T.withMultiChoiceItems(
                items: List<MultiItem<*>>,
                selectedItems: List<MultiItem<*>>
        ): T {
            if (arguments == null)
                arguments = Bundle()
            requireArguments().apply {
                putParcelableArrayList(ARG_MULTI_ITEMS, ArrayList(items))
                putParcelableArrayList(ARG_SELECTED_ITEMS, ArrayList(selectedItems))
            }
            return this
        }

        @JvmStatic
        fun getResult(data: Intent?): ControlResult? {
            data ?: return null
            return ControlResult.valueOf(data.getStringExtra(EXTRA_RESULT))
        }

        @JvmStatic
        fun getSelectionResult(data: Intent?): String? = data?.getStringExtra(EXTRA_SELECTION_RESULT)

        @JvmStatic
        fun getSelectionItemResult(data: Intent?): List<MultiItem<*>>? =
                data?.getParcelableArrayListExtra(EXTRA_SELECTION_ITEM_RESULT)
    }

    enum class ControlResult { OK, CANCEL }

    interface Callback {
        fun onAlertDialogResult(requestCode: Int, resultCode: Int, data: Intent?)
    }

    @Parcelize
    data class MultiItem<T : Parcelable>(
            val title: String?,
            val checkTitle: String?,
            val item: T?
    ) : Parcelable

}