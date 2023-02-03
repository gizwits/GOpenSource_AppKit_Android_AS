package com.gizwits.opensource.appkit.view;

import java.util.Locale;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class HexWatcher implements TextWatcher {
	private EditText _text;

	public HexWatcher(EditText _text) {
		this._text = _text;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s == null || s.length() == 0)
			return;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ' && i < s.length() - 1) {
				continue;
			} else {
				sb.append(s.charAt(i));
				if (sb.length() % 3 == 0 && sb.charAt(sb.length() - 1) != ' ') {
					sb.insert(sb.length() - 1, ' ');
				}
			}
		}

		if (!sb.toString().equals(s.toString())) {
			int index = start + 1;
			if (sb.charAt(start) == ' ') {
				if (before == 0) {
					index++;
				} else {
					index--;
				}
			} else {
				if (before == 1) {
					index--;
				}
			}
			_text.setText(sb.toString().toUpperCase(Locale.getDefault()));
			_text.setSelection(index);
		}

		// _text.removeTextChangedListener(this);
		// _text.setText(s.toString().toUpperCase(Locale.getDefault()));
		// _text.addTextChangedListener(this);

	}

	@Override
	public void afterTextChanged(Editable s) {
	}

}