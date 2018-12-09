package com.michael;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.shared.ui.ContentMode;//Had to add this
import com.vaadin.ui.Slider;//Had to add this
import com.vaadin.ui.Grid;//Had to add this manually
import com.vaadin.ui.GridLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        Connection connection = null;
        
        String connectionString = "jdbc:sqlserver://db4class.database.windows.net:1433;" + 
        "database=dbMartyPlanners;" + 
        "user=michael@db4class;" + 
        "password=CloudDev123;" + 
        "encrypt=true;" + 
        "trustServerCertificate=false;" + 
        "hostNameInCertificate=*.database.windows.net;" +
        "loginTimeout=30;";

        final VerticalLayout layout = new VerticalLayout();
        HorizontalLayout h = new HorizontalLayout();
        VerticalLayout v = new VerticalLayout();
        Label logo = new Label("<H1>Marty Party Planners</H1> <p/> <h3>Please enter the details below and click Book</h3><br>", ContentMode.HTML);
      
        try 
        {
            
            // Connect with JDBC driver to a database
            System.out.println("Attempting to connect....");
            connection = DriverManager.getConnection(connectionString);
            System.out.println("Success..connected..");

	        // Add a label to the web app with the message and name of the database we connected to 
	        layout.addComponent(new Label("Connected to database: " + connection.getCatalog()));
            
            // Connect with JDBC driver to a database
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM roomTable;");
            // The ResultSet places the current row directly before the first row. Calling next() moves it to the next row, and returns true if there is a row. False if there is no row there.
            /*while(rs.next())
            {              
                if(rs.getBoolean("alcoholAllowed") == true) // If the current row has true for the boolean column 'Alcohol Allowed'
                {  
                        // Print out a message made up from the columns in the database 
                        System.out.println(rs.getString("room") + " with " + rs.getString("capacity") + " no children in " + rs.getString("feature"));
                }
                else
                {
                        System.out.println(rs.getString("room") + " with " + rs.getString("capacity") + " has chidren in " + rs.getString("feature"));
                }
            }
            */

            
            // Convert the resultset that comes back into a List - we need a Java class to represent the data (Room.java in this case)
            List<Room> rooms = new ArrayList<Room>();
            // While there are more records in the resultset
            while(rs.next())
            {
	        // Add a new room is instantiated with the fields from the record (that we want, we might not want all the fields)
            rooms.add(new Room(rs.getString("Room"), 
            rs.getString("Capacity"), 
            rs.getString("Feature"), 
            rs.getString("AlcoholAllowed")));
        }  
        // Add my component, grid is templated with Room
        Grid<Room> myGrid = new Grid<>();
        // Set the items (List)
        myGrid.setItems(rooms);
        // Configure the order and the caption of the grid
        myGrid.addColumn(Room::getRoomName).setCaption("Room");
        myGrid.addColumn(Room::getCapacity).setCaption("Capacity");
        myGrid.addColumn(Room::getFeature).setCaption("Feature");
        myGrid.addColumn(Room::getAlcoholAllowed).setCaption("Alcohol Allowed");

// Add the grid to the list
    layout.addComponent(myGrid);

    final TextField name = new TextField();
    name.setCaption("Name of party");

    Slider s = new Slider("How many people are invited to this party", 10, 500);
    s.setValue(10.0);
    s.setWidth(s.getMax()+"px");

    //s.addValueChangeListener(e ->{
      //  double x = s.getValue();
    //});
    
    ComboBox<String> children = new ComboBox<String>("Children Attending");
    children.setItems("Yes", "No");

    Label initiaLabel = new Label("Your party is not yet booked");
    Button button = new Button("Book");
    v.addComponent(initiaLabel);
    button.addClickListener(e -> {
        v.replaceComponent(initiaLabel, new Label("Thanks " + name.getValue() 
                + ", Marty says it works!"));
    });

    //List<Room> rooms = new ArrayList<Room>();

    /*Grid<Room> myGrid = new Grid<>();
    myGrid.setItems(rooms);
    myGrid.addColumn(Room::getRoomName).setCaption("Room");
    myGrid.addColumn(Room::getCapacity).setCaption("Capacity");
    myGrid.addColumn(Room::getFeature).setCaption("Feature");
    myGrid.addColumn(Room::getAlcoholAllowed).setCaption("Alcohol Allowed");
    myGrid.setSelectionMode(SelectionMode.MULTI);
    myGrid.setSizeFull();*/

    h.addComponents(name, s, children);
    v.addComponents(button, initiaLabel);
    layout.addComponents(logo, h, v, myGrid);




} 
catch (Exception e) 
{
    // This will show an error message if something went wrong
    layout.addComponent(new Label(e.getMessage()));
}

        
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
