package jvaug.mycontactlist;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.format.DateFormat;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    static TextView dateText;
    List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the dateText components (not necessary to the assignment)
        dateText = findViewById(R.id.textBirthday);

        // Initialize all the interactables
        initToggleButton();
        initListButton();
        initMapButton();
        initSettingsButton();
        initSaveButton();
        setForEditing(((ToggleButton)findViewById(R.id.toggleButtonEdit)).isChecked());
        initChangeDateButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-disable the edit toggle button
        final ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
        editToggle.setChecked(false);
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
    private void initMapButton() {
        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactMapActivity.class);
            // Move the current contact to the google map activity
            Contact c = getCurrentContact();
            assert(c != null);
            intent.putExtra("current_contact", c);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
    private void initSettingsButton() {
        ImageButton ibList = findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactSettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(view -> saveContact() );
    }

    // Get the current contact from the current values in the text fields
    private Contact getCurrentContact() {
        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);
        TextView date_text = findViewById(R.id.textBirthday);

        // Make sure zip is a valid number
        int zip = -2;
        try {
            zip = Integer.parseInt(editZipCode.getText().toString());
        } catch (NumberFormatException e) {
            zip = -1;
        }

        int total_information  = 0;
        String contactName = editName.getText().toString();
        if (!contactName.equals("")) total_information++;
        String contactStreetAddress = editAddress.getText().toString();
        if (!contactStreetAddress.equals("")) total_information++;
        String contactCity = editCity.getText().toString();
        if (!contactCity.equals("")) total_information++;
        String contactState = editState.getText().toString();
        if (!contactState.equals("")) total_information++;
        String contactHomePhone = editPhone.getText().toString();
        if (!contactHomePhone.equals("")) total_information++;
        String contactCellPhone = editCell.getText().toString();
        if (!contactCellPhone.equals("")) total_information++;
        String contactEmail = editEmail.getText().toString();
        if (!contactEmail.equals("")) total_information++;
        String contactDate = date_text.getText().toString();
        if (!contactDate.equals("")) total_information++;

        // Warn if the user doesn't have at least 2 peices of information
        if (total_information < 2) Toast.makeText(MainActivity.this, "Not enough information, must contain at least two bits of information", Toast.LENGTH_LONG).show();

        return new Contact(contactName, contactStreetAddress, contactCity, contactState, zip, contactHomePhone, contactCellPhone, contactEmail, contactDate);
    }

    // Clear the input fields back to empty
    private void clearFields() {
        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);
        TextView date_text = findViewById(R.id.textBirthday);

        editName.setText("");
        editAddress.setText("");
        editCity.setText("");
        editState.setText("");
        editZipCode.setText("");
        editPhone.setText("");
        editCell.setText("");
        editEmail.setText("");
    }

    // Save the current contact into the local database
    private void saveContact() {
        Contact c = getCurrentContact();
        contacts.add(c);

        ContactDataBaseAccessor ds = new ContactDataBaseAccessor(MainActivity.this);
        boolean wasSuccessful;
        try {
            ds.open();
            if (c.getID() == -1) {
                wasSuccessful = ds.insert_contact(c);
            }
            else {
                wasSuccessful = ds.update_contact(c);
            }
            ds.close();
        }
        catch (Exception e) {
            wasSuccessful = false;
            ds.close();
        }

        if (wasSuccessful) {
            clearFields();
            Toast.makeText(MainActivity.this, "Contact Saved!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, "Contact Save Failed!", Toast.LENGTH_LONG).show();
        }
    }

    private void initToggleButton() {
        final ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(view -> setForEditing(editToggle.isChecked()));
    }

    // Enable or Disable all the input fields as editable
    private void setForEditing(boolean enabled) {
        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);
        Button buttonChangeDate = findViewById(R.id.buttonBirthday);
        Button buttonSave = findViewById(R.id.buttonSave);

        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipCode.setEnabled(enabled);
        editPhone.setEnabled(enabled);
        editCell.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        buttonChangeDate.setEnabled(enabled);
        buttonSave.setEnabled(enabled);

        if (enabled) {
            editName.requestFocus();
        }

    }

    // Not important, a fragment for selecting a date
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date for the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            dateText.setText(DateFormat.format("MM/dd/yyyy", new Date(year - 1900, month, day))); // for some reason the calendar returns 3923 for 2023
        }
    }
    private void initChangeDateButton() {
        Button changeDate = findViewById(R.id.buttonBirthday);
        changeDate.setOnClickListener(view -> {
            DatePickerFragment datePickerDialog = new DatePickerFragment();
            datePickerDialog.show(getSupportFragmentManager(), "datePicker");
        });
    }
}