package com.library.transaction;

/**
 * Command interface for transaction operations
 */
interface TransactionCommand {
    void execute();
    void undo();
}