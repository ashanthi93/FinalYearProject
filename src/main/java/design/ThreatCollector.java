package design;

import com.sun.org.apache.regexp.internal.RE;
import design.model.Interaction;
import design.model.Threat;
import design.model.ThreatModel;

import java.io.File;
import java.util.ArrayList;

/*
 * Object from this class can be created when select report by the ui to analyze
 */
public class ThreatCollector {

    private ThreatModel threatModel;
    private ArrayList<Interaction> interactionArrayList;
    private ArrayList<Threat> threatArrayList;

    public ThreatCollector() {
    }

    public void readFile(File xmlFile) {

        ReportParser reportParser = new ReportParser();
        /*
        * This file should read xml and
        * generate ThreatModel, Interaction and Threats
        */
    }

    /* getters */
    public ThreatModel getThreatModel() {
        return threatModel;
    }

    public ArrayList<Interaction> getInteractionArrayList() {
        return interactionArrayList;
    }

    public ArrayList<Threat> getThreatArrayList() {
        return threatArrayList;
    }

    /**
     * @param threatModelId
     * @param diagramName
     */
    private void createThreatModel(String threatModelId, String diagramName) {

        threatModel.setId(threatModelId);
        threatModel.setDiagramName(diagramName);
        threatModel.setInteractions(interactionArrayList);
    }

    /**
     * Create interaction object, then add into global interaction arrayList of ThreatModel
     * and then return the interaction object.
     *
     * @param interactionName
     * @param threatArrayList arrayList containing threats of the interaction
     * @return interaction object
     */
    private Interaction createInteraction(String interactionName, ArrayList<Threat> threatArrayList) {

        /* create interaction object */
        Interaction interaction = new Interaction();

        interaction.setName(interactionName);
        interaction.setThreats(threatArrayList);

        //add interaction object to interactionArrayList
        interactionArrayList.add(interaction);

        return interaction;
    }

    /**
     * @param threatId
     * @param threatName
     * @param threatCateogryName
     * @param description
     * @return
     */
    private Threat createThreat(String threatId, String threatName, String threatCateogryName, String description) {

        Threat threat = new Threat();

        threat.setId(threatId);
        threat.setName(threatName);
        threat.setThreatCategoryName(threatCateogryName);
        threat.setDescription(description);

        threatArrayList.add(threat);

        return threat;
    }

}
