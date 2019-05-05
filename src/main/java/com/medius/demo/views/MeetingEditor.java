package com.medius.demo.views;

import com.medius.demo.entities.Customer;
import com.medius.demo.entities.Meeting;
import com.medius.demo.entities.MeetingResult;
import com.medius.demo.repositories.CustomerRepository;
import com.medius.demo.repositories.MeetingRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@SpringComponent
@UIScope
public class MeetingEditor extends VerticalLayout implements KeyNotifier {

    private final MeetingRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * The currently edited meeting and customer
     */
    private Meeting meeting;
    private Customer customer;

    /* Fields to edit properties in Meeting entity */
    // TODO - Add Meeting Result as an entity and add contracts, bills and next meeting arrangements to the app
    TextField location = new TextField("Location");
    ComboBox<MeetingResult> meetingResult = new ComboBox<>("Meeting Result");
    DatePicker startDate = new DatePicker("Start Date");
    TimePicker startTime = new TimePicker("Start Time");
    DatePicker endDate = new DatePicker("End Date");
    TimePicker endTime = new TimePicker("End Time");

    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Meeting> binder = new Binder<>(Meeting.class);
    private ChangeHandler changeHandler;

    @Autowired
    public MeetingEditor(MeetingRepository repository) {
        this.repository = repository;
        HorizontalLayout locationAndResult = new HorizontalLayout(location, meetingResult);
        HorizontalLayout startDateTimeLayout = new HorizontalLayout(startDate, startTime);
        HorizontalLayout endDateTimeLayout = new HorizontalLayout(endDate, endTime);
        add(locationAndResult, startDateTimeLayout, endDateTimeLayout, actions);

        meetingResult.setItems(MeetingResult.values());

        // bind fields with backend data
        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and cancel
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editMeeting(null));
        setVisible(false);
    }

    void delete() {
        repository.delete(meeting);
        changeHandler.onChange();
    }

    void save() {
        if (meeting.getStartDate() != null && meeting.getEndDate() != null) {
            repository.save(meeting);
            Notification.show("Meeting successfully saved!").setDuration(1000);
        }

        if (customer != null) {
           customerRepository.save(customer);
        }
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editMeeting(Meeting m) {
        if (m == null) {
            setVisible(false);
            return;
        }

        // check if meeting with this ID actually exists
        boolean persisted = m.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            meeting = repository.findById(m.getId()).get();
        }
        else {
            meeting = m;
        }
        cancel.setVisible(persisted);

        binder.setBean(meeting);

        setVisible(true);

        // Focus location initially
        location.focus();
    }

    public final void addMeeting(Meeting m, Customer c) {
        if (m == null) {
            setVisible(false);
            return;
        }

        boolean persisted = m.getId() != null;
        if (persisted) {
            meeting = repository.findById(m.getId()).get();
        } else {
            meeting = m;
            customer = c;
            Set<Customer> customersSet = new HashSet<Customer>();
            customersSet.add(customer);
            meeting.setCustomers(customersSet);
            customer.getMeetings().add(meeting);
            delete.setVisible(false);
        }
        binder.setBean(meeting);

        setVisible(true);

        // Focus location initially
        location.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete is clicked
        changeHandler = h;
    }

}