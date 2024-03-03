package ParkingLot.commands;

import ParkingLot.controllers.TicketController;
import ParkingLot.dtos.ResponseStatus;
import ParkingLot.dtos.requestDto.GenerateTicketRequestDto;
import ParkingLot.dtos.responseDto.GenerateTicketResponseDto;
import ParkingLot.models.VehicleType;
import ParkingLot.reposetories.GateReposetory;
import ParkingLot.reposetories.ParkingLotReposetory;
import ParkingLot.reposetories.TicketReposetory;
import ParkingLot.reposetories.VehicleReposetory;
import ParkingLot.services.TicketService;
import ParkingLot.strategies.spotAssignmentStrategy.SpotAssignmentStrategy;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GenerateTicketCommand implements Command {
    private TicketController ticketController;
    private Scanner sc;



    public GenerateTicketCommand(GateReposetory gateReposetory, VehicleReposetory vehicleReposetory, SpotAssignmentStrategy spotAssignmentStrategy, ParkingLotReposetory parkingLotReposetory, TicketReposetory ticketReposetory) {
        this.ticketController = new TicketController(new TicketService(gateReposetory, vehicleReposetory, spotAssignmentStrategy, parkingLotReposetory, ticketReposetory));
        sc = new Scanner(System.in);
    }


    @Override
    public void execute(){
        GenerateTicketRequestDto requestTicket = new GenerateTicketRequestDto();
        System.out.println("Enter the parking Lot Id: ");
        String parkingLotIdInput = sc.next();
        Long parkingLotId = Long.parseLong(parkingLotIdInput);
        requestTicket.setParkingLotId(parkingLotId);
        System.out.println("Enter the vehicle No: ");
        requestTicket.setVehicleNo(sc.next());
        System.out.println("Enter the Vehicle Type, Bike/Car: ");
        String type = sc.next();
        if (type.equals("Bike")) {
            requestTicket.setVehicleType(VehicleType.BIKE);
        } else if (type.equals("Car")) {
            requestTicket.setVehicleType(VehicleType.CAR);
        }
        GenerateTicketResponseDto responseTicket = ticketController.generateTicket(requestTicket);
        if (responseTicket.getResponseStatus().equals(ResponseStatus.FAILURE)) {
            System.out.println("##################################################################################");
            System.out.println(responseTicket.getMessage());
            System.out.println("##################################################################################");
            System.out.println();
        } else {
            System.out.println("##################################################################################");
            System.out.println(responseTicket.getMessage());
            System.out.println("Ticket Id --> " + responseTicket.getTicketID());
            System.out.println("Spot No --> " + responseTicket.getSpotNo());
            System.out.println("Entry Gate No --> " + responseTicket.getGateNo());
            System.out.println("Generated By --> " + responseTicket.getOperatorName());
            System.out.println("Ticket generated time --> " + DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(responseTicket.getTime()));
            System.out.println("##################################################################################");
            System.out.println();
        }

    }

    @Override
    public boolean matches(String input){
        if (input.equals(CommandKeywords.GENERATE_TICKET)){
            return true;
        }
        return false;
    }
}
