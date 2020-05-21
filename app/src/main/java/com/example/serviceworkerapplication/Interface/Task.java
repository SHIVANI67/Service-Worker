package com.example.serviceworkerapplication.Interface;

// Task interface having two method definition
public interface Task<T> {
    T onExecuteTask(String url);
    void onTaskComplete(T result);
}
