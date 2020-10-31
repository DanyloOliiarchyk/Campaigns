package com.oliiarchyk.campaigns.views;


import com.oliiarchyk.campaigns.models.Campaign;
import com.oliiarchyk.campaigns.services.CampaignService;
import com.oliiarchyk.campaigns.forms.CampaignForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@CssImport("./styles/shared-styles.css")
public class CampaignView extends VerticalLayout {

    private CampaignForm form;
    private Grid<Campaign> grid = new Grid<>(Campaign.class);
    private TextField filter = new TextField();
    private final CampaignService service;

    @Autowired
    public CampaignView(CampaignService service) {
        this.service = service;
        Paragraph paragraph = new Paragraph("Campaign List");
        paragraph.setClassName("paragraph");
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new CampaignForm();
        form.addListener(CampaignForm.SaveEvent.class, this::saveCampaign);
        form.addListener(CampaignForm.DeleteEvent.class, this::deleteCampaign);
        form.addListener(CampaignForm.CancelEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(paragraph, getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteCampaign(CampaignForm.DeleteEvent event) {
        service.delete(event.getCampaign());
        updateList();
        closeEditor();
    }

    private void saveCampaign(CampaignForm.SaveEvent event) {
        service.save(event.getCampaign());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setCampaign(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    public void configureGrid() {
        grid.addClassName("campaign-grid");
        grid.setSizeFull();
        grid.setColumns("campaign", "ranking", "updateAt");
        grid.addComponentColumn(this::buildEditButton);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editCampaign(e.getValue()));
    }

    private Button buildEditButton(Campaign campaign) {
        Button button = new Button("edit");
        button.addClickListener(e -> button.getUI().ifPresent(ui -> ui.navigate("edit" + "/" + campaign.getId())));
        return button;
    }

    private void editCampaign(Campaign campaign) {
        if (campaign == null) {
            closeEditor();
        } else {
            form.setCampaign(campaign);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolBar() {
        filter.setClearButtonVisible(true);
        filter.setPlaceholder("Find ...");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> updateList());

        Button addNew = new Button("Add campaign", click -> addCampaign());
        HorizontalLayout toolbar = new HorizontalLayout(filter, addNew);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addCampaign() {
        grid.asSingleSelect().clear();
        editCampaign(new Campaign());
    }

    public void updateList() {
        grid.setItems(service.getAll(filter.getValue()));
    }
}
