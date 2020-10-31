package com.oliiarchyk.campaigns.forms;

import com.oliiarchyk.campaigns.models.Campaign;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

public class CampaignForm extends FormLayout {
    private Campaign camp;

    private TextField campaign = new TextField("Campaign");
    private TextField ranking = new TextField("Ranking");
    private DatePicker updateAt = new DatePicker("UpdateAt");
    private VerticalLayout fields = new VerticalLayout(campaign, ranking, updateAt);

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");

    Binder<Campaign> binder;

    @Autowired
    public CampaignForm() {
        addClassName("campaign-form");
        this.binder = new Binder<>(Campaign.class);
        binder.bindInstanceFields(this);
        add(fields, createButtonsLayout());
    }

    public void setCampaign(Campaign camp) {
        binder.setBean(camp);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickShortcut(Key.ESCAPE);
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        cancel.addClickListener(event -> fireEvent(new CancelEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new VerticalLayout(save, cancel, delete);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class CampaignFormEvent extends ComponentEvent<CampaignForm> {
        private Campaign camp;

        protected CampaignFormEvent(CampaignForm source, Campaign campaign) {
            super(source, false);
            this.camp = campaign;
        }

        public Campaign getCampaign() {
            return camp;
        }
    }

    public static class SaveEvent extends CampaignFormEvent {
        SaveEvent(CampaignForm source, Campaign campaign) {
            super(source, campaign);
        }
    }

    public static class DeleteEvent extends CampaignFormEvent {
        DeleteEvent(CampaignForm source, Campaign campaign) {
            super(source, campaign);
        }
    }

    public static class CancelEvent extends CampaignFormEvent {
        CancelEvent(CampaignForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}