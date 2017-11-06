package com.example.nublikaska.todolist;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SampleDBAdapter DBAdapter;
    ArrayAdapter<String> arrayAdapter;
    ListView toDoList;
    Button buttonSave;
    EditText element;
    ArrayList<Long> indexs;
    ArrayList arrayList;
    int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoList = (ListView) findViewById(R.id.myListView);
        toDoList.setOnItemClickListener(new MainActivity.ItemList());
        toDoList.setOnItemLongClickListener(new MainActivity.ItemList());
        registerForContextMenu(toDoList);

        buttonSave = (Button) findViewById(R.id.save_button);
        buttonSave.setOnClickListener(ClickButton);

        element = (EditText) findViewById(R.id.edit_text);

        indexs = new ArrayList<Long>();
        arrayList = new ArrayList<String>();

        DBAdapter = new SampleDBAdapter(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateToDoList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, Menu.FIRST, Menu.NONE, "Изменить");
        menu.add(0, Menu.FIRST + 1, Menu.NONE, "Удалить");
        menu.add(0, Menu.FIRST + 2, Menu.NONE, "Удалить все");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case (Menu.FIRST) :
                DBAdapter.open();
                DBAdapter.UpdateItem(element.getText().toString(), indexs.get(currentIndex));
                DBAdapter.close();
                UpdateToDoList();
                break;
            case (Menu.FIRST + 1) :
                DBAdapter.open();
                DBAdapter.removeEntry(indexs.get(currentIndex));
                indexs.remove(currentIndex);
                arrayList.remove(currentIndex);
                DBAdapter.close();
                arrayAdapter.notifyDataSetChanged();
                break;
            case (Menu.FIRST + 2) :
                DBAdapter.open();
                Cursor cursor = DBAdapter.getAllEntries();
                while (cursor.moveToNext()) {
                    indexs.remove(cursor.getLong(cursor.getColumnIndex(DBAdapter.COLUMN_ID)));
                    arrayList.remove(cursor.getString(cursor.getColumnIndex(DBAdapter.COLUMN_DO)));
                    DBAdapter.removeEntry(cursor.getLong(cursor.getColumnIndex(DBAdapter.COLUMN_ID)));
                }
                arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, arrayList);
                toDoList.setAdapter(arrayAdapter);
                DBAdapter.close();
                UpdateToDoList();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void UpdateToDoList() {
        arrayList.clear();
        DBAdapter.open();
        Cursor cursor = DBAdapter.getAllEntries();
        while (cursor.moveToNext()) {
            indexs.add(cursor.getLong(cursor.getColumnIndex(DBAdapter.COLUMN_ID)));
            arrayList.add(cursor.getString(cursor.getColumnIndex(DBAdapter.COLUMN_DO)));
        }
        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, arrayList);
        toDoList.setAdapter(arrayAdapter);
        DBAdapter.close();
    }

    View.OnClickListener ClickButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case (R.id.save_button):
                    String str = element.getText().toString().replace(" ", "");
                    if (!str.equals("")) {
                        //Toast.makeText(MainActivity.this, "Мама, он со мной разговаривает", Toast.LENGTH_SHORT);

                        DBAdapter.open();
                        indexs.add(DBAdapter.insertEntry(element.getText().toString()));
                        DBAdapter.close();

                        arrayList.add(element.getText().toString());
                        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        toDoList.setAdapter(arrayAdapter);
                    }
                    break;
            }
        }
    };

    /////////////////////////////////class ItemList///////////////////////////////////////////

    class ItemList implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this, String.valueOf(indexs.get(position)), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            currentIndex = position;
            return false;
        }
    }
}
