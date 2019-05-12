package com.example.aprojectktomkow.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class NoteDAO
{
    private DBHelper dbHelper;

    public NoteDAO(Context context)
    {
        dbHelper = new DBHelper(context);
    }

    // wstawienie nowej notatki do bazy danych
    public void insertNote(final Note note)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Notes.Columns.NOTE_TEXT, note.getNoteText());

        dbHelper.getWritableDatabase().insert(Notes.TABLE_NAME, null, contentValues);
    }

    // pobranie notatki na podstawie jej id
    public Note getNoteById(final int id)
    {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + Notes.TABLE_NAME + " where " + Notes.Columns.NOTE_ID + " = " + id, null);
        if (cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            return mapCursorToNote(cursor);
        }
        return null;
    }

    // aktualizacja notatki w bazie
    public void updateNote(final Note note)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Notes.Columns.NOTE_TEXT, note.getNoteText());

        dbHelper.getWritableDatabase().update(Notes.TABLE_NAME,
                contentValues,
                " " + Notes.Columns.NOTE_ID + " = ? ",
                new String[]{note.getId().toString()}
        );
    }

    // usunięcie notatki z bazy
    public void deleteNoteById(final Integer id)
    {
        dbHelper.getWritableDatabase().delete(Notes.TABLE_NAME,
                " " + Notes.Columns.NOTE_ID + " = ? ",
                new String[]{id.toString()}
        );
    }

    // pobranie wszystkich notatek
    public List getAllNotes()
    {
        Cursor cursor = dbHelper.getReadableDatabase().query(Notes.TABLE_NAME,
                new String[]{Notes.Columns.NOTE_ID, Notes.Columns.NOTE_TEXT},
                null, null, null, null, null
        );

        List results = new ArrayList<>();

        if (cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                results.add(mapCursorToNote(cursor));
            }
        }

        return results;
    }

    // zamiana cursora na obiekt notatki
    private Note mapCursorToNote(final Cursor cursor)
    {
        int idColumnId = cursor.getColumnIndex(Notes.Columns.NOTE_ID);
        int textColumnId = cursor.getColumnIndex(Notes.Columns.NOTE_TEXT);

        Note note = new Note();
        note.setId(cursor.getInt(idColumnId));
        note.setNoteText(cursor.getString(textColumnId));
        return note;
    }
}
