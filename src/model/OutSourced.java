package model;

/** This is the OutSourced class which inherits from static Part.*/
public class OutSourced extends Part{ //extends Part allows for inheritance from Part

    private String companyName;

    /** Constructor for the Outsourced class with "super" to show it inherits from Part.
     This will call the additional parameter String companyName to account for the Radio Button Outsourced.
     @param id The id of the part.
     @param name The name of the part.
     @param price The price of the part.
     @param stock The inventory value for the part.
     @param min  The minimum value for the inventory of the part.
     @param max The maximum value for the inventory of the part.
     @param companyName The company name of the outsourced part.
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    //Setter and Getter

    /** This is the getCompanyName method.
     This gets the nameCompany for Outsourced class.
     @return Returns the company Name for the outsourced part.
     */
    public String getCompanyName() {
        return companyName;
    }

    /** This is the setCompanyName method.
     This gets the nameCompany for Outsourced class.
     @param companyName the new company name for the outsourced part.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
