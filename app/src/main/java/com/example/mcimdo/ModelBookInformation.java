package com.example.mcimdo;

public class ModelBookInformation {
    private int IdBook;
    private String Title;
    private String Author;
    private String[] Geners;
    private String Description;

    public int getIdBook() {
        return IdBook;
    }

    public void setIdBook(int idBook) {
        IdBook = idBook;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String[] getGeners() {
        return Geners;
    }

    public void setGeners(String[] geners) {
        Geners = geners;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
