package org.sse.knowedgemodel.prolog;

import org.dom4j.DocumentException;
import org.sse.design.model.ThreatControl;
import org.sse.settings.config.design.ThreatModelConfig;
import org.sse.settings.config.design.control.AttackerControlConfig;
import org.sse.settings.config.design.control.DefensiveControlConfig;
import org.sse.settings.config.source.control.BugControlConfig;
import org.sse.settings.config.source.BugModelConfig;
import org.sse.settings.config.source.mapping.MappingConfig;
import org.sse.source.model.BugCategory;
import org.sse.source.model.BugControl;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AssociationLoader {

    public void callProlog() throws DocumentException, IOException, ParserConfigurationException, SAXException {

        PrintWriter writer = new PrintWriter("src/main/resources/prolog/knowledgeBase.pl");
        writer.print(""); // clear the existing file.
        writer.println(":- [rules].");
        writer.println(":- [stride].");
        writer.println("");
        writer.println("");


        // write stride frames to the prolog file
       /* ArrayList<String[]> stride = ThreatModelConfig.loadConfigFile();
        List<ThreatControl> strideMapping = AttackerControlConfig.loadConfigFile();
        for (int i = 0; i < stride.size(); i++) {
            String data[] = stride.get(i);
            String defensive = "";
            //System.out.println(data[1] + ", " + data[2] + ", " + data[3] );
            List<String> mapping = strideMapping.get(i).getDescription();
            for (int j = 0; j < mapping.length; j++) {
                if (j > 0) {
                    defensive = defensive + "," + mapping[j].toLowerCase();
                } else {
                    defensive = defensive + mapping[j].toLowerCase();
                }

            }
            String s = "stride(\n" + data[1].toLowerCase().replace(' ', '_') + ",\n" + "security_control(\"" + data[2] + "\")" + ",\n" + "[" + defensive + "]).";
            // writer.println(s);
        }*/


        // write asf data to prolog file
        /*List<ThreatControl> asf = DefensiveControlConfig.loadConfigFile();
        for (int i = 0; i < asf.size(); i++) {
            //String data[] = asf.get(i);
            String s = "stride_defensive(\n" + data[0].toLowerCase() + ",\n" + "name(\"" + data[1] + "\")).";
            //System.out.println(s);
            //writer.println(s);

        }*/

        //write owasp top 10 to the prolog file
        List<BugCategory> owasp = BugModelConfig.loadConfigFile();
        HashMap<String, String[]> owaspMapping = MappingConfig.loadConfigFile();



        for (int i = 0; i < owasp.size(); i++) {
            //String[] data = owasp.get(i);
            String proactives = "";
            String[] mapping = owaspMapping.get(owasp.get(i).getId());
            System.out.println(mapping.length);
            for (int j = 0; j < mapping.length; j++) {
                if (j > 0) {
                    proactives = proactives + "," + mapping[j].toLowerCase();
                } else {
                    proactives = proactives + mapping[j].toLowerCase();
                }

            }

            System.out.println();
            String s = "owasp_top10(\n" + owasp.get(i).getId().toLowerCase() + ",\n" + "name(\"" + owasp.get(i).getName() + "\")" + ",\n" + "[" + proactives + "]).";
            //System.out.println(s);
            writer.println(s);

        }
        writer.println();
        writer.println();


        //write owasp proactives to the prolog file
        List<BugControl> proactive = BugControlConfig.loadConfigFile();
        for (int i = 0; i < proactive.size(); i++) {
            //String data[] = proactive.get(i).getName();
            String s = "owasp_top10_proactive(\n" + proactive.get(i).getId().toLowerCase() + ",\n" + "name(\"" + proactive.get(i).getName() + "\"), \n" + "\"" + proactive.get(i).getId().toLowerCase() + " description\"\n" + ").";
            //System.out.println(s);
            writer.println(s);
            writer.println();

        }

        /*writer.println();
        writer.println();

        writer.println("owasp(Bug_type):-\n" +
                "\towasp_top10(\n" +
                "\t\tBug_type,\n" +
                "\t\t_,\n" +
                "\t\tY\n" +
                "\t),\n" +
                "\tlength(Y,A),\n" +
                "\ttestloop(0,A,Y).\n" +
                "\n" +
                "testloop(N, Length, List):- \n" +
                "\tN<Length, \n" +
                "\tnth0(N,List,B),\n" +
                "\tget_proactive_description(B), nl, \n" +
                "\tM is N+1, \n" +
                "\ttestloop(M,Length,List).\n" +
                "\n" +
                "get_proactive_description(Name):-\n" +
                "\towasp_top10_proactive(\n" +
                "\t\tName,\n" +
                "\t\t_,\n" +
                "\t\tY\n" +
                "\t\t),\n" +
                "\twrite(Y).\n" +
                "\n" +
                "remove_frame(A):-\n" +
                "\towasp_top10_proactive(\n" +
                "\t\tA,\n" +
                "\t\t_,\n" +
                "\t\t_\n" +
                "\t),\n" +
                "\tretract(owasp_top10_proactive(A,_,_)).");*/
        writer.close();


        new PrologConverter().prologCaller("a1");

        /*while(q2.hasMoreSolutions()){
            Map solution = q2.nextSolution();
            System.out.println(solution.get("A"));
        }*/

    }



}
