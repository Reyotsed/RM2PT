package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Tooltip;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.time.LocalDate;
import java.util.LinkedList;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import gui.supportclass.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import services.*;
import services.impl.*;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Method;

import entities.*;

public class PrototypeController implements Initializable {


	DateTimeFormatter dateformatter;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		videosystem_service = ServiceManager.createVideoSystem();
		thirdpartyservices_service = ServiceManager.createThirdPartyServices();
		loginservice_service = ServiceManager.createLoginService();
		registerservice_service = ServiceManager.createRegisterService();
		watchvideoservice_service = ServiceManager.createWatchVideoService();
		likevideoservice_service = ServiceManager.createLikeVideoService();
		commentvideoservice_service = ServiceManager.createCommentVideoService();
		managevideoservice_service = ServiceManager.createManageVideoService();
		uploadvideoservice_service = ServiceManager.createUploadVideoService();
		updatevideoservice_service = ServiceManager.createUpdateVideoService();
				
		this.dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
	   	 //prepare data for contract
	   	 prepareData();
	   	 
	   	 //generate invariant panel
	   	 genereateInvairantPanel();
	   	 
		 //Actor Threeview Binding
		 actorTreeViewBinding();
		 
		 //Generate
		 generatOperationPane();
		 genereateOpInvariantPanel();
		 
		 //prilimariry data
		 try {
			DataFitService.fit();
		 } catch (PreconditionException e) {
			// TODO Auto-generated catch block
		 	e.printStackTrace();
		 }
		 
		 //generate class statistic
		 classStatisicBingding();
		 
		 //generate object statistic
		 generateObjectTable();
		 
		 //genereate association statistic
		 associationStatisicBingding();

		 //set listener 
		 setListeners();
	}
	
	/**
	 * deepCopyforTreeItem (Actor Generation)
	 */
	TreeItem<String> deepCopyTree(TreeItem<String> item) {
		    TreeItem<String> copy = new TreeItem<String>(item.getValue());
		    for (TreeItem<String> child : item.getChildren()) {
		        copy.getChildren().add(deepCopyTree(child));
		    }
		    return copy;
	}
	
	/**
	 * check all invariant and update invariant panel
	 */
	public void invairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}				
			}
			
			for (Entry<String, Label> inv : service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * check op invariant and update op invariant panel
	 */		
	public void opInvairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : op_entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
			for (Entry<String, Label> inv : op_service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	*	generate op invariant panel 
	*/
	public void genereateOpInvariantPanel() {
		
		opInvariantPanel = new HashMap<String, VBox>();
		op_entity_invariants_label_map = new LinkedHashMap<String, Label>();
		op_service_invariants_label_map = new LinkedHashMap<String, Label>();
		
		VBox v;
		List<String> entities;
		v = new VBox();
		
		//entities invariants
		entities = CommentVideoServiceImpl.opINVRelatedEntity.get("commentVideo");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("commentVideo" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CommentVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("commentVideo", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LikeVideoServiceImpl.opINVRelatedEntity.get("isLiked");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("isLiked" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LikeVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("isLiked", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LikeVideoServiceImpl.opINVRelatedEntity.get("likeVideo");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("likeVideo" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LikeVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("likeVideo", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LoginServiceImpl.opINVRelatedEntity.get("login");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("login" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LoginService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("login", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageVideoServiceImpl.opINVRelatedEntity.get("getUserVideo");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("getUserVideo" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("getUserVideo", v);
		
		v = new VBox();
		
		//entities invariants
		entities = RegisterServiceImpl.opINVRelatedEntity.get("register");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("register" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("RegisterService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("register", v);
		
		v = new VBox();
		
		//entities invariants
		entities = UpdateVideoServiceImpl.opINVRelatedEntity.get("getVideoInfo");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("getVideoInfo" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("UpdateVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("getVideoInfo", v);
		
		v = new VBox();
		
		//entities invariants
		entities = UpdateVideoServiceImpl.opINVRelatedEntity.get("updateVideo");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("updateVideo" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("UpdateVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("updateVideo", v);
		
		v = new VBox();
		
		//entities invariants
		entities = UploadVideoServiceImpl.opINVRelatedEntity.get("uploadVideo");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("uploadVideo" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("UploadVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("uploadVideo", v);
		
		v = new VBox();
		
		//entities invariants
		entities = WatchVideoServiceImpl.opINVRelatedEntity.get("getVideoList");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("getVideoList" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("WatchVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("getVideoList", v);
		
		v = new VBox();
		
		//entities invariants
		entities = WatchVideoServiceImpl.opINVRelatedEntity.get("getLowVideoList");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("getLowVideoList" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("WatchVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("getLowVideoList", v);
		
		v = new VBox();
		
		//entities invariants
		entities = WatchVideoServiceImpl.opINVRelatedEntity.get("getLikedVideoList");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("getLikedVideoList" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("WatchVideoService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("getLikedVideoList", v);
		
		
	}
	
	
	/*
	*  generate invariant panel
	*/
	public void genereateInvairantPanel() {
		
		service_invariants_label_map = new LinkedHashMap<String, Label>();
		entity_invariants_label_map = new LinkedHashMap<String, Label>();
		
		//entity_invariants_map
		VBox v = new VBox();
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			Label l = new Label(inv.getKey());
			l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			service_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		//entity invariants
		for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
			
			String INVname = inv.getKey();
			Label l = new Label(INVname);
			if (INVname.contains("AssociationInvariants")) {
				l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #099b17 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			} else {
				l.setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
			}	
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			entity_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		ScrollPane scrollPane = new ScrollPane(v);
		scrollPane.setFitToWidth(true);
		all_invariant_pane.setMaxHeight(850);
		
		all_invariant_pane.setContent(scrollPane);
	}	
	
	
	
	/* 
	*	mainPane add listener
	*/
	public void setListeners() {
		 mainPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			 
			 	if (newTab.getText().equals("System State")) {
			 		System.out.println("refresh all");
			 		refreshAll();
			 	}
		    
		    });
	}
	
	
	//checking all invariants
	public void checkAllInvariants() {
		
		invairantPanelUpdate();
	
	}	
	
	//refresh all
	public void refreshAll() {
		
		invairantPanelUpdate();
		classStatisticUpdate();
		generateObjectTable();
	}
	
	
	//update association
	public void updateAssociation(String className) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber();
		}
		
	}
	
	public void updateAssociation(String className, int index) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber(index);
		}
		
	}	
	
	public void generateObjectTable() {
		
		allObjectTables = new LinkedHashMap<String, TableView>();
		
		TableView<Map<String, String>> tableUser = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		
		//table data
		ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
		List<User> rsUser = EntityManager.getAllInstancesOf("User");
		for (User r : rsUser) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			

			dataUser.add(unit);
		}
		
		tableUser.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableUser.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("User", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableUser.setItems(dataUser);
		allObjectTables.put("User", tableUser);
		
		TableView<Map<String, String>> tableWatchVideo = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableWatchVideo_VideoId = new TableColumn<Map<String, String>, String>("VideoId");
		tableWatchVideo_VideoId.setMinWidth("VideoId".length()*10);
		tableWatchVideo_VideoId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("VideoId"));
		    }
		});	
		tableWatchVideo.getColumns().add(tableWatchVideo_VideoId);
		TableColumn<Map<String, String>, String> tableWatchVideo_VideoContent = new TableColumn<Map<String, String>, String>("VideoContent");
		tableWatchVideo_VideoContent.setMinWidth("VideoContent".length()*10);
		tableWatchVideo_VideoContent.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("VideoContent"));
		    }
		});	
		tableWatchVideo.getColumns().add(tableWatchVideo_VideoContent);
		
		//table data
		ObservableList<Map<String, String>> dataWatchVideo = FXCollections.observableArrayList();
		List<WatchVideo> rsWatchVideo = EntityManager.getAllInstancesOf("WatchVideo");
		for (WatchVideo r : rsWatchVideo) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getVideoId() != null)
				unit.put("VideoId", String.valueOf(r.getVideoId()));
			else
				unit.put("VideoId", "");
			if (r.getVideoContent() != null)
				unit.put("VideoContent", String.valueOf(r.getVideoContent()));
			else
				unit.put("VideoContent", "");

			dataWatchVideo.add(unit);
		}
		
		tableWatchVideo.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableWatchVideo.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("WatchVideo", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableWatchVideo.setItems(dataWatchVideo);
		allObjectTables.put("WatchVideo", tableWatchVideo);
		
		TableView<Map<String, String>> tableCommentVideo = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableCommentVideo_CommentId = new TableColumn<Map<String, String>, String>("CommentId");
		tableCommentVideo_CommentId.setMinWidth("CommentId".length()*10);
		tableCommentVideo_CommentId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("CommentId"));
		    }
		});	
		tableCommentVideo.getColumns().add(tableCommentVideo_CommentId);
		TableColumn<Map<String, String>, String> tableCommentVideo_Content = new TableColumn<Map<String, String>, String>("Content");
		tableCommentVideo_Content.setMinWidth("Content".length()*10);
		tableCommentVideo_Content.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Content"));
		    }
		});	
		tableCommentVideo.getColumns().add(tableCommentVideo_Content);
		TableColumn<Map<String, String>, String> tableCommentVideo_VideoId = new TableColumn<Map<String, String>, String>("VideoId");
		tableCommentVideo_VideoId.setMinWidth("VideoId".length()*10);
		tableCommentVideo_VideoId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("VideoId"));
		    }
		});	
		tableCommentVideo.getColumns().add(tableCommentVideo_VideoId);
		TableColumn<Map<String, String>, String> tableCommentVideo_UserId = new TableColumn<Map<String, String>, String>("UserId");
		tableCommentVideo_UserId.setMinWidth("UserId".length()*10);
		tableCommentVideo_UserId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserId"));
		    }
		});	
		tableCommentVideo.getColumns().add(tableCommentVideo_UserId);
		
		//table data
		ObservableList<Map<String, String>> dataCommentVideo = FXCollections.observableArrayList();
		List<CommentVideo> rsCommentVideo = EntityManager.getAllInstancesOf("CommentVideo");
		for (CommentVideo r : rsCommentVideo) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getCommentId() != null)
				unit.put("CommentId", String.valueOf(r.getCommentId()));
			else
				unit.put("CommentId", "");
			if (r.getContent() != null)
				unit.put("Content", String.valueOf(r.getContent()));
			else
				unit.put("Content", "");
			if (r.getVideoId() != null)
				unit.put("VideoId", String.valueOf(r.getVideoId()));
			else
				unit.put("VideoId", "");
			if (r.getUserId() != null)
				unit.put("UserId", String.valueOf(r.getUserId()));
			else
				unit.put("UserId", "");

			dataCommentVideo.add(unit);
		}
		
		tableCommentVideo.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableCommentVideo.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("CommentVideo", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableCommentVideo.setItems(dataCommentVideo);
		allObjectTables.put("CommentVideo", tableCommentVideo);
		
		TableView<Map<String, String>> tableVideoComponent = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		
		//table data
		ObservableList<Map<String, String>> dataVideoComponent = FXCollections.observableArrayList();
		List<VideoComponent> rsVideoComponent = EntityManager.getAllInstancesOf("VideoComponent");
		for (VideoComponent r : rsVideoComponent) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			

			dataVideoComponent.add(unit);
		}
		
		tableVideoComponent.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableVideoComponent.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("VideoComponent", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableVideoComponent.setItems(dataVideoComponent);
		allObjectTables.put("VideoComponent", tableVideoComponent);
		
		TableView<Map<String, String>> tableLikeVideo = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableLikeVideo_UserId = new TableColumn<Map<String, String>, String>("UserId");
		tableLikeVideo_UserId.setMinWidth("UserId".length()*10);
		tableLikeVideo_UserId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserId"));
		    }
		});	
		tableLikeVideo.getColumns().add(tableLikeVideo_UserId);
		TableColumn<Map<String, String>, String> tableLikeVideo_VideoId = new TableColumn<Map<String, String>, String>("VideoId");
		tableLikeVideo_VideoId.setMinWidth("VideoId".length()*10);
		tableLikeVideo_VideoId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("VideoId"));
		    }
		});	
		tableLikeVideo.getColumns().add(tableLikeVideo_VideoId);
		
		//table data
		ObservableList<Map<String, String>> dataLikeVideo = FXCollections.observableArrayList();
		List<LikeVideo> rsLikeVideo = EntityManager.getAllInstancesOf("LikeVideo");
		for (LikeVideo r : rsLikeVideo) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getUserId() != null)
				unit.put("UserId", String.valueOf(r.getUserId()));
			else
				unit.put("UserId", "");
			if (r.getVideoId() != null)
				unit.put("VideoId", String.valueOf(r.getVideoId()));
			else
				unit.put("VideoId", "");

			dataLikeVideo.add(unit);
		}
		
		tableLikeVideo.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableLikeVideo.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("LikeVideo", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableLikeVideo.setItems(dataLikeVideo);
		allObjectTables.put("LikeVideo", tableLikeVideo);
		
		TableView<Map<String, String>> tableViewer = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		
		//table data
		ObservableList<Map<String, String>> dataViewer = FXCollections.observableArrayList();
		List<Viewer> rsViewer = EntityManager.getAllInstancesOf("Viewer");
		for (Viewer r : rsViewer) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			

			dataViewer.add(unit);
		}
		
		tableViewer.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableViewer.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Viewer", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableViewer.setItems(dataViewer);
		allObjectTables.put("Viewer", tableViewer);
		
		TableView<Map<String, String>> tablePoster = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tablePoster_UserId = new TableColumn<Map<String, String>, String>("UserId");
		tablePoster_UserId.setMinWidth("UserId".length()*10);
		tablePoster_UserId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserId"));
		    }
		});	
		tablePoster.getColumns().add(tablePoster_UserId);
		TableColumn<Map<String, String>, String> tablePoster_UserName = new TableColumn<Map<String, String>, String>("UserName");
		tablePoster_UserName.setMinWidth("UserName".length()*10);
		tablePoster_UserName.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserName"));
		    }
		});	
		tablePoster.getColumns().add(tablePoster_UserName);
		TableColumn<Map<String, String>, String> tablePoster_Password = new TableColumn<Map<String, String>, String>("Password");
		tablePoster_Password.setMinWidth("Password".length()*10);
		tablePoster_Password.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Password"));
		    }
		});	
		tablePoster.getColumns().add(tablePoster_Password);
		
		//table data
		ObservableList<Map<String, String>> dataPoster = FXCollections.observableArrayList();
		List<Poster> rsPoster = EntityManager.getAllInstancesOf("Poster");
		for (Poster r : rsPoster) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getUserId() != null)
				unit.put("UserId", String.valueOf(r.getUserId()));
			else
				unit.put("UserId", "");
			if (r.getUserName() != null)
				unit.put("UserName", String.valueOf(r.getUserName()));
			else
				unit.put("UserName", "");
			if (r.getPassword() != null)
				unit.put("Password", String.valueOf(r.getPassword()));
			else
				unit.put("Password", "");

			dataPoster.add(unit);
		}
		
		tablePoster.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tablePoster.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Poster", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tablePoster.setItems(dataPoster);
		allObjectTables.put("Poster", tablePoster);
		
		TableView<Map<String, String>> tableUploadVideo = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableUploadVideo_VideoInfo = new TableColumn<Map<String, String>, String>("VideoInfo");
		tableUploadVideo_VideoInfo.setMinWidth("VideoInfo".length()*10);
		tableUploadVideo_VideoInfo.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("VideoInfo"));
		    }
		});	
		tableUploadVideo.getColumns().add(tableUploadVideo_VideoInfo);
		
		//table data
		ObservableList<Map<String, String>> dataUploadVideo = FXCollections.observableArrayList();
		List<UploadVideo> rsUploadVideo = EntityManager.getAllInstancesOf("UploadVideo");
		for (UploadVideo r : rsUploadVideo) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getVideoInfo() != null)
				unit.put("VideoInfo", String.valueOf(r.getVideoInfo()));
			else
				unit.put("VideoInfo", "");

			dataUploadVideo.add(unit);
		}
		
		tableUploadVideo.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableUploadVideo.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("UploadVideo", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableUploadVideo.setItems(dataUploadVideo);
		allObjectTables.put("UploadVideo", tableUploadVideo);
		
		TableView<Map<String, String>> tableUpdateVideo = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableUpdateVideo_VideoInfo = new TableColumn<Map<String, String>, String>("VideoInfo");
		tableUpdateVideo_VideoInfo.setMinWidth("VideoInfo".length()*10);
		tableUpdateVideo_VideoInfo.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("VideoInfo"));
		    }
		});	
		tableUpdateVideo.getColumns().add(tableUpdateVideo_VideoInfo);
		
		//table data
		ObservableList<Map<String, String>> dataUpdateVideo = FXCollections.observableArrayList();
		List<UpdateVideo> rsUpdateVideo = EntityManager.getAllInstancesOf("UpdateVideo");
		for (UpdateVideo r : rsUpdateVideo) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getVideoInfo() != null)
				unit.put("VideoInfo", String.valueOf(r.getVideoInfo()));
			else
				unit.put("VideoInfo", "");

			dataUpdateVideo.add(unit);
		}
		
		tableUpdateVideo.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableUpdateVideo.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("UpdateVideo", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableUpdateVideo.setItems(dataUpdateVideo);
		allObjectTables.put("UpdateVideo", tableUpdateVideo);
		

		
	}
	
	/* 
	* update all object tables with sub dataset
	*/ 
	public void updateUserTable(List<User> rsUser) {
			ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
			for (User r : rsUser) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				dataUser.add(unit);
			}
			
			allObjectTables.get("User").setItems(dataUser);
	}
	public void updateWatchVideoTable(List<WatchVideo> rsWatchVideo) {
			ObservableList<Map<String, String>> dataWatchVideo = FXCollections.observableArrayList();
			for (WatchVideo r : rsWatchVideo) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getVideoId() != null)
					unit.put("VideoId", String.valueOf(r.getVideoId()));
				else
					unit.put("VideoId", "");
				if (r.getVideoContent() != null)
					unit.put("VideoContent", String.valueOf(r.getVideoContent()));
				else
					unit.put("VideoContent", "");
				dataWatchVideo.add(unit);
			}
			
			allObjectTables.get("WatchVideo").setItems(dataWatchVideo);
	}
	public void updateCommentVideoTable(List<CommentVideo> rsCommentVideo) {
			ObservableList<Map<String, String>> dataCommentVideo = FXCollections.observableArrayList();
			for (CommentVideo r : rsCommentVideo) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getCommentId() != null)
					unit.put("CommentId", String.valueOf(r.getCommentId()));
				else
					unit.put("CommentId", "");
				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				if (r.getVideoId() != null)
					unit.put("VideoId", String.valueOf(r.getVideoId()));
				else
					unit.put("VideoId", "");
				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				dataCommentVideo.add(unit);
			}
			
			allObjectTables.get("CommentVideo").setItems(dataCommentVideo);
	}
	public void updateVideoComponentTable(List<VideoComponent> rsVideoComponent) {
			ObservableList<Map<String, String>> dataVideoComponent = FXCollections.observableArrayList();
			for (VideoComponent r : rsVideoComponent) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				dataVideoComponent.add(unit);
			}
			
			allObjectTables.get("VideoComponent").setItems(dataVideoComponent);
	}
	public void updateLikeVideoTable(List<LikeVideo> rsLikeVideo) {
			ObservableList<Map<String, String>> dataLikeVideo = FXCollections.observableArrayList();
			for (LikeVideo r : rsLikeVideo) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				if (r.getVideoId() != null)
					unit.put("VideoId", String.valueOf(r.getVideoId()));
				else
					unit.put("VideoId", "");
				dataLikeVideo.add(unit);
			}
			
			allObjectTables.get("LikeVideo").setItems(dataLikeVideo);
	}
	public void updateViewerTable(List<Viewer> rsViewer) {
			ObservableList<Map<String, String>> dataViewer = FXCollections.observableArrayList();
			for (Viewer r : rsViewer) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				dataViewer.add(unit);
			}
			
			allObjectTables.get("Viewer").setItems(dataViewer);
	}
	public void updatePosterTable(List<Poster> rsPoster) {
			ObservableList<Map<String, String>> dataPoster = FXCollections.observableArrayList();
			for (Poster r : rsPoster) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				if (r.getUserName() != null)
					unit.put("UserName", String.valueOf(r.getUserName()));
				else
					unit.put("UserName", "");
				if (r.getPassword() != null)
					unit.put("Password", String.valueOf(r.getPassword()));
				else
					unit.put("Password", "");
				dataPoster.add(unit);
			}
			
			allObjectTables.get("Poster").setItems(dataPoster);
	}
	public void updateUploadVideoTable(List<UploadVideo> rsUploadVideo) {
			ObservableList<Map<String, String>> dataUploadVideo = FXCollections.observableArrayList();
			for (UploadVideo r : rsUploadVideo) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getVideoInfo() != null)
					unit.put("VideoInfo", String.valueOf(r.getVideoInfo()));
				else
					unit.put("VideoInfo", "");
				dataUploadVideo.add(unit);
			}
			
			allObjectTables.get("UploadVideo").setItems(dataUploadVideo);
	}
	public void updateUpdateVideoTable(List<UpdateVideo> rsUpdateVideo) {
			ObservableList<Map<String, String>> dataUpdateVideo = FXCollections.observableArrayList();
			for (UpdateVideo r : rsUpdateVideo) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getVideoInfo() != null)
					unit.put("VideoInfo", String.valueOf(r.getVideoInfo()));
				else
					unit.put("VideoInfo", "");
				dataUpdateVideo.add(unit);
			}
			
			allObjectTables.get("UpdateVideo").setItems(dataUpdateVideo);
	}
	
	/* 
	* update all object tables
	*/ 
	public void updateUserTable() {
			ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
			List<User> rsUser = EntityManager.getAllInstancesOf("User");
			for (User r : rsUser) {
				Map<String, String> unit = new HashMap<String, String>();


				dataUser.add(unit);
			}
			
			allObjectTables.get("User").setItems(dataUser);
	}
	public void updateWatchVideoTable() {
			ObservableList<Map<String, String>> dataWatchVideo = FXCollections.observableArrayList();
			List<WatchVideo> rsWatchVideo = EntityManager.getAllInstancesOf("WatchVideo");
			for (WatchVideo r : rsWatchVideo) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getVideoId() != null)
					unit.put("VideoId", String.valueOf(r.getVideoId()));
				else
					unit.put("VideoId", "");
				if (r.getVideoContent() != null)
					unit.put("VideoContent", String.valueOf(r.getVideoContent()));
				else
					unit.put("VideoContent", "");
				dataWatchVideo.add(unit);
			}
			
			allObjectTables.get("WatchVideo").setItems(dataWatchVideo);
	}
	public void updateCommentVideoTable() {
			ObservableList<Map<String, String>> dataCommentVideo = FXCollections.observableArrayList();
			List<CommentVideo> rsCommentVideo = EntityManager.getAllInstancesOf("CommentVideo");
			for (CommentVideo r : rsCommentVideo) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getCommentId() != null)
					unit.put("CommentId", String.valueOf(r.getCommentId()));
				else
					unit.put("CommentId", "");
				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				if (r.getVideoId() != null)
					unit.put("VideoId", String.valueOf(r.getVideoId()));
				else
					unit.put("VideoId", "");
				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				dataCommentVideo.add(unit);
			}
			
			allObjectTables.get("CommentVideo").setItems(dataCommentVideo);
	}
	public void updateVideoComponentTable() {
			ObservableList<Map<String, String>> dataVideoComponent = FXCollections.observableArrayList();
			List<VideoComponent> rsVideoComponent = EntityManager.getAllInstancesOf("VideoComponent");
			for (VideoComponent r : rsVideoComponent) {
				Map<String, String> unit = new HashMap<String, String>();


				dataVideoComponent.add(unit);
			}
			
			allObjectTables.get("VideoComponent").setItems(dataVideoComponent);
	}
	public void updateLikeVideoTable() {
			ObservableList<Map<String, String>> dataLikeVideo = FXCollections.observableArrayList();
			List<LikeVideo> rsLikeVideo = EntityManager.getAllInstancesOf("LikeVideo");
			for (LikeVideo r : rsLikeVideo) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				if (r.getVideoId() != null)
					unit.put("VideoId", String.valueOf(r.getVideoId()));
				else
					unit.put("VideoId", "");
				dataLikeVideo.add(unit);
			}
			
			allObjectTables.get("LikeVideo").setItems(dataLikeVideo);
	}
	public void updateViewerTable() {
			ObservableList<Map<String, String>> dataViewer = FXCollections.observableArrayList();
			List<Viewer> rsViewer = EntityManager.getAllInstancesOf("Viewer");
			for (Viewer r : rsViewer) {
				Map<String, String> unit = new HashMap<String, String>();


				dataViewer.add(unit);
			}
			
			allObjectTables.get("Viewer").setItems(dataViewer);
	}
	public void updatePosterTable() {
			ObservableList<Map<String, String>> dataPoster = FXCollections.observableArrayList();
			List<Poster> rsPoster = EntityManager.getAllInstancesOf("Poster");
			for (Poster r : rsPoster) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				if (r.getUserName() != null)
					unit.put("UserName", String.valueOf(r.getUserName()));
				else
					unit.put("UserName", "");
				if (r.getPassword() != null)
					unit.put("Password", String.valueOf(r.getPassword()));
				else
					unit.put("Password", "");
				dataPoster.add(unit);
			}
			
			allObjectTables.get("Poster").setItems(dataPoster);
	}
	public void updateUploadVideoTable() {
			ObservableList<Map<String, String>> dataUploadVideo = FXCollections.observableArrayList();
			List<UploadVideo> rsUploadVideo = EntityManager.getAllInstancesOf("UploadVideo");
			for (UploadVideo r : rsUploadVideo) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getVideoInfo() != null)
					unit.put("VideoInfo", String.valueOf(r.getVideoInfo()));
				else
					unit.put("VideoInfo", "");
				dataUploadVideo.add(unit);
			}
			
			allObjectTables.get("UploadVideo").setItems(dataUploadVideo);
	}
	public void updateUpdateVideoTable() {
			ObservableList<Map<String, String>> dataUpdateVideo = FXCollections.observableArrayList();
			List<UpdateVideo> rsUpdateVideo = EntityManager.getAllInstancesOf("UpdateVideo");
			for (UpdateVideo r : rsUpdateVideo) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getVideoInfo() != null)
					unit.put("VideoInfo", String.valueOf(r.getVideoInfo()));
				else
					unit.put("VideoInfo", "");
				dataUpdateVideo.add(unit);
			}
			
			allObjectTables.get("UpdateVideo").setItems(dataUpdateVideo);
	}
	
	public void classStatisicBingding() {
	
		 classInfodata = FXCollections.observableArrayList();
	 	 user = new ClassInfo("User", EntityManager.getAllInstancesOf("User").size());
	 	 classInfodata.add(user);
	 	 watchvideo = new ClassInfo("WatchVideo", EntityManager.getAllInstancesOf("WatchVideo").size());
	 	 classInfodata.add(watchvideo);
	 	 commentvideo = new ClassInfo("CommentVideo", EntityManager.getAllInstancesOf("CommentVideo").size());
	 	 classInfodata.add(commentvideo);
	 	 videocomponent = new ClassInfo("VideoComponent", EntityManager.getAllInstancesOf("VideoComponent").size());
	 	 classInfodata.add(videocomponent);
	 	 likevideo = new ClassInfo("LikeVideo", EntityManager.getAllInstancesOf("LikeVideo").size());
	 	 classInfodata.add(likevideo);
	 	 viewer = new ClassInfo("Viewer", EntityManager.getAllInstancesOf("Viewer").size());
	 	 classInfodata.add(viewer);
	 	 poster = new ClassInfo("Poster", EntityManager.getAllInstancesOf("Poster").size());
	 	 classInfodata.add(poster);
	 	 uploadvideo = new ClassInfo("UploadVideo", EntityManager.getAllInstancesOf("UploadVideo").size());
	 	 classInfodata.add(uploadvideo);
	 	 updatevideo = new ClassInfo("UpdateVideo", EntityManager.getAllInstancesOf("UpdateVideo").size());
	 	 classInfodata.add(updatevideo);
	 	 
		 class_statisic.setItems(classInfodata);
		 
		 //Class Statisic Binding
		 class_statisic.getSelectionModel().selectedItemProperty().addListener(
				 (observable, oldValue, newValue) ->  { 
				 										 //no selected object in table
				 										 objectindex = -1;
				 										 
				 										 //get lastest data, reflect updateTableData method
				 										 try {
												 			 Method updateob = this.getClass().getMethod("update" + newValue.getName() + "Table", null);
												 			 updateob.invoke(this);			 
												 		 } catch (Exception e) {
												 		 	 e.printStackTrace();
												 		 }		 										 
				 	
				 										 //show object table
				 			 				 			 TableView obs = allObjectTables.get(newValue.getName());
				 			 				 			 if (obs != null) {
				 			 				 				object_statics.setContent(obs);
				 			 				 				object_statics.setText("All Objects " + newValue.getName() + ":");
				 			 				 			 }
				 			 				 			 
				 			 				 			 //update association information
							 			 				 updateAssociation(newValue.getName());
				 			 				 			 
				 			 				 			 //show association information
				 			 				 			 ObservableList<AssociationInfo> asso = allassociationData.get(newValue.getName());
				 			 				 			 if (asso != null) {
				 			 				 			 	association_statisic.setItems(asso);
				 			 				 			 }
				 			 				 		  });
	}
	
	public void classStatisticUpdate() {
	 	 user.setNumber(EntityManager.getAllInstancesOf("User").size());
	 	 watchvideo.setNumber(EntityManager.getAllInstancesOf("WatchVideo").size());
	 	 commentvideo.setNumber(EntityManager.getAllInstancesOf("CommentVideo").size());
	 	 videocomponent.setNumber(EntityManager.getAllInstancesOf("VideoComponent").size());
	 	 likevideo.setNumber(EntityManager.getAllInstancesOf("LikeVideo").size());
	 	 viewer.setNumber(EntityManager.getAllInstancesOf("Viewer").size());
	 	 poster.setNumber(EntityManager.getAllInstancesOf("Poster").size());
	 	 uploadvideo.setNumber(EntityManager.getAllInstancesOf("UploadVideo").size());
	 	 updatevideo.setNumber(EntityManager.getAllInstancesOf("UpdateVideo").size());
		
	}
	
	/**
	 * association binding
	 */
	public void associationStatisicBingding() {
		
		allassociationData = new HashMap<String, ObservableList<AssociationInfo>>();
		
		ObservableList<AssociationInfo> User_association_data = FXCollections.observableArrayList();
		AssociationInfo User_associatition_Usertocomponent = new AssociationInfo("User", "VideoComponent", "Usertocomponent", false);
		User_association_data.add(User_associatition_Usertocomponent);
		
		allassociationData.put("User", User_association_data);
		
		ObservableList<AssociationInfo> WatchVideo_association_data = FXCollections.observableArrayList();
		AssociationInfo WatchVideo_associatition_VideotoUploadVideo = new AssociationInfo("WatchVideo", "UploadVideo", "VideotoUploadVideo", false);
		WatchVideo_association_data.add(WatchVideo_associatition_VideotoUploadVideo);
		AssociationInfo WatchVideo_associatition_VideotoUpdateVideo = new AssociationInfo("WatchVideo", "UpdateVideo", "VideotoUpdateVideo", false);
		WatchVideo_association_data.add(WatchVideo_associatition_VideotoUpdateVideo);
		
		allassociationData.put("WatchVideo", WatchVideo_association_data);
		
		ObservableList<AssociationInfo> CommentVideo_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("CommentVideo", CommentVideo_association_data);
		
		ObservableList<AssociationInfo> VideoComponent_association_data = FXCollections.observableArrayList();
		AssociationInfo VideoComponent_associatition_ComponenttoVideo = new AssociationInfo("VideoComponent", "WatchVideo", "ComponenttoVideo", false);
		VideoComponent_association_data.add(VideoComponent_associatition_ComponenttoVideo);
		AssociationInfo VideoComponent_associatition_ComponenttoComment = new AssociationInfo("VideoComponent", "CommentVideo", "ComponenttoComment", false);
		VideoComponent_association_data.add(VideoComponent_associatition_ComponenttoComment);
		AssociationInfo VideoComponent_associatition_Componenttolike = new AssociationInfo("VideoComponent", "LikeVideo", "Componenttolike", false);
		VideoComponent_association_data.add(VideoComponent_associatition_Componenttolike);
		
		allassociationData.put("VideoComponent", VideoComponent_association_data);
		
		ObservableList<AssociationInfo> LikeVideo_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("LikeVideo", LikeVideo_association_data);
		
		ObservableList<AssociationInfo> Viewer_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("Viewer", Viewer_association_data);
		
		ObservableList<AssociationInfo> Poster_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("Poster", Poster_association_data);
		
		ObservableList<AssociationInfo> UploadVideo_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("UploadVideo", UploadVideo_association_data);
		
		ObservableList<AssociationInfo> UpdateVideo_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("UpdateVideo", UpdateVideo_association_data);
		
		
		association_statisic.getSelectionModel().selectedItemProperty().addListener(
			    (observable, oldValue, newValue) ->  { 
	
							 		if (newValue != null) {
							 			 try {
							 			 	 if (newValue.getNumber() != 0) {
								 				 //choose object or not
								 				 if (objectindex != -1) {
									 				 Class[] cArg = new Class[1];
									 				 cArg[0] = List.class;
									 				 //reflect updateTableData method
										 			 Method updateob = this.getClass().getMethod("update" + newValue.getTargetClass() + "Table", cArg);
										 			 //find choosen object
										 			 Object selectedob = EntityManager.getAllInstancesOf(newValue.getSourceClass()).get(objectindex);
										 			 //reflect find association method
										 			 Method getAssociatedObject = selectedob.getClass().getMethod("get" + newValue.getAssociationName());
										 			 List r = new LinkedList();
										 			 //one or mulity?
										 			 if (newValue.getIsMultiple() == true) {
											 			 
											 			r = (List) getAssociatedObject.invoke(selectedob);
										 			 }
										 			 else {
										 				r.add(getAssociatedObject.invoke(selectedob));
										 			 }
										 			 //invoke update method
										 			 updateob.invoke(this, r);
										 			  
										 			 
								 				 }
												 //bind updated data to GUI
					 				 			 TableView obs = allObjectTables.get(newValue.getTargetClass());
					 				 			 if (obs != null) {
					 				 				object_statics.setContent(obs);
					 				 				object_statics.setText("Targets Objects " + newValue.getTargetClass() + ":");
					 				 			 }
					 				 		 }
							 			 }
							 			 catch (Exception e) {
							 				 e.printStackTrace();
							 			 }
							 		}
					 		  });
		
	}	
	
	

    //prepare data for contract
	public void prepareData() {
		
		//definition map
		definitions_map = new HashMap<String, String>();
		
		//precondition map
		preconditions_map = new HashMap<String, String>();
		preconditions_map.put("commentVideo", "true");
		preconditions_map.put("isLiked", "true");
		preconditions_map.put("likeVideo", "true");
		preconditions_map.put("login", "true");
		preconditions_map.put("getUserVideo", "true");
		preconditions_map.put("register", "true");
		preconditions_map.put("getVideoInfo", "true");
		preconditions_map.put("updateVideo", "true");
		preconditions_map.put("uploadVideo", "true");
		preconditions_map.put("getVideoList", "true");
		preconditions_map.put("getLowVideoList", "true");
		preconditions_map.put("getLikedVideoList", "true");
		
		//postcondition map
		postconditions_map = new HashMap<String, String>();
		postconditions_map.put("commentVideo", "result = true");
		postconditions_map.put("isLiked", "result = true");
		postconditions_map.put("likeVideo", "result = true");
		postconditions_map.put("login", "result = true");
		postconditions_map.put("getUserVideo", "result = true");
		postconditions_map.put("register", "result = true");
		postconditions_map.put("getVideoInfo", "result = true");
		postconditions_map.put("updateVideo", "result = true");
		postconditions_map.put("uploadVideo", "result = true");
		postconditions_map.put("getVideoList", "result = true");
		postconditions_map.put("getLowVideoList", "result = true");
		postconditions_map.put("getLikedVideoList", "result = true");
		
		//service invariants map
		service_invariants_map = new LinkedHashMap<String, String>();
		
		//entity invariants map
		entity_invariants_map = new LinkedHashMap<String, String>();
		
	}
	
	public void generatOperationPane() {
		
		 operationPanels = new LinkedHashMap<String, GridPane>();
		
		 // ==================== GridPane_commentVideo ====================
		 GridPane commentVideo = new GridPane();
		 commentVideo.setHgap(4);
		 commentVideo.setVgap(6);
		 commentVideo.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> commentVideo_content = commentVideo.getChildren();
		 Label commentVideo_userId_label = new Label("userId:");
		 commentVideo_userId_label.setMinWidth(Region.USE_PREF_SIZE);
		 commentVideo_content.add(commentVideo_userId_label);
		 GridPane.setConstraints(commentVideo_userId_label, 0, 0);
		 
		 commentVideo_userId_t = new TextField();
		 commentVideo_content.add(commentVideo_userId_t);
		 commentVideo_userId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(commentVideo_userId_t, 1, 0);
		 Label commentVideo_videoId_label = new Label("videoId:");
		 commentVideo_videoId_label.setMinWidth(Region.USE_PREF_SIZE);
		 commentVideo_content.add(commentVideo_videoId_label);
		 GridPane.setConstraints(commentVideo_videoId_label, 0, 1);
		 
		 commentVideo_videoId_t = new TextField();
		 commentVideo_content.add(commentVideo_videoId_t);
		 commentVideo_videoId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(commentVideo_videoId_t, 1, 1);
		 Label commentVideo_content_label = new Label("content:");
		 commentVideo_content_label.setMinWidth(Region.USE_PREF_SIZE);
		 commentVideo_content.add(commentVideo_content_label);
		 GridPane.setConstraints(commentVideo_content_label, 0, 2);
		 
		 commentVideo_content_t = new TextField();
		 commentVideo_content.add(commentVideo_content_t);
		 commentVideo_content_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(commentVideo_content_t, 1, 2);
		 operationPanels.put("commentVideo", commentVideo);
		 
		 // ==================== GridPane_isLiked ====================
		 GridPane isLiked = new GridPane();
		 isLiked.setHgap(4);
		 isLiked.setVgap(6);
		 isLiked.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> isLiked_content = isLiked.getChildren();
		 Label isLiked_userId_label = new Label("userId:");
		 isLiked_userId_label.setMinWidth(Region.USE_PREF_SIZE);
		 isLiked_content.add(isLiked_userId_label);
		 GridPane.setConstraints(isLiked_userId_label, 0, 0);
		 
		 isLiked_userId_t = new TextField();
		 isLiked_content.add(isLiked_userId_t);
		 isLiked_userId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(isLiked_userId_t, 1, 0);
		 Label isLiked_videoId_label = new Label("videoId:");
		 isLiked_videoId_label.setMinWidth(Region.USE_PREF_SIZE);
		 isLiked_content.add(isLiked_videoId_label);
		 GridPane.setConstraints(isLiked_videoId_label, 0, 1);
		 
		 isLiked_videoId_t = new TextField();
		 isLiked_content.add(isLiked_videoId_t);
		 isLiked_videoId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(isLiked_videoId_t, 1, 1);
		 operationPanels.put("isLiked", isLiked);
		 
		 // ==================== GridPane_likeVideo ====================
		 GridPane likeVideo = new GridPane();
		 likeVideo.setHgap(4);
		 likeVideo.setVgap(6);
		 likeVideo.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> likeVideo_content = likeVideo.getChildren();
		 Label likeVideo_userId_label = new Label("userId:");
		 likeVideo_userId_label.setMinWidth(Region.USE_PREF_SIZE);
		 likeVideo_content.add(likeVideo_userId_label);
		 GridPane.setConstraints(likeVideo_userId_label, 0, 0);
		 
		 likeVideo_userId_t = new TextField();
		 likeVideo_content.add(likeVideo_userId_t);
		 likeVideo_userId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(likeVideo_userId_t, 1, 0);
		 Label likeVideo_videoId_label = new Label("videoId:");
		 likeVideo_videoId_label.setMinWidth(Region.USE_PREF_SIZE);
		 likeVideo_content.add(likeVideo_videoId_label);
		 GridPane.setConstraints(likeVideo_videoId_label, 0, 1);
		 
		 likeVideo_videoId_t = new TextField();
		 likeVideo_content.add(likeVideo_videoId_t);
		 likeVideo_videoId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(likeVideo_videoId_t, 1, 1);
		 operationPanels.put("likeVideo", likeVideo);
		 
		 // ==================== GridPane_login ====================
		 GridPane login = new GridPane();
		 login.setHgap(4);
		 login.setVgap(6);
		 login.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> login_content = login.getChildren();
		 Label login_userName_label = new Label("userName:");
		 login_userName_label.setMinWidth(Region.USE_PREF_SIZE);
		 login_content.add(login_userName_label);
		 GridPane.setConstraints(login_userName_label, 0, 0);
		 
		 login_userName_t = new TextField();
		 login_content.add(login_userName_t);
		 login_userName_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(login_userName_t, 1, 0);
		 Label login_password_label = new Label("password:");
		 login_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 login_content.add(login_password_label);
		 GridPane.setConstraints(login_password_label, 0, 1);
		 
		 login_password_t = new TextField();
		 login_content.add(login_password_t);
		 login_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(login_password_t, 1, 1);
		 operationPanels.put("login", login);
		 
		 // ==================== GridPane_getUserVideo ====================
		 GridPane getUserVideo = new GridPane();
		 getUserVideo.setHgap(4);
		 getUserVideo.setVgap(6);
		 getUserVideo.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> getUserVideo_content = getUserVideo.getChildren();
		 Label getUserVideo_userId_label = new Label("userId:");
		 getUserVideo_userId_label.setMinWidth(Region.USE_PREF_SIZE);
		 getUserVideo_content.add(getUserVideo_userId_label);
		 GridPane.setConstraints(getUserVideo_userId_label, 0, 0);
		 
		 getUserVideo_userId_t = new TextField();
		 getUserVideo_content.add(getUserVideo_userId_t);
		 getUserVideo_userId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(getUserVideo_userId_t, 1, 0);
		 operationPanels.put("getUserVideo", getUserVideo);
		 
		 // ==================== GridPane_register ====================
		 GridPane register = new GridPane();
		 register.setHgap(4);
		 register.setVgap(6);
		 register.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> register_content = register.getChildren();
		 Label register_userName_label = new Label("userName:");
		 register_userName_label.setMinWidth(Region.USE_PREF_SIZE);
		 register_content.add(register_userName_label);
		 GridPane.setConstraints(register_userName_label, 0, 0);
		 
		 register_userName_t = new TextField();
		 register_content.add(register_userName_t);
		 register_userName_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(register_userName_t, 1, 0);
		 Label register_password_label = new Label("password:");
		 register_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 register_content.add(register_password_label);
		 GridPane.setConstraints(register_password_label, 0, 1);
		 
		 register_password_t = new TextField();
		 register_content.add(register_password_t);
		 register_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(register_password_t, 1, 1);
		 operationPanels.put("register", register);
		 
		 // ==================== GridPane_getVideoInfo ====================
		 GridPane getVideoInfo = new GridPane();
		 getVideoInfo.setHgap(4);
		 getVideoInfo.setVgap(6);
		 getVideoInfo.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> getVideoInfo_content = getVideoInfo.getChildren();
		 Label getVideoInfo_videoId_label = new Label("videoId:");
		 getVideoInfo_videoId_label.setMinWidth(Region.USE_PREF_SIZE);
		 getVideoInfo_content.add(getVideoInfo_videoId_label);
		 GridPane.setConstraints(getVideoInfo_videoId_label, 0, 0);
		 
		 getVideoInfo_videoId_t = new TextField();
		 getVideoInfo_content.add(getVideoInfo_videoId_t);
		 getVideoInfo_videoId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(getVideoInfo_videoId_t, 1, 0);
		 operationPanels.put("getVideoInfo", getVideoInfo);
		 
		 // ==================== GridPane_updateVideo ====================
		 GridPane updateVideo = new GridPane();
		 updateVideo.setHgap(4);
		 updateVideo.setVgap(6);
		 updateVideo.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> updateVideo_content = updateVideo.getChildren();
		 Label updateVideo_content_label = new Label("content:");
		 updateVideo_content_label.setMinWidth(Region.USE_PREF_SIZE);
		 updateVideo_content.add(updateVideo_content_label);
		 GridPane.setConstraints(updateVideo_content_label, 0, 0);
		 
		 updateVideo_content_t = new TextField();
		 updateVideo_content.add(updateVideo_content_t);
		 updateVideo_content_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(updateVideo_content_t, 1, 0);
		 Label updateVideo_videoId_label = new Label("videoId:");
		 updateVideo_videoId_label.setMinWidth(Region.USE_PREF_SIZE);
		 updateVideo_content.add(updateVideo_videoId_label);
		 GridPane.setConstraints(updateVideo_videoId_label, 0, 1);
		 
		 updateVideo_videoId_t = new TextField();
		 updateVideo_content.add(updateVideo_videoId_t);
		 updateVideo_videoId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(updateVideo_videoId_t, 1, 1);
		 operationPanels.put("updateVideo", updateVideo);
		 
		 // ==================== GridPane_uploadVideo ====================
		 GridPane uploadVideo = new GridPane();
		 uploadVideo.setHgap(4);
		 uploadVideo.setVgap(6);
		 uploadVideo.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> uploadVideo_content = uploadVideo.getChildren();
		 Label uploadVideo_userId_label = new Label("userId:");
		 uploadVideo_userId_label.setMinWidth(Region.USE_PREF_SIZE);
		 uploadVideo_content.add(uploadVideo_userId_label);
		 GridPane.setConstraints(uploadVideo_userId_label, 0, 0);
		 
		 uploadVideo_userId_t = new TextField();
		 uploadVideo_content.add(uploadVideo_userId_t);
		 uploadVideo_userId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(uploadVideo_userId_t, 1, 0);
		 Label uploadVideo_content_label = new Label("content:");
		 uploadVideo_content_label.setMinWidth(Region.USE_PREF_SIZE);
		 uploadVideo_content.add(uploadVideo_content_label);
		 GridPane.setConstraints(uploadVideo_content_label, 0, 1);
		 
		 uploadVideo_content_t = new TextField();
		 uploadVideo_content.add(uploadVideo_content_t);
		 uploadVideo_content_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(uploadVideo_content_t, 1, 1);
		 operationPanels.put("uploadVideo", uploadVideo);
		 
		 // ==================== GridPane_getVideoList ====================
		 GridPane getVideoList = new GridPane();
		 getVideoList.setHgap(4);
		 getVideoList.setVgap(6);
		 getVideoList.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> getVideoList_content = getVideoList.getChildren();
		 Label getVideoList_label = new Label("This operation is no intput parameters..");
		 getVideoList_label.setMinWidth(Region.USE_PREF_SIZE);
		 getVideoList_content.add(getVideoList_label);
		 GridPane.setConstraints(getVideoList_label, 0, 0);
		 operationPanels.put("getVideoList", getVideoList);
		 
		 // ==================== GridPane_getLowVideoList ====================
		 GridPane getLowVideoList = new GridPane();
		 getLowVideoList.setHgap(4);
		 getLowVideoList.setVgap(6);
		 getLowVideoList.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> getLowVideoList_content = getLowVideoList.getChildren();
		 Label getLowVideoList_label = new Label("This operation is no intput parameters..");
		 getLowVideoList_label.setMinWidth(Region.USE_PREF_SIZE);
		 getLowVideoList_content.add(getLowVideoList_label);
		 GridPane.setConstraints(getLowVideoList_label, 0, 0);
		 operationPanels.put("getLowVideoList", getLowVideoList);
		 
		 // ==================== GridPane_getLikedVideoList ====================
		 GridPane getLikedVideoList = new GridPane();
		 getLikedVideoList.setHgap(4);
		 getLikedVideoList.setVgap(6);
		 getLikedVideoList.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> getLikedVideoList_content = getLikedVideoList.getChildren();
		 Label getLikedVideoList_userId_label = new Label("userId:");
		 getLikedVideoList_userId_label.setMinWidth(Region.USE_PREF_SIZE);
		 getLikedVideoList_content.add(getLikedVideoList_userId_label);
		 GridPane.setConstraints(getLikedVideoList_userId_label, 0, 0);
		 
		 getLikedVideoList_userId_t = new TextField();
		 getLikedVideoList_content.add(getLikedVideoList_userId_t);
		 getLikedVideoList_userId_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(getLikedVideoList_userId_t, 1, 0);
		 operationPanels.put("getLikedVideoList", getLikedVideoList);
		 
	}	

	public void actorTreeViewBinding() {
		
		 

		TreeItem<String> treeRootadministrator = new TreeItem<String>("Root node");
		
		
					 			
						 		
		treeRootadministrator.getChildren().addAll(Arrays.asList(
				));	
				
	 			treeRootadministrator.setExpanded(true);

		actor_treeview_administrator.setShowRoot(false);
		actor_treeview_administrator.setRoot(treeRootadministrator);
	 		
		actor_treeview_administrator.getSelectionModel().selectedItemProperty().addListener(
		 				 (observable, oldValue, newValue) -> { 
		 				 								
		 				 							 //clear the previous return
		 											 operation_return_pane.setContent(new Label());
		 											 
		 				 							 clickedOp = newValue.getValue();
		 				 							 GridPane op = operationPanels.get(clickedOp);
		 				 							 VBox vb = opInvariantPanel.get(clickedOp);
		 				 							 
		 				 							 //op pannel
		 				 							 if (op != null) {
		 				 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
		 				 								 
		 				 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
		 				 								 choosenOperation = new LinkedList<TextField>();
		 				 								 for (Node n : l) {
		 				 								 	 if (n instanceof TextField) {
		 				 								 	 	choosenOperation.add((TextField)n);
		 				 								 	  }
		 				 								 }
		 				 								 
		 				 								 definition.setText(definitions_map.get(newValue.getValue()));
		 				 								 precondition.setText(preconditions_map.get(newValue.getValue()));
		 				 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
		 				 								 
		 				 						     }
		 				 							 else {
		 				 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
		 				 								 l.setPadding(new Insets(8, 8, 8, 8));
		 				 								 operation_paras.setContent(l);
		 				 							 }	
		 				 							 
		 				 							 //op invariants
		 				 							 if (vb != null) {
		 				 							 	ScrollPane scrollPane = new ScrollPane(vb);
		 				 							 	scrollPane.setFitToWidth(true);
		 				 							 	invariants_panes.setMaxHeight(200); 
		 				 							 	//all_invariant_pane.setContent(scrollPane);	
		 				 							 	
		 				 							 	invariants_panes.setContent(scrollPane);
		 				 							 } else {
		 				 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
		 				 							     l.setPadding(new Insets(8, 8, 8, 8));
		 				 							     invariants_panes.setContent(l);
		 				 							 }
		 				 							 
		 				 							 //reset pre- and post-conditions area color
		 				 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
		 				 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
		 				 							 //reset condition panel title
		 				 							 precondition_pane.setText("Precondition");
		 				 							 postcondition_pane.setText("Postcondition");
		 				 						} 
		 				 				);

		
		
		 
		TreeItem<String> treeRootviewer = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_login = new TreeItem<String>("login");
			subTreeRoot_login.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("login")
				 		));	
			TreeItem<String> subTreeRoot_register = new TreeItem<String>("register");
			subTreeRoot_register.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("register")
				 		));	
		
		treeRootviewer.getChildren().addAll(Arrays.asList(
			subTreeRoot_login,
			subTreeRoot_register
					));
		
		treeRootviewer.setExpanded(true);

		actor_treeview_viewer.setShowRoot(false);
		actor_treeview_viewer.setRoot(treeRootviewer);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_viewer.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootposter = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_watchVideo = new TreeItem<String>("watchVideo");
			subTreeRoot_watchVideo.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("getVideoList"),
					 	new TreeItem<String>("getLowVideoList"),
					 	new TreeItem<String>("getLikedVideoList")
				 		));	
			TreeItem<String> subTreeRoot_manageVideo = new TreeItem<String>("manageVideo");
			subTreeRoot_manageVideo.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("getUserVideo")
				 		));	
		
		treeRootposter.getChildren().addAll(Arrays.asList(
			subTreeRoot_watchVideo,
			subTreeRoot_manageVideo
					));
		
		treeRootposter.setExpanded(true);

		actor_treeview_poster.setShowRoot(false);
		actor_treeview_poster.setRoot(treeRootposter);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_poster.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
	}

	/**
	*    Execute Operation
	*/
	@FXML
	public void execute(ActionEvent event) {
		
		switch (clickedOp) {
		case "commentVideo" : commentVideo(); break;
		case "isLiked" : isLiked(); break;
		case "likeVideo" : likeVideo(); break;
		case "login" : login(); break;
		case "getUserVideo" : getUserVideo(); break;
		case "register" : register(); break;
		case "getVideoInfo" : getVideoInfo(); break;
		case "updateVideo" : updateVideo(); break;
		case "uploadVideo" : uploadVideo(); break;
		case "getVideoList" : getVideoList(); break;
		case "getLowVideoList" : getLowVideoList(); break;
		case "getLikedVideoList" : getLikedVideoList(); break;
		
		}
		
		System.out.println("execute buttion clicked");
		
		//checking relevant invariants
		opInvairantPanelUpdate();
	}

	/**
	*    Refresh All
	*/		
	@FXML
	public void refresh(ActionEvent event) {
		
		refreshAll();
		System.out.println("refresh all");
	}		
	
	/**
	*    Save All
	*/			
	@FXML
	public void save(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save State to File");
		fileChooser.setInitialFileName("*.state");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showSaveDialog(stage);
		
		if (file != null) {
			System.out.println("save state to file " + file.getAbsolutePath());				
			EntityManager.save(file);
		}
	}
	
	/**
	*    Load All
	*/			
	@FXML
	public void load(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open State File");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showOpenDialog(stage);
		
		if (file != null) {
			System.out.println("choose file" + file.getAbsolutePath());
			EntityManager.load(file); 
		}
		
		//refresh GUI after load data
		refreshAll();
	}
	
	
	//precondition unsat dialog
	public void preconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Precondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}
	
	//postcondition unsat dialog
	public void postconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Postcondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}

	public void thirdpartyServiceUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("third party service is exception");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}		
	
	
	public void commentVideo() {
		
		System.out.println("execute commentVideo");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: commentVideo in service: CommentVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(commentvideoservice_service.commentVideo(
			commentVideo_userId_t.getText(),
			commentVideo_videoId_t.getText(),
			commentVideo_content_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void isLiked() {
		
		System.out.println("execute isLiked");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: isLiked in service: LikeVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(likevideoservice_service.isLiked(
			isLiked_userId_t.getText(),
			isLiked_videoId_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void likeVideo() {
		
		System.out.println("execute likeVideo");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: likeVideo in service: LikeVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(likevideoservice_service.likeVideo(
			likeVideo_userId_t.getText(),
			likeVideo_videoId_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void login() {
		
		System.out.println("execute login");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: login in service: LoginService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(loginservice_service.login(
			login_userName_t.getText(),
			login_password_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void getUserVideo() {
		
		System.out.println("execute getUserVideo");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: getUserVideo in service: ManageVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managevideoservice_service.getUserVideo(
			getUserVideo_userId_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void register() {
		
		System.out.println("execute register");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: register in service: RegisterService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(registerservice_service.register(
			register_userName_t.getText(),
			register_password_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void getVideoInfo() {
		
		System.out.println("execute getVideoInfo");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: getVideoInfo in service: UpdateVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(updatevideoservice_service.getVideoInfo(
			getVideoInfo_videoId_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void updateVideo() {
		
		System.out.println("execute updateVideo");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: updateVideo in service: UpdateVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(updatevideoservice_service.updateVideo(
			updateVideo_content_t.getText(),
			updateVideo_videoId_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void uploadVideo() {
		
		System.out.println("execute uploadVideo");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: uploadVideo in service: UploadVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(uploadvideoservice_service.uploadVideo(
			uploadVideo_userId_t.getText(),
			uploadVideo_content_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void getVideoList() {
		
		System.out.println("execute getVideoList");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: getVideoList in service: WatchVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(watchvideoservice_service.getVideoList(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void getLowVideoList() {
		
		System.out.println("execute getLowVideoList");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: getLowVideoList in service: WatchVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(watchvideoservice_service.getLowVideoList(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void getLikedVideoList() {
		
		System.out.println("execute getLikedVideoList");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: getLikedVideoList in service: WatchVideoService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(watchvideoservice_service.getLikedVideoList(
			getLikedVideoList_userId_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}




	//select object index
	int objectindex;
	
	@FXML
	TabPane mainPane;

	@FXML
	TextArea log;
	
	@FXML
	TreeView<String> actor_treeview_viewer;
	@FXML
	TreeView<String> actor_treeview_poster;
	
	@FXML
	TreeView<String> actor_treeview_administrator;


	@FXML
	TextArea definition;
	@FXML
	TextArea precondition;
	@FXML
	TextArea postcondition;
	@FXML
	TextArea invariants;
	
	@FXML
	TitledPane precondition_pane;
	@FXML
	TitledPane postcondition_pane;
	
	//chosen operation textfields
	List<TextField> choosenOperation;
	String clickedOp;
		
	@FXML
	TableView<ClassInfo> class_statisic;
	@FXML
	TableView<AssociationInfo> association_statisic;
	
	Map<String, ObservableList<AssociationInfo>> allassociationData;
	ObservableList<ClassInfo> classInfodata;
	
	VideoSystem videosystem_service;
	ThirdPartyServices thirdpartyservices_service;
	LoginService loginservice_service;
	RegisterService registerservice_service;
	WatchVideoService watchvideoservice_service;
	LikeVideoService likevideoservice_service;
	CommentVideoService commentvideoservice_service;
	ManageVideoService managevideoservice_service;
	UploadVideoService uploadvideoservice_service;
	UpdateVideoService updatevideoservice_service;
	
	ClassInfo user;
	ClassInfo watchvideo;
	ClassInfo commentvideo;
	ClassInfo videocomponent;
	ClassInfo likevideo;
	ClassInfo viewer;
	ClassInfo poster;
	ClassInfo uploadvideo;
	ClassInfo updatevideo;
		
	@FXML
	TitledPane object_statics;
	Map<String, TableView> allObjectTables;
	
	@FXML
	TitledPane operation_paras;
	
	@FXML
	TitledPane operation_return_pane;
	
	@FXML 
	TitledPane all_invariant_pane;
	
	@FXML
	TitledPane invariants_panes;
	
	Map<String, GridPane> operationPanels;
	Map<String, VBox> opInvariantPanel;
	
	//all textfiled or eumntity
	TextField commentVideo_userId_t;
	TextField commentVideo_videoId_t;
	TextField commentVideo_content_t;
	TextField isLiked_userId_t;
	TextField isLiked_videoId_t;
	TextField likeVideo_userId_t;
	TextField likeVideo_videoId_t;
	TextField login_userName_t;
	TextField login_password_t;
	TextField getUserVideo_userId_t;
	TextField register_userName_t;
	TextField register_password_t;
	TextField getVideoInfo_videoId_t;
	TextField updateVideo_content_t;
	TextField updateVideo_videoId_t;
	TextField uploadVideo_userId_t;
	TextField uploadVideo_content_t;
	TextField getLikedVideoList_userId_t;
	
	HashMap<String, String> definitions_map;
	HashMap<String, String> preconditions_map;
	HashMap<String, String> postconditions_map;
	HashMap<String, String> invariants_map;
	LinkedHashMap<String, String> service_invariants_map;
	LinkedHashMap<String, String> entity_invariants_map;
	LinkedHashMap<String, Label> service_invariants_label_map;
	LinkedHashMap<String, Label> entity_invariants_label_map;
	LinkedHashMap<String, Label> op_entity_invariants_label_map;
	LinkedHashMap<String, Label> op_service_invariants_label_map;
	

	
}
