package com.example.serviceworkerapplication;

// interface acting as wrapper to store response obtained by network call and to update the UI
public interface OnImageFetched<T> {
    void onImageFetchedThroughNetwork(T response);
}
