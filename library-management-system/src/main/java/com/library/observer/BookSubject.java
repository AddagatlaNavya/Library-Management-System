package com.library.observer;

/**
 * Subject that notifies observers when book becomes available
 */
public interface BookSubject {
    void attach(BookObserver observer);
    void detach(BookObserver observer);
    void notifyObservers();
}

