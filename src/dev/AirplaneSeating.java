package dev;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class AirplaneSeating {
		
	public static void main(String[] args) {
		
		int[][] seatingLayout = {{3,2}, {4,3}, {2,3}, {3,4}};
		char[] seatPreferenceInOrder = {'A','W','C'};
		
        System.out.println("Enter the no. of passengers waiting in queue:");
        Scanner scan = new Scanner(System.in);
		int noOfPassengers = scan.nextInt();
		HashMap<Integer,Integer> blockVsSpacer = new HashMap<Integer,Integer>();
		
		List<Seat> seats = assignSeatPreferences(seatingLayout, blockVsSpacer);
		designateSeatsToPassengers(seats, noOfPassengers, seatPreferenceInOrder);
		displaySeatingPlan(seats,blockVsSpacer);
	}

	public static List<Seat> assignSeatPreferences(int[][] seatingLayout, HashMap<Integer,Integer> blockVsSpacer) {

		List<Seat> seats = new LinkedList<Seat>();
		int noOfSeatBlocks = seatingLayout.length, sumOfColumns=0;
		
		for(int i=0; i<noOfSeatBlocks; i++) {
			int[] currentSeatingLayout = seatingLayout[i];
			int noOfRows = currentSeatingLayout[1];
			int noOfColumns = currentSeatingLayout[0];
			for(int j=0; j<noOfRows; j++) {
				for(int k=0; k<noOfColumns; k++) {
					char seatType = '-';
					if((i==0 && k==0) || ((i==noOfSeatBlocks-1) && (k==noOfColumns-1))){
						seatType = 'W';
					}
					else if(k==0 || (k==noOfColumns-1)){
						seatType = 'A';
					}
					else if(k>0 || (k<noOfColumns-1)){
						seatType = 'C';
					}
					Seat seat = new Seat(j,k,i,seatType);
					seats.add(seat);
				}
			}
			sumOfColumns+=noOfColumns;
			blockVsSpacer.put(i, sumOfColumns);
		}
		return seats;
	}
	
	public static void designateSeatsToPassengers(List<Seat> seats, int noOfPassengers, char[] seatPreferenceInOrder) {
		
        TreeMap<Integer,List<Seat>> xWiseSeatsMap = groupXWiseSeatsMap(seats);
        List<Seat> orderedSeats = new LinkedList<Seat>(); 
        int passengerSeatNo=0;

		for(int i=0;i<seatPreferenceInOrder.length;i++) {
            addOrderedSeats(orderedSeats, xWiseSeatsMap, seatPreferenceInOrder[i]);
        }
        	
        for(Seat seat: orderedSeats)
		{
    		if(passengerSeatNo == noOfPassengers) {
    			break;
    		}
    		seat.setPassengerNo(++passengerSeatNo);
		}
	}
	
	
	public static void displaySeatingPlan(List<Seat> seats, HashMap<Integer,Integer> blockVsSpacer) {
		
        System.out.println("-----------------------------------------------"); 
        TreeMap<Integer,List<Seat>> xWiseSeatsMap = groupXWiseSeatsMap(seats);
        
        for(Integer x: xWiseSeatsMap.keySet())
		{	
        	List<Seat> xWiseSeats = xWiseSeatsMap.get(x);
        	int xSpacer = 0;
    		int blockSpacer = 0;

        	for(Seat seat: xWiseSeats)
    		{    		
        		if(seat.getY() == 0 && (!(seat.getBlock() == 0))) {
        			int actualSpacer = blockVsSpacer.get(seat.getBlock()-1);
        			boolean isXSpaced = false;
        			while(xSpacer < actualSpacer) {
                		System.out.format("%3s","");
            			xSpacer++;
            			isXSpaced=true;
        			}
        			blockSpacer = adjustBlockSpacing(seat, blockSpacer, isXSpaced);
				}
        		 	
        		if(seat.getPassengerNo()!=null) {
            		System.out.format("%2d", seat.getPassengerNo());
        			System.out.print(" ");
        		}
        		else {		
        			System.out.print(" - ");
        		}
        		xSpacer++;
    		}
        	
			System.out.println();
		}
	}
	
	public static TreeMap<Integer,List<Seat>> groupXWiseSeatsMap(List<Seat> seats) 
	{
		TreeMap<Integer,List<Seat>> xWiseSeatsMap = new TreeMap<Integer,List<Seat>>();
		for(Seat seat: seats)
		{
			List<Seat> xWiseSeats = xWiseSeatsMap.get(seat.getX());
			if(xWiseSeats == null || xWiseSeats.isEmpty()) {
				xWiseSeats = new LinkedList<Seat>();
				xWiseSeatsMap.put(seat.getX(), xWiseSeats);
			}
			xWiseSeats.add(seat);
		}
		return xWiseSeatsMap;
	}
	
	public static void addOrderedSeats(List<Seat> orderedSeats, TreeMap<Integer,List<Seat>> xWiseSeatsMap, char seatPreference){
		
		for(List<Seat> xWiseSeats: xWiseSeatsMap.values())
		{	
        	for(Seat seat: xWiseSeats)
    		{
        		if(seat.getSeatType() == seatPreference) {
        			orderedSeats.add(seat);
        		}
    		}
		}	
	}
	
	public static int adjustBlockSpacing(Seat seat, int blockSpacer, boolean isXSpaced) 
	{
		if(isXSpaced) {
			while(blockSpacer<seat.getBlock()-1) {
    			System.out.print("  ");
    			blockSpacer++;
			}
		}
		blockSpacer++;		
		System.out.print("| ");	
		return blockSpacer;
	}

}

class Seat
{		
	public Seat(int x, int y, int block, char seatType) 
	{
		this.x = x;
		this.y = y;
		this.block = block;
		this.seatType = seatType;
	}
	
	int x,y,block;
	char seatType;
	Integer passengerNo;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getBlock() {
		return block;
	}
	public void setBlock(int block) {
		this.block = block;
	}
	public char getSeatType() {
		return seatType;
	}
	public void setSeatType(char seatType) {
		this.seatType = seatType;
	}
	public Integer getPassengerNo() {
		return passengerNo;
	}
	public void setPassengerNo(Integer passengerNo) {
		this.passengerNo = passengerNo;
	}
}


