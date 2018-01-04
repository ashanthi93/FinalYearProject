package org.sse.userinterface.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;
import org.dom4j.DocumentException;
import org.sse.design.ThreatExtractor;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.sse.design.model.ThreatCategory;
import org.sse.knowedgemodel.prolog.PrologConverter;
import org.sse.outputgenerators.FileFormat;
import org.sse.outputgenerators.ReportType;

import javafx.scene.control.cell.PropertyValueFactory;
import org.dom4j.DocumentException;
import org.sse.design.ThreatCategoriesLoader;
import org.sse.design.model.Threat;
import org.sse.design.model.ThreatMitigation;

import org.sse.outputgenerators.report.builder.concrete.JSONReportBuilder;
import org.sse.outputgenerators.report.builder.concrete.XMLReportBuilder;
import org.sse.outputgenerators.report.creator.AssociationReportCreator;
import org.sse.outputgenerators.report.creator.BugCategoryReportCreator;
import org.sse.outputgenerators.report.creator.ThreatCategoryReportCreator;
import org.sse.outputgenerators.report.model.AssociationReport;
import org.sse.outputgenerators.report.model.BugReport;
import org.sse.outputgenerators.report.model.ThreatReport;
import org.sse.source.model.Bug;
import org.sse.source.model.BugCategory;
import org.sse.source.model.BugCountermeasures;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import static org.sse.userinterface.controller.BugInputWindowController.updetedList;

public class HomeWindowController implements Initializable {

    public static boolean isHomeOpened = false;
    public static String selectedIndex = "NONE";
    static PrologConverter prolog = new PrologConverter();

    @FXML
    private JFXButton newProjectBtn;
    @FXML
    private JFXButton sourceCancelBtn;

    // Table to hold source code bugs and details
    @FXML
    private TableView<BugCategory> OWASPT10_Table;

    @FXML
    private TableColumn<BugCategory, String> t10_id;
    @FXML
    private TableColumn<BugCategory, String> t10_name;
    @FXML
    private TableColumn<BugCategory, String> t10_description;

    // create design table
    @FXML
    private TableView<ThreatMitigation> designTable;

    @FXML
    private TableColumn<ThreatMitigation, String> designThreatColumn;
    @FXML
    private TableColumn<ThreatMitigation, String> designCategoryColumn;
    @FXML
    private TableColumn<ThreatMitigation, String> designMitigationColumn;

    @FXML
    private TabPane homeTabPane;

    private HashMap<String, ThreatCategory> threatMap;
    private ObservableList<ThreatMitigation> threatData;

    @FXML
    private TableView<BugCountermeasures> sourceTable;

    @FXML
    private TableColumn<BugCountermeasures, String> sourceBugColumn;
    @FXML
    private TableColumn<BugCountermeasures, String> sourceCategoryColumn;
    @FXML
    private TableColumn<BugCountermeasures, String> sourcePreventionColumn;

    private ObservableList<BugCountermeasures> bugData;

    public void start(String path, String title, Boolean resizable, int index) {

        try{
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");

            stage.setTitle(title);
            stage.setResizable(resizable);
            stage.setScene(scene);
            stage.show();

            TabPane tabs = (TabPane) scene.lookup("#settingsTabPane");
            SingleSelectionModel<Tab> selectionModel = tabs.getSelectionModel();
            selectionModel.select(index);
        }catch(Exception e){
            Alert alert = NewProjectWindowController.createAlert(Alert.AlertType.ERROR, "Error!", null, "\n  Error occured while opening the Settings Window.");
            alert.showAndWait();
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        setThreatProperties();
        setBugProperties();

        if (selectedIndex.equals("DESIGN")) {
            List<Tab> tabs = new ArrayList(homeTabPane.getTabs());
            tabs.sort((o1, o2) -> o1.getText().compareTo(o2.getText()));
            homeTabPane.getTabs().clear();
            homeTabPane.getTabs().setAll(tabs);
            homeTabPane.getSelectionModel().select(0);
        }
    }

    public HomeWindowController() {
        threatLoader();
        bugLoader();
        try {
            initializeDesignTab();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws DocumentException
     */
    private void initializeDesignTab() throws DocumentException {

    }

    private void threatLoader() {

        try{
            threatMap = ThreatCategoriesLoader.getThreatCategoryHashMap();

            int id = 0;
            HashMap<Integer, ThreatMitigation> threatObjects = new HashMap<>();

            for (String key : threatMap.keySet()) {

                ThreatCategory categoryList = threatMap.get(key);
                List<Threat> list = categoryList.getThreatList();

                for (Threat threat : list) {

                    List<String> mitigations = categoryList.getMitigationList();

                    ThreatCategory threatCategory = threatMap.get(key);
                    threatCategory.setMitigationList(mitigations);
                    threatMap.put(key, threatCategory);

                    ThreatMitigation threatmitigation = new ThreatMitigation();
                    threatmitigation.setThreat(threat.getName());
                    threatmitigation.setCategory(threat.getThreatCategoryName());
                    threatmitigation.setMitigation(mitigations.get(0));

                    threatObjects.put(id, threatmitigation);
                    id++;

                    for (int i = 1; i < mitigations.size(); i++) {
                        ThreatMitigation threatmitigationCopy = new ThreatMitigation();
                        threatmitigationCopy.setThreat("");
                        threatmitigationCopy.setCategory("");
                        threatmitigationCopy.setMitigation(mitigations.get(i));

                        threatObjects.put(id, threatmitigationCopy);
                        id++;
                    }
                }
            }
            threatData = FXCollections.observableArrayList(threatObjects.values());

        }catch(DocumentException ex){
            Alert alert = NewProjectWindowController.createAlert(Alert.AlertType.ERROR, "Error!", null, "\n  Error occured while loading Threats.");
            alert.showAndWait();
        }catch(Exception e){
            Alert alert = NewProjectWindowController.createAlert(Alert.AlertType.ERROR, "Error!", null, "\n  Error occured while loading Threats.");
            alert.showAndWait();
        }
    }

    public void bugLoader(){

        List<Bug> bugs =  BugInputWindowController.updetedList;
        HashMap<Integer, BugCountermeasures> bugObjects = new HashMap<>();
        int id = 0;
        for (Bug bug :bugs) {

            String [] category = bug.getCategoryName().split(":");
            List<String> preventions = prolog.getPreventionTechniques(category[0].toLowerCase());

            BugCountermeasures bugcountermeasure = new BugCountermeasures();
            bugcountermeasure.setBug(bug.getName());
            bugcountermeasure.setCategory(bug.getCategoryName());
            bugcountermeasure.setCountermeasure(preventions.get(0));
            bugObjects.put(id,bugcountermeasure);
            id++;
            for (int i = 1; i <preventions.size() ; i++) {
                BugCountermeasures bugcountermeasureCopy = new BugCountermeasures();
                bugcountermeasureCopy.setBug("");
                bugcountermeasureCopy.setCategory("");
                bugcountermeasureCopy.setCountermeasure(preventions.get(i));
                bugObjects.put(id,bugcountermeasureCopy);
            }
        }
        bugData = FXCollections.observableArrayList(bugObjects.values());

    }

    /**
     *
     */
    private void setThreatProperties() {

        designThreatColumn.setCellValueFactory(new PropertyValueFactory<ThreatMitigation, String>("threat"));
        designThreatColumn.prefWidthProperty().bind(designTable.widthProperty().divide(5));

        designCategoryColumn.setCellValueFactory(new PropertyValueFactory<ThreatMitigation, String>("category"));
        designCategoryColumn.prefWidthProperty().bind(designTable.widthProperty().divide(5));

        designMitigationColumn.setCellValueFactory(new PropertyValueFactory<ThreatMitigation, String>("mitigation"));
        designMitigationColumn.prefWidthProperty().bind(designTable.widthProperty().divide(1.5));

        designTable.setItems(threatData);
    }

    public void setBugProperties() {

        sourceBugColumn.setCellValueFactory(new PropertyValueFactory<BugCountermeasures, String>("bug"));
        sourceBugColumn.prefWidthProperty().bind(sourceTable.widthProperty().divide(5));

        sourceCategoryColumn.setCellValueFactory(new PropertyValueFactory<BugCountermeasures, String>("category"));
        sourceCategoryColumn.prefWidthProperty().bind(sourceTable.widthProperty().divide(5));

        sourcePreventionColumn.setCellValueFactory(new PropertyValueFactory<BugCountermeasures, String>("countermeasure"));
        sourcePreventionColumn.prefWidthProperty().bind(sourceTable.widthProperty().divide(1.5));

        sourceTable.setItems(bugData);
    }

    @FXML
    private void settingsSubAction(ActionEvent event) {
        start("/fxml/Settings.fxml", "Settings", true, 0);
    }

    @FXML
    private void sourceNextBtnAction(ActionEvent event) {
        int selectedNum = homeTabPane.getSelectionModel().getSelectedIndex();

        if (selectedNum == 0) {
            boolean returned = fileOpen("Select Threat Report", "TMT Files (*.tm7)", "*.tm7");
            if (returned) {
                homeTabPane.getSelectionModel().select(1);
                threatLoader();
                setThreatProperties();
            }
        } else {
            homeTabPane.getSelectionModel().select(2);
        }
    }

    @FXML
    private void designSaveBtnAction(ActionEvent event) {

        try {
            this.saveReport(ReportType.THREAT_REPORT, FileFormat.CNX);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void designNextBtnAction(ActionEvent event) {
        int selectedNum = homeTabPane.getSelectionModel().getSelectedIndex();
        isHomeOpened = true;

        if (selectedNum == 0) {
            start("/fxml/BugInputWindow.fxml", "Bug Input Window");
            homeTabPane.getSelectionModel().select(1);
            bugLoader();
            setBugProperties();

        } else {
            homeTabPane.getSelectionModel().select(2);
        }
    }

    private void cancelBtnMethod(){
        Alert alert = this.createAlert(Alert.AlertType.CONFIRMATION, "Confirm!", null, "\n Are you sure you want to exit?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait();
        if(!alert.getResult().getButtonData().isCancelButton()){
            Stage homeStage = (Stage) this.sourceCancelBtn.getScene().getWindow();
            homeStage.close();
        }
    }

    @FXML
    private void sourceCancelBtnAction(ActionEvent event){
        try{
            cancelBtnMethod();
        }catch(Exception e){
            Alert alert = this.createAlert(Alert.AlertType.ERROR, "Error!", null, "\n Error occurred while closing the window.");
            alert.showAndWait();
        }
    }
    @FXML
    private void designCancelBtnAction(ActionEvent event){
        try{
            cancelBtnMethod();
        }catch(Exception e){
            Alert alert = this.createAlert(Alert.AlertType.ERROR, "Error!", null, "\n Error occurred while closing the window.");
            alert.showAndWait();
        }
    }
    @FXML
    private void associationCancelBtnAction(ActionEvent event){
        try{
            cancelBtnMethod();
        }catch(Exception e){
            Alert alert = this.createAlert(Alert.AlertType.ERROR, "Error!", null, "\n Error occurred while closing the window.");
            alert.showAndWait();
        }
    }

    private boolean fileOpen(String title, String displayName, String fileType) {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(displayName, fileType);
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = new Stage();
        fileChooser.setTitle(title);
        File file = fileChooser.showOpenDialog(stage);

        try {
            if (file != null) {

                ThreatExtractor threatExtractor = ThreatExtractor.getInstance();

                if (threatExtractor.readFile(file)) {

                    threatExtractor.classifyThreats();
                    return true;

                } else {
                    Alert alert = this.createAlert(Alert.AlertType.ERROR, "Error", null, "\n Threat report validation fails !");

                    alert.showAndWait();
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Invalid Threat Model", "\n Threat Category model does not maatch with STRIDE !");
            alert.showAndWait();
        } catch (DocumentException e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Invalid File", "\n Threat Report is invalid !");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void start(String path, String title) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");

            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Error occured while opeing the window.");
            alert.showAndWait();
        }
    }

    public static Alert createAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert;
    }

    @FXML
    private void sourceSaveBtnAction(ActionEvent event) {

        try {
            this.saveReport(ReportType.BUG_REPORT, FileFormat.CNX);

        } catch (JsonProcessingException e) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        }
    }

    @FXML
    private void analysisSaveBtnAction(ActionEvent event) {

        try {
            this.saveReport(ReportType.BUG_REPORT, FileFormat.CNX);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        }
    }

    @FXML
    private void xmlMenuItemAction(ActionEvent event) {

        try {
            Tab selectedTab = homeTabPane.getSelectionModel().getSelectedItem();

            if (selectedTab.getId().equals("sourceTab")) {
                this.saveReport(ReportType.BUG_REPORT, FileFormat.XML);

            } else if (selectedTab.getId().equals("designTab")) {
                this.saveReport(ReportType.THREAT_REPORT, FileFormat.XML);

            } else {
                this.saveReport(ReportType.ASSOCIATION_REPORT, FileFormat.XML);
            }
        } catch (JsonProcessingException e) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        }
    }

    @FXML
    private void jsonMenuItemAction(ActionEvent event) {

        try {
            Tab selectedTab = homeTabPane.getSelectionModel().getSelectedItem();

            if (selectedTab.getId().equals("sourceTab")) {
                this.saveReport(ReportType.BUG_REPORT, FileFormat.JSON);

            } else if (selectedTab.getId().equals("designTab")) {
                this.saveReport(ReportType.THREAT_REPORT, FileFormat.JSON);

            } else {
                this.saveReport(ReportType.ASSOCIATION_REPORT, FileFormat.JSON);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to save the file.!");
            alert.showAndWait();
        }
    }

    @FXML
    private void newMenuItemAction(ActionEvent event) {

        Stage previousStage = (Stage) this.newProjectBtn.getScene().getWindow();
        previousStage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/NewProjectWindow.fxml"));
            Stage stage = new Stage();
            MainController.newProjectWindow = new Scene(root);
            MainController.newProjectWindow.getStylesheets().add("/styles/Styles.css");

            stage.setTitle("Start New Project");
            stage.setResizable(false);
            stage.setScene(MainController.newProjectWindow);
            stage.show();

        } catch (IOException e) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to open the window.");
            alert.showAndWait();
        }
    }

    @FXML
    private void openMenuItemAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CNX File", ".cnx")
        );
        Stage stage = new Stage();
        fileChooser.setTitle("Open Project");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null){
            System.out.println("File open");
        }
    }

    /**
     * @param reportType
     * @param fileFormat
     * @throws JsonProcessingException
     */
    private void saveReport(ReportType reportType, FileFormat fileFormat) throws IOException, SAXException, ParserConfigurationException {

        Object reportObject;

        switch (reportType) {
            case THREAT_REPORT:
                reportObject = this.convertToThreatReport();
                break;
            case BUG_REPORT:
                reportObject = this.convertToBugReport();
                break;
            case ASSOCIATION_REPORT:
                reportObject = this.convertToAssociationReport();
                break;
            default:
                return;
        }

        if (reportObject != null) {

            String outputFileAsString;
            String fileDescription;
            String fileExtension;

            switch (fileFormat) {
                case CNX:
                    outputFileAsString = this.convertToXML(reportObject);
                    fileDescription = "CNX File";
                    fileExtension = "*.cnx";
                    break;
                case XML:
                    outputFileAsString = this.convertToXML(reportObject);
                    fileDescription = "XML File";
                    fileExtension = "*.xml";
                    break;
                case JSON:
                    outputFileAsString = this.convertToJSON(reportObject);
                    fileDescription = "JSON File";
                    fileExtension = "*.json";
                    break;
                default:
                    return;
            }

            boolean isSaveSucceed = this.fileSaveAction(outputFileAsString, fileDescription, fileExtension);

            if (!isSaveSucceed) {
                /*
                * error message
                */
            }
        } else {
            throw new NullPointerException("Report can not be null");
        }
    }

    /**
     * @param outputXMLFile
     * @param fileDescription
     * @param fileExtensionFormat
     * @return
     */
    private boolean fileSaveAction(String outputXMLFile, String fileDescription, String fileExtensionFormat) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(fileDescription, fileExtensionFormat)
        );

        File file = fileChooser.showSaveDialog(this.newProjectBtn.getScene().getWindow());

        if (file != null) {
            try (PrintStream ps = new PrintStream(file)) {

                ps.print(outputXMLFile);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @return
     */
    private ThreatReport convertToThreatReport() {

        HashMap<String, ThreatCategory> threatCategoryHashMap = new HashMap<>();

        for (ThreatCategory threatCategory : threatMap.values()) {


            threatCategoryHashMap.put(threatCategory.getId(), threatCategory);
        }

        ThreatCategoryReportCreator threatCategoryReportCreator = new ThreatCategoryReportCreator(threatCategoryHashMap);
        ThreatReport threatReport = threatCategoryReportCreator.generateReport("Threats Analysis Report");

        return threatReport;
    }

    /**
     * @return
     */
    private BugReport convertToBugReport() {

        HashMap<String, BugCategory> bugCategoryHashMap = new HashMap<>();

        for (BugCategory bugCategory : OWASPT10_Table.getItems()) {
            bugCategoryHashMap.put(bugCategory.getId(), bugCategory);
        }

        BugCategoryReportCreator bugCategoryReportCreator = new BugCategoryReportCreator(bugCategoryHashMap);
        BugReport bugReport = bugCategoryReportCreator.generateReport("Bug Analysis Report");

        return bugReport;
    }

    /**
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private AssociationReport convertToAssociationReport() {
        AssociationReport associationReport = null;
        try {
            HashMap<BugCategory, String[]> bugCategoryToThreatCategoryMapping = new HashMap<>();
            HashMap<String, ThreatCategory> threatCategoryHashMap = new HashMap<>();

            AssociationReportCreator associationReportCreator =
                    new AssociationReportCreator(bugCategoryToThreatCategoryMapping, threatCategoryHashMap);
            associationReport = associationReportCreator.generateReport("Association Analysis Report");

            return associationReport;
        } catch (ParserConfigurationException pce) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to convert the report.");
            alert.showAndWait();
        } catch (SAXException se) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to convert the report.");
            alert.showAndWait();
        } catch (IOException ioe) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to convert the report.");
            alert.showAndWait();
        }catch(Exception e){
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to convert the report.");
            alert.showAndWait();
        }
        return associationReport;
    }

    /**
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    private String convertToXML(Object object) {
        String xmlReport = null;
        try {
            XMLReportBuilder xmlReportBuilder = new XMLReportBuilder();
            xmlReport = xmlReportBuilder.convertReport(object);

            return xmlReport;
        } catch (JsonProcessingException je) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to convert to XML.");
            alert.showAndWait();
        }catch (Exception e){
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to convert to XML.");
            alert.showAndWait();
        }
        return xmlReport;
    }

    /**
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    private String convertToJSON(Object object) {
        String jsonReport = null;
        try {
            JSONReportBuilder jsonReportBuilder = new JSONReportBuilder();
            jsonReport = jsonReportBuilder.convertReport(object);

            return jsonReport;
        } catch (JsonProcessingException je) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to convert to JSON.");
            alert.showAndWait();
        }catch(Exception e){
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", "Error!", "\n Unable to convert to JSON.");
            alert.showAndWait();
        }
        return jsonReport;
    }
}
