package org.project.consumer;

import org.project.costumer.Customer;
import org.project.index.Index;

import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;

import static java.lang.Thread.sleep;


public class Consumer {
    private final BlockingQueue<Customer> blockingQueue;
    private final String fieldName;
    private final Index index;

    public Consumer(BlockingQueue<Customer> blockingQueue, String fieldName, Index index){
        this.blockingQueue = blockingQueue;
        this.fieldName = fieldName;
        this.index = index;
    }

    public void run() {
        waitForQueue();
        while (!this.blockingQueue.isEmpty()) {
            Customer customer = blockingQueue.remove();
            update(customer);
        }
    }

    private void waitForQueue() {
        try{
            sleep(2000);
        } catch (InterruptedException e) {
            System.exit(1);
        }
    }

    private void update(Customer customer){
        try {
            Field field = Customer.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(customer);
            String fileName = customer.getFileName();
            index.notify(value.toString(), fileName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.exit(1);
        }
    }
}
