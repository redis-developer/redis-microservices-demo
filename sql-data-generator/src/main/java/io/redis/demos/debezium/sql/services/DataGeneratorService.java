package io.redis.demos.debezium.sql.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DataGeneratorService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private String status="STOPPED";
    ScheduledExecutorService executorService = null;

    public DataGeneratorService() {
    }

    public void start(){
        log.info("Starting data generator....");
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::callBusinessLogic, 5, 10, TimeUnit.SECONDS );
        status="RUNNING";
    }

    public void stop(){
        log.info("Stopping data generator....");
        executorService.shutdownNow();
        executorService = null;
        status="STOPPED";
    }

    public String getState() {
        return this.status;
    }


    private void callBusinessLogic() {
        log.info("=== CALL BUSINESS LOGIC ===");

        executorService.schedule(() ->
                        createNewCustomer(1005, "John", "Doe", "jdoe@email.com"),
                3 , TimeUnit.SECONDS);

        executorService.schedule(() ->
                        addNewOrder(1002, 10, 109, "2019-04-01"),
                6 , TimeUnit.SECONDS);


        executorService.schedule(() ->
                        createNewCustomer(1006, "Jane", "Simon", "jsimon@email.com"),
                12 , TimeUnit.SECONDS);

        executorService.schedule(() ->
                        addNewOrder(1001, 8, 101, "2019-04-03"),
                12 , TimeUnit.SECONDS);


        executorService.schedule(() ->
                        createNewCustomer(1007, "Richard", "Dreyfus", "rdreyfus@email.com"),
                15 , TimeUnit.SECONDS);


        executorService.schedule(() ->
                        addNewOrder(1001, 8, 101, "2019-04-03"),
                19 , TimeUnit.SECONDS);
    }

    private void createNewCustomer( int id, String firstName, String lastName, String email ) {
        log.info(" == Delete/Create Customers ==");

        // Delete customer 1005, then recreate it
        jdbcTemplate.execute( String.format("DELETE FROM customers WHERE id = %s ", id) );
        jdbcTemplate.execute(String.format("INSERT INTO customers (id, first_name, last_name, email) "+
                "VALUES (%s, '%s', '%s', '%s')", id, firstName, lastName, email));


    }

    private void addNewOrder( int purchaser, int quantity, int product_id, String orderDate ) {
        log.info(" == Create Order ==");

        jdbcTemplate.execute(
                String.format("INSERT INTO orders (order_date, purchaser, quantity, product_id) VALUES ('%s', %s, %s, %s);",
                        orderDate, purchaser, quantity, product_id));


    }


}
