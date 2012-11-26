package com.example.vaadinproj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.example.vaadinproj.domain.Wydarzenie;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

public class VaadinprojApplication extends Application implements Property.ValueChangeListener {
	SimpleDateFormat sdate = new SimpleDateFormat("dd.MM.yy");
	String data = sdate.format(new Date());
	final HashMap<String, Integer> mapacheck = new HashMap<String, Integer>();
	final HashMap<String, BeanItemContainer<Wydarzenie>> mapa = new HashMap<String, BeanItemContainer<Wydarzenie>>();
	Table wydarzenieTable;
	BeanItemContainer<Wydarzenie> beanTemp = new BeanItemContainer<Wydarzenie>(Wydarzenie.class);
	@Override
	public void init() {
		final Window mainWindow = new Window("Vaadinproj Application");
		final Form formularz = new Form();
		mapa.put(data, new BeanItemContainer<Wydarzenie>(Wydarzenie.class));
		mapacheck.put(data, 1);
		wydarzenieTable = new Table("Wydarzenia", beanTemp);
		formularz.setCaption("Podaj wydarzenie");
		formularz.setWriteThrough(false);
		final Wydarzenie wydarzenie = new Wydarzenie();
		BeanItem<Wydarzenie> wydarzenieItem = new BeanItem<Wydarzenie>(wydarzenie);
		final BeanItemContainer<Wydarzenie> wydarzenieItemCont = new BeanItemContainer<Wydarzenie>(Wydarzenie.class);
		formularz.setItemDataSource(wydarzenieItem);
		formularz.setVisibleItemProperties(Arrays.asList(new String[] { "tresc" }));
		InlineDateField datetime = new InlineDateField("Podaj date:");
		datetime.setValue(new java.util.Date());
		datetime.setResolution(PopupDateField.RESOLUTION_DAY);
		datetime.addListener(this);
		datetime.setImmediate(true);
		//mainWindow.addComponent(label);
		setMainWindow(mainWindow);
		//mainWindow.addComponent(datetime);
		//mainWindow.addComponent(formularz);

		
		wydarzenieTable.setImmediate(true);
		wydarzenieTable.setWidth("400px");
		Button dodaj = new Button("Dodaj", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					formularz.commit();
					wydarzenieItemCont.addItem(new Wydarzenie(data, wydarzenie.getTresc()));
					//getMainWindow().showNotification(Integer.toString(wydarzenieItemCont.size()));
					mapa.get(data).addItem(new Wydarzenie(data, wydarzenie.getTresc()));
					//wydarzenieTable.addItem(new Wydarzenie(data, wydarzenie.getTresc()));
					mainWindow.removeComponent(wydarzenieTable);
					wydarzenieTable = new Table("Wydarzenie", mapa.get(data));
					wydarzenieTable.setSelectable(true);
					wydarzenieTable.setWidth("400px");
					mainWindow.addComponent(wydarzenieTable);
					//table(data, wydarzenieItemCont);
				} catch(Exception e) {
					
				}
				
			}
		});
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(datetime);
		hl.setSpacing(true);
		//hl.addComponent(wydarzenieTable);
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(formularz);
		vl.addComponent(dodaj);
		vl.setSpacing(true);
		hl.addComponent(vl);
		//mainWindow.addComponent(dodaj);

		//mainWindow.addComponent(wydarzenieTable);
		Button usun = new Button("Usun", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				wydarzenieTable.removeItem(wydarzenieTable.getValue());
				wydarzenieTable.select(null);
				
			}
		});
		vl.addComponent(usun);
		mainWindow.addComponent(hl);
		mainWindow.addComponent(wydarzenieTable);
		//mainWindow.addComponent(usun);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		Object value = event.getProperty().getValue();
		if(value == null || !(value instanceof Date)) {
			getMainWindow().showNotification("Zła data!");
		} else {
			String dateOut = dateFormatter.format(value);
			this.data = dateOut;
			if(!mapacheck.containsKey(data)) {
				mapacheck.put(data, 1);
				mapa.put(data, new BeanItemContainer<Wydarzenie>(Wydarzenie.class));
			}
			getMainWindow().removeComponent(wydarzenieTable);
			wydarzenieTable = new Table("Wydarzenie", mapa.get(data));
			wydarzenieTable.setSelectable(true);
			wydarzenieTable.setWidth("400px");
			getMainWindow().addComponent(wydarzenieTable);
		}
		
	}
}
