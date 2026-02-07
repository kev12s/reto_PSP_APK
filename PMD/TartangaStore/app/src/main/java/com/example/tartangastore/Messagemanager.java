package com.example.tartangastore;

import android.content.Context;

public class Messagemanager {
    private Context context;
    private int[] messageIds;
    private int currentIndex;

    public Messagemanager(Context context, int[] messageIds) {
        this.context = context;
        this.messageIds = messageIds;
        this.currentIndex = 0;
    }

    public String getCurrentMessage() {
        return context.getString(messageIds[currentIndex]);
    }

    public void next() {
        currentIndex = (currentIndex + 1) % messageIds.length;
    }

    public void previous() {
        currentIndex = (currentIndex - 1 + messageIds.length) % messageIds.length;
    }

    public boolean hasNext() {
        return currentIndex < messageIds.length - 1;
    }

    public boolean hasPrevious() {
        return currentIndex > 0;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getTotalMessages() {
        return messageIds.length;
    }
}