package com.example.aprojectktomkow.DB;

public class Note
{
    private Integer id;
    private String noteText;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNoteText()
    {
        return noteText;
    }

    public void setNoteText(String noteText)
    {
        this.noteText = noteText;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(id).append(": ");
        sb.append(noteText);
        return sb.toString();
    }
}
