package com.oliiarchyk.campaigns.views;

import com.oliiarchyk.campaigns.models.Predicate;
import com.oliiarchyk.campaigns.services.PredicateService;
import com.oliiarchyk.campaigns.forms.PredicateForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route("edit")
@CssImport("./styles/shared-styles.css")
public class PredicateView extends VerticalLayout implements HasUrlParameter<String> {

    private PredicateForm form;
    private Grid<Predicate> grid = new Grid<>(Predicate.class);
    private final PredicateService service;
    public Long camp_id;

    @Autowired
    public PredicateView(PredicateService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new PredicateForm();
        form.addListener(PredicateForm.SaveEvent.class, this::savePredicate);
        form.addListener(PredicateForm.DeleteEvent.class, this::deletePredicate);
        form.addListener(PredicateForm.CancelEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    public void configureGrid() {
        grid.addClassName("campaign-grid");
        grid.setSizeFull();
        grid.addColumn(predicate -> predicate.getAttribute()).setHeader("Predicates:");

        grid.removeColumnByKey("attribute");
        grid.removeColumnByKey("conditions");
        grid.removeColumnByKey("type");
        grid.removeColumnByKey("id");
        grid.removeColumnByKey("campId");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editPredicate(e.getValue()));
    }

    private void editPredicate(Predicate predicate) {
        if (predicate == null) {
            closeEditor();
        } else {
            form.setPredicate(predicate);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolBar() {
        Button addNew = new Button("Add predicate", click -> addPredicate());
        Button exit = new Button("To campaigns");
        exit.addClickListener(e -> exit.getUI().ifPresent(ui -> ui.navigate("")));
        HorizontalLayout toolbar = new HorizontalLayout(addNew, exit);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addPredicate() {
        grid.asSingleSelect().clear();
        editPredicate(new Predicate());
    }

    private void closeEditor() {
        form.setPredicate(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void deletePredicate(PredicateForm.DeleteEvent event) {
        service.delete(event.getPredicate());
        updateList();
        closeEditor();
    }

    private void savePredicate(PredicateForm.SaveEvent event) {
        event.getPredicate().setCampId(getCamp_id());
        service.save(event.getPredicate());
        updateList();
        closeEditor();
    }

    private void updateList() {
        grid.setItems(service.getAllByCampId(getCamp_id()));
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        setCamp_id(Long.parseLong(parameter));
        updateList();
    }

    private void setCamp_id(Long camp_id) {
        this.camp_id = camp_id;
    }

    private Long getCamp_id() {
        return camp_id;
    }
}