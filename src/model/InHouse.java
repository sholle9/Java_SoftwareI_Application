package model;

/** This is the InHouse class which inherits from static Part.*/
public class InHouse extends Part{ //'extends' allows for inheritance from Part
    private int machineId;

    /** Constructor for the InHouse class with "super" to show it inherits from Part.
     This will call the additional parameter int machineId.
     @param id The id of the part.
     @param name The name of the part.
     @param price The price of the part.
     @param stock The inventory value for the part.
     @param min  The minimum value for the inventory of the part.
     @param max The maximum value for the inventory of the part.
     @param machineId The machine id of the InHouse part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max,int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** This is the getMachineId method.
     This gets the machineID for InHouse class.
     @return Returns the machine id for the in-house part.
     */
    public int getMachineId() {
        return machineId;
    }

    /** This is the setMachineId method.
     This sets the machineID for a part in the InHouse Class.
     @param machineId the new machineId for the in-house part.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
