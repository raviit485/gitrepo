package com.example;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
@EnableScheduling
public class MyTasks {
    //Create rest template object
    RestTemplate restTemplate = new RestTemplate();
    //Id variable for adding vehicles
    private static int vehicleCount = 1;

    //Add vehicle at given interval
    @Scheduled(cron="*/10 * * * * *")
    public void addVehicle(){
        //Create random object
        Random random = new Random();
        //Generate Random string for make and model
        String makeModel = "";
        for(int i = 0; i < 20; i++)
            makeModel += (char)((char)random.nextInt(122 - 97) + 97);
        //Create new vehicle from random variables
        Vehicle vehicle = new Vehicle(vehicleCount, makeModel, random.nextInt(2016 - 1986) + 1986, random.nextInt(45000 - 15000) + 15000);
        //Send post request
        restTemplate.postForObject("http://localhost:8081/addVehicle/", vehicle, Vehicle.class);
        //Increment vehicle count
        vehicleCount++;
        //Print action details
        System.out.println("VEHICLE ADDED: " + vehicle.toJSON());
    }

    //Add update vehicles at interval
    @Scheduled(cron="25 * * * * *")
    public void updateVehicle(){
        try {
            //Create random object
            Random random = new Random();
            //Create new vehicle from random variables
            Vehicle vehicle = new Vehicle(random.nextInt(vehicleCount - 1) + 1, "MODIFIED-VEHICLE-MAKE-MODEL", 99999, 0.0);
            //Send put request
            restTemplate.put("http://localhost:8080/updateVehicle/", vehicle, Vehicle.class);
            //Send get request to verify updated vehicle
            Vehicle updatedVehicle = restTemplate.getForObject("http://localhost:8080/getVehicle/" + vehicle.getId(), Vehicle.class);
            //Print action details
            System.out.println("VEHICLE WITH ID " + vehicle.getId() + " MODIFIED TO: " + updatedVehicle.toJSON());
        } catch (Exception e){
            System.out.println("VEHICLE INVENTORY IS EMPTY: NO VEHICLE CAN BE UPDATED");
        }
    }

    //Add vehicle at given interval
    @Scheduled(cron="45 * * * * *")
    public void deleteVehicle(){
        try {
            //Create random object
            Random random = new Random();
            int id = random.nextInt(vehicleCount - 1) + 1;
            //Get the planned deleted vehicle's details and print action
            Vehicle vehicleToDelete = restTemplate.getForObject("http://localhost:8080/getVehicle/" + id, Vehicle.class);
            System.out.println("DELETED VEHICLE: " + vehicleToDelete.toJSON());
            //Delete car with given id
            restTemplate.delete("http://localhost:8080/deleteVehicle/" + id);
            //Decrement total vehicle count
            vehicleCount--;
        }catch (Exception e){
            System.out.println("VEHICLE INVENTORY IS EMPTY: NO VEHICLE CAN BE DELETED");
        }
    }
}