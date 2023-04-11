package jvaug.mycontactlist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    RecyclerView contactList;
    ContactListViewAdapter contactAdapter;
    List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // Initialize interactables
        initListButton();
        initMapButton();
        initSettingsButton();

        initNewContactButton();
        initDeleteContactSwitch();
    }

    @Override
    public void onResume() {
        super.onResume();

        ContactDataBaseAccessor ds = new ContactDataBaseAccessor(this);
        try {
            ds.open();
            contacts = ds.get_contacts();
            ds.close();
            if (contacts.size() > 0) {
                contactList = findViewById(R.id.recycler_view_contacts);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                contactList.setLayoutManager(layoutManager);
                contactAdapter = new ContactListViewAdapter(contacts, this);
                contactList.setAdapter(contactAdapter);
            }
            else {
                Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }

    }

    private void initNewContactButton() {
        Button newContactButton = findViewById(R.id.button_new_contact);
        newContactButton.setOnClickListener(view -> {
            Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initDeleteContactSwitch() {
        Switch deleteContactsSwitch = findViewById(R.id.switch_delete_contacts);
        deleteContactsSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            // Tell the contacts list adapter that deletion is enabled and to show the delete buttons
            contactAdapter.setDeleting(compoundButton.isChecked());
            contactAdapter.notifyDataSetChanged();
        });
    }

    private void initListButton() {
    }
    private void initMapButton() {
        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(view -> {
            Intent intent = new Intent(ContactListActivity.this, ContactMapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
    private void initSettingsButton() {
        ImageButton ibList = findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(view -> {
            Intent intent = new Intent(ContactListActivity.this, ContactSettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
