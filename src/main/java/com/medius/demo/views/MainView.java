package com.medius.demo.views;

import com.medius.demo.entities.Customer;
import com.medius.demo.entities.Meeting;
import com.medius.demo.entities.MeetingResult;
import com.medius.demo.repositories.CustomerRepository;
import com.medius.demo.repositories.MeetingRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Route
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends VerticalLayout {
    private final CustomerRepository repo;
    private final MeetingRepository meetingRepository;
    final Grid<Customer> grid;
    final CustomerEditor editor;
    final MeetingEditor meetingEditor;
    final TextField filter;
    private final Button addNewBtn;
    Grid<Meeting> meetingsGrid;
    Label meetingsLabel;
    List<GridSortOrder<Meeting>> order;

    public MainView(CustomerRepository repo, MeetingRepository meetingRepository, CustomerEditor editor, MeetingEditor meetingEditor) {
        this.repo = repo;
        this.meetingRepository = meetingRepository;
        this.editor = editor;
        this.meetingEditor = meetingEditor;
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS_CIRCLE.create());

        // layouts
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);

        grid.setColumns("firstName", "lastName");
        grid.addComponentColumn(customer -> new Button("Show meetings", VaadinIcon.TABLE.create(), e -> showMeetings(customer)));
        grid.addComponentColumn(customer -> new Button("Add a meeting", VaadinIcon.PLUS_CIRCLE.create(),
                e -> newMeeting(customer)));
        HorizontalLayout gridLayout = new HorizontalLayout(grid, editor);
        gridLayout.setSizeFull();

        if (editor.isVisible()) {
            grid.setSizeFull();
        }

        add(actions, gridLayout);

        filter.setPlaceholder("Filter by last name");
        filter.setClearButtonVisible(true);

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        // Double click on customer in grid allows us to edit this customer
        grid.addItemDoubleClickListener(e -> {
            editor.editCustomer(e.getItem());
        });

        // Instantiate and edit new Customer when the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "", "", "")));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
           editor.setVisible(false);
           listCustomers(filter.getValue());
        });

        // Initialize listing
        listCustomers(null);

        // initialize grid to show customer's meetings
        meetingsGrid = new Grid<>(Meeting.class);
        meetingsGrid.setVisible(false);
        meetingsLabel = new Label();
        meetingsGrid.setColumns("location", "meetingResult", "startDate", "startTime", "endDate", "endTime");

        // Set sorting: new meetings on top of the grid
        order = new ArrayList<>();
        order.add(new GridSortOrder<>(meetingsGrid.getColumnByKey("startDate"), SortDirection.DESCENDING));

        // Layout
        HorizontalLayout meetingsGridLayout = new HorizontalLayout(meetingsGrid, meetingEditor);
        meetingsGridLayout.setSizeFull();
        add(meetingsLabel, meetingsGridLayout);
    }

    private void showMeetings(Customer customer) {
        meetingsLabel.setVisible(true);
        meetingsLabel.setText("Showing meetings for customer: " + customer.getFirstName() + " " + customer.getLastName());

        meetingsGrid.sort(order);

        // Fill meetings grid with selected user's data
        meetingsGrid.setVisible(true);
        Set<Meeting> meetings = customer.getMeetings();
        meetingsGrid.setItems(meetings);

        // If meeting is double-clicked, we can edit it
        meetingsGrid.addItemDoubleClickListener(e -> {
           meetingEditor.editMeeting(e.getItem());
        });

        // Workaround to refresh meetingsGrid items
        // TODO Fix this so the meetingsGrid doesn't need to be reopened...
        meetingEditor.setChangeHandler(() -> {
            meetingEditor.setVisible(false);
            listCustomers(null);
            meetingsGrid.setVisible(false);
            meetingsGrid.setItems(customer.getMeetings());

            meetingsLabel.setVisible(false);
        });
    }

    private void newMeeting(Customer customer) {
        meetingsLabel.setText("Adding a new meeting for customer: " + customer.getFirstName() + " " + customer.getLastName());
        meetingEditor.addMeeting(new Meeting("", MeetingResult.None,null, null, null, null), customer);
    }

    private void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        } else {
            grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }
}
