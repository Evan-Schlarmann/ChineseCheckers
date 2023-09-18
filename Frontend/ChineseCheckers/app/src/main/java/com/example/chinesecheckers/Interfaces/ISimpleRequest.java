package com.example.chinesecheckers.Interfaces;

/**
 * Interface for a volley request.
 */
public interface ISimpleRequest {
    public void createRequestObject();
    public void sendRequest(int method, String URL);
}
