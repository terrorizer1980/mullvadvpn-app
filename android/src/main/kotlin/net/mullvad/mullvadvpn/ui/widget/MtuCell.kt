package net.mullvad.mullvadvpn.ui.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import kotlin.properties.Delegates.observable
import net.mullvad.mullvadvpn.R

private const val MIN_MTU_VALUE = 1280
private const val MAX_MTU_VALUE = 1420

class MtuCell : Cell {
    private val input =
        (LayoutInflater.from(context).inflate(R.layout.mtu_edit_text, null) as EditText).apply {
            val width = resources.getDimensionPixelSize(R.dimen.cell_input_width)
            val height = resources.getDimensionPixelSize(R.dimen.cell_input_height)

            layoutParams = LayoutParams(width, height, 0.0f)

            addTextChangedListener(InputWatcher())
            setOnFocusChangeListener { _, newHasFocus -> hasFocus = newHasFocus }
        }

    private val validInputColor = context.getColor(R.color.white)
    private val invalidInputColor = context.getColor(R.color.red)

    var value: Int?
        get() {
            val result = input.text.toString().trim().toIntOrNull()
            android.util.Log.d("mullvad", "MTU value returned: $result")
            return result
        }
        set(value) {
            android.util.Log.d("mullvad", "MTU value being set to: $value")
            input.setText(value?.toString() ?: "")
            android.util.Log.d("mullvad", "MTU value was set to: ${input.text}", Exception())
        }

    var onSubmit: ((Int?) -> Unit)? = null

    var hasFocus by observable(false) { _, oldValue, newValue ->
        if (oldValue == true && newValue == false) {
            val mtu = value

            if (mtu == null || (mtu >= MIN_MTU_VALUE && mtu <= MAX_MTU_VALUE)) {
                onSubmit?.invoke(mtu)
            }
        }
    }

    @JvmOverloads
    constructor(
        context: Context,
        attributes: AttributeSet? = null,
        defaultStyleAttribute: Int = 0,
        defaultStyleResource: Int = 0
    ) : super(
        context,
        attributes,
        defaultStyleAttribute,
        defaultStyleResource,
        TextView(context)
    ) {
        cell.apply {
            setEnabled(false)
            setFocusable(false)
            addView(input)
        }

        footer?.text =
            context.getString(R.string.wireguard_mtu_footer, MIN_MTU_VALUE, MAX_MTU_VALUE)
    }

    inner class InputWatcher : TextWatcher {
        override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
            android.util.Log.d("mullvad", "Before MTU text changed: $text")
        }

        override fun onTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
            android.util.Log.d("mullvad", "On MTU text changed: $text")
        }

        override fun afterTextChanged(text: Editable) {
            android.util.Log.d("mullvad", "After MTU text changed: $text")
            val value = text.toString().trim().toIntOrNull()
            android.util.Log.d("mullvad", "  value: $value")

            if (value != null && value >= MIN_MTU_VALUE && value <= MAX_MTU_VALUE) {
                input.setTextColor(validInputColor)
            } else {
                input.setTextColor(invalidInputColor)
            }
        }
    }
}
