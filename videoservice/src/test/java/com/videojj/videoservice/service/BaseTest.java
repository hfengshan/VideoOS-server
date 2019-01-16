package com.videojj.videoservice.service;

import org.mockito.Mockito;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.ArgumentMatchers.any;

public class BaseTest {
    protected void mockTransactionTemplate(TransactionTemplate transactionTemplate){

        Mockito.doCallRealMethod().when(transactionTemplate).setTransactionManager(any());
        Mockito.doCallRealMethod().when(transactionTemplate).execute(any());
        transactionTemplate.setTransactionManager(new AbstractPlatformTransactionManager() {
            @Override
            protected Object doGetTransaction() throws TransactionException {
                return null;
            }

            @Override
            protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {

            }

            @Override
            protected void doCommit(DefaultTransactionStatus status) throws TransactionException {

            }

            @Override
            protected void doRollback(DefaultTransactionStatus status) throws TransactionException {

            }
        });
    }
}
