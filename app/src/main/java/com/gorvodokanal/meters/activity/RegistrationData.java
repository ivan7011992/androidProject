package com.gorvodokanal.meters.activity;

import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class RegistrationData {

    private HashMap<String, EditText> inputs = new HashMap<>();

    public void add(String name, EditText input) {

        inputs.put(name, input);
    }

    public void setViewMask(String name, String mask) {

        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots(mask);
        FormatWatcher formatWatcher = new MaskFormatWatcher( // форматировать текст будет вот он
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(inputs.get(name));
    }

    public HashMap<String, String> getData() {
        HashMap<String, String> data = new HashMap<>();
        for(Map.Entry<String, EditText> inputEntry : inputs.entrySet()) {
            data.put(inputEntry.getKey(), inputEntry.getValue().getText().toString().trim());
        }
        return data;
    }

}
