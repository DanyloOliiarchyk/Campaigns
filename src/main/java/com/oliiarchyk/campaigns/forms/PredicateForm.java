package com.oliiarchyk.campaigns.forms;


import com.oliiarchyk.campaigns.models.Predicate;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

public class PredicateForm extends VerticalLayout {
    private Predicate predicate;

    private ComboBox<Predicate.Type> type = new ComboBox<>("Type");
    private TextField attribute = new TextField("Attribute");
    private TextArea conditions = new TextArea("Conditions");
    private VerticalLayout fields = new VerticalLayout(type, conditions, attribute);

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");

    Binder<Predicate> binder;

    @Autowired
    public PredicateForm() {
        addClassName("campaign-form");
        this.binder = new Binder<>(Predicate.class);
        binder.bindInstanceFields(this);
        type.setItems(Predicate.Type.values());
        add(fields, createButtonsLayout());
    }

    public void setPredicate(Predicate p) {
        binder.setBean(p);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickShortcut(Key.ESCAPE);
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new PredicateForm.DeleteEvent(this, binder.getBean())));
        cancel.addClickListener(event -> fireEvent(new PredicateForm.CancelEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, cancel, delete);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new PredicateForm.SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class PredicateFormEvent extends ComponentEvent<PredicateForm> {
        private Predicate predicate;

        protected PredicateFormEvent(PredicateForm source, Predicate predicate) {
            super(source, false);
            this.predicate = predicate;
        }

        public Predicate getPredicate() {
            return predicate;
        }
    }

    public static class SaveEvent extends PredicateForm.PredicateFormEvent {
        SaveEvent(PredicateForm source, Predicate predicate) {
            super(source, predicate);
        }
    }

    public static class DeleteEvent extends PredicateForm.PredicateFormEvent {
        DeleteEvent(PredicateForm source, Predicate predicate) {
            super(source, predicate);
        }
    }

    public static class CancelEvent extends PredicateForm.PredicateFormEvent {
        CancelEvent(PredicateForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}