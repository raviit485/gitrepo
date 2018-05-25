package com.example;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Controller {
    //Create Array of content in list for convenience
    public static ArrayList<Vehicle> inventory;

    //Get vehicle by ID function
    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable("id") int id) throws IOException{
        //Get current inventory
        inventory = readCurrentInventory();
        for (int i = 0; i < inventory.size(); i++){
            //If id is a match, return that vehicle
            if(inventory.get(i).getId() == id)
                return inventory.get(i);
        }
        return null;
    }

    //Add vehicle to text file list
    @RequestMapping(value = "/addVehicle/", method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException{
        //Create object mapper variable
        ObjectMapper mapper = new ObjectMapper();
        //Create file writer to write to file with append mode as true
        FileWriter output = new FileWriter("./inventory.txt", true);
        //Set vehicle id to the number of vehicles in inventory + 1
        LineNumberReader  lnr = new LineNumberReader(new FileReader(new File("./inventory.txt")));
        lnr.skip(Long.MAX_VALUE);
        newVehicle.setId(lnr.getLineNumber() + 1);
        lnr.close();
        //serialize and write to file
        mapper.writeValue(output, newVehicle);
        //Write vehicle object to file as string
        FileUtils.writeStringToFile(new File("./inventory.txt"), System.lineSeparator(), UTF_8, true);
        return newVehicle;
    }

    //Add vehicle to text file list
    @RequestMapping(value = "/updateVehicle/", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle updatedVehicle) throws IOException{
        //Get current inventory
        inventory = readCurrentInventory();
        for (int i = 0; i < inventory.size(); i++){
            //Replace current vehicle at given index with new vehicle
            if(inventory.get(i).getId() == updatedVehicle.getId())
                inventory.set(i, updatedVehicle);
        }
        //Write new inventory
        updateInventory(inventory);
        //Return the updated vehicle
        return updatedVehicle;
    }

    //Delete Vehicle function
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteVehicle/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException {
        //Response
        @SuppressWarnings("rawtypes")
		ResponseEntity response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        //Get current inventory
        inventory = readCurrentInventory();
        for (int i = 0; i < inventory.size(); i++){
            //If id is a match, remove vehicle at id
            if(inventory.get(i).getId() == id) {
                inventory.remove(i);
                response = new ResponseEntity<String>(HttpStatus.ACCEPTED);
            }
        }
        //Write new inventory
        updateInventory(inventory);
        //Return response message
        return response;
    }

    //Get inventory from text file
    public static ArrayList<Vehicle> readCurrentInventory(){
        try {
            //Create scanner object to read file
            @SuppressWarnings("resource")
			Scanner s = new Scanner(new File("./inventory.txt"));
            //Create Object mapper
            ObjectMapper objectMapper = new ObjectMapper();
            //Create list to hold inventory
            ArrayList<Vehicle> currentInventory = new ArrayList<Vehicle>();
            //Iterate over each line in file
            while (s.hasNext())
                currentInventory.add(objectMapper.readValue(s.nextLine(), Vehicle.class));
            return currentInventory;
        }catch (Exception e){return null;}
    }

    //Add new altered inventory
    public void updateInventory(ArrayList<Vehicle> x){
        try {
            //Erase Inventory and write new order inventory
            new FileOutputStream("./inventory.txt", false).close();
            for (Vehicle each : x)
                addVehicle(each);
        } catch (Exception e){}
    }
}