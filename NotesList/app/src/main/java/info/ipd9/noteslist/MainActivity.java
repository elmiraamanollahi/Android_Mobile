package info.ipd9.noteslist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    String [] initialNotesList = { "Buy milk", "Learn Android", "Do the homeworks" };

    ArrayList<String> notesList = new ArrayList<>();

    ArrayAdapter adapter;
    private ListView lvNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        lvNotes = (ListView) findViewById(R.id.lvNotes);

        notesList.addAll(Arrays.asList(initialNotesList));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesList);
        lvNotes.setAdapter(adapter);
        //
        lvNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Log.d(TAG, "long clicked pos: " + pos);
                showDeleteDialog(pos);
                return true;
            }
        });
        //
        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Log.d(TAG, "clicked pos: " + pos);
                showEditDialog(pos);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        loadData();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        saveData();
    }

    private static final String filename = "data.txt";

    private void saveData() {
        String string = "";
        for (String note : notesList) {
            string += note + "\n";
        }

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving data", Toast.LENGTH_LONG).show();
        }
    }

    private void loadData() {
        FileInputStream inputStream;
        try {
            inputStream = openFileInput(filename);
            Scanner input = new Scanner(inputStream);
            notesList.clear();
            while (input.hasNextLine()) {
                String note = input.nextLine();
                notesList.add(note);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading data", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mi_add_note:
                Log.d(TAG, "Menu item Add Note selected");
                showAddNoteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddNoteDialog() {
        // FIXME: all strings should come from strings.xml !!!
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add note");
        // setup text input
        final EditText input = new EditText(this);
        // input type - regular text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // setup buttons
        builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: add item to list
                String note = input.getText().toString();
                Log.d(TAG, "Add note: " + note);
                notesList.add(note);
                Toast.makeText(MainActivity.this, "Note added", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showDeleteDialog(final int pos) {
        // FIXME: all strings should come from strings.xml !!!
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete note");
        // setup text input
        final TextView tvNote = new TextView(this);
        tvNote.setText(notesList.get(pos));
        // center TextView and give it more space on top
        tvNote.setPadding(0,25,0,0);
        tvNote.setGravity(Gravity.CENTER);
        // input type - regular text
        builder.setView(tvNote);
        // setup buttons
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: add item to list
                Log.d(TAG, "Delete note: " + notesList.get(pos));
                notesList.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void showEditDialog(final int pos) {
        // FIXME: all strings should come from strings.xml !!!
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit note");
        // setup text input
        final EditText input = new EditText(this);
        input.setText(notesList.get(pos));
        // input type - regular text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // setup buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: add item to list
                String note = input.getText().toString();
                Log.d(TAG, "Save note: " + note);
                notesList.set(pos, note);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

}
