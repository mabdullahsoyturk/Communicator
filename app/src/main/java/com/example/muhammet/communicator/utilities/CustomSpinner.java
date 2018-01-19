package com.example.muhammet.communicator.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CustomSpinner extends android.support.v7.widget.AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener {

    private String[] _items = null;
    private boolean[] _selection = null;

    private final ArrayAdapter<String> adapter;

    public CustomSpinner(Context context) {
        super(context);

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (_selection != null && which < _selection.length) {
            _selection[which] = isChecked;

            adapter.clear();
            adapter.add(buildSelectedItemString());
            setSelection(0);
        }
        else {
            throw new IllegalArgumentException("Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(_items, _selection, this);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException("setAdapter is not supported by CustomSpinner.");
    }

    public void setItems(String[] items) {
        _items = items;
        _selection = new boolean[_items.length];

        Arrays.fill(_selection, false); // true defaults to checked, false defaults to unchecked
        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    public void setItemsChecked(String[] items) {
        _items = items;
        _selection = new boolean[_items.length];

        Arrays.fill(_selection, true); // true defaults to checked, false defaults to unchecked
        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    public void setItemsSaved(String[] items, String saved) {
        _items = items;
        _selection = new boolean[_items.length];

        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            _selection[i] = saved.toLowerCase().contains(_items[i].toLowerCase());

            if (_selection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i]);
            }
        }

        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    public void setItems(List<String> items) {
        _items = items.toArray(new String[items.size()]);
        _selection = new boolean[_items.length];

        Arrays.fill(_selection, false); // true defaults to checked, false defaults to unchecked
        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    public void setItemsChecked(List<String> items) {
        _items = items.toArray(new String[items.size()]);
        _selection = new boolean[_items.length];

        Arrays.fill(_selection, true); // true defaults to checked, false defaults to unchecked
        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    public void setSelection(String[] selection) {
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    _selection[j] = true;
                }
            }
        }
    }

    public void setSelection(List<String> selection) {
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    _selection[j] = true;
                }
            }
        }
    }

    public void setSelection(int[] selectedIndicies) {
        for (int index : selectedIndicies) {
            if (index >= 0 && index < _selection.length) {
                _selection[index] = true;
            }
            else {
                throw new IllegalArgumentException("Index " + index + " is out of bounds.");
            }
        }
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<>();
        for (int i = 0; i < _items.length; ++i) {
            if (_selection[i]) {
                selection.add(_items[i]);
            }
        }
        return selection;
    }

    public List<Integer> getSelectedIndicies() {
        List<Integer> selection = new LinkedList<>();
        for (int i = 0; i < _items.length; ++i) {
            if (_selection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (_selection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i]);
            }
        }

        return sb.toString();
    }


}
