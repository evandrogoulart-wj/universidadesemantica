package br.com.evandrogoulart;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

public class Main {
    public static void main(String[] args) {

        String fusekiURL = "http://localhost:3030/Educacao/update";

        Model model = ModelFactory.createDefaultModel();

        String ns = "http://example.org/";
        String prefixo = "ex";

        Resource cursoCC = model.createResource(ns + "CC");
        Resource cursoSI = model.createResource(ns + "SI");

        model.add(cursoCC, ResourceFactory.createProperty(ns + "nome"), "Ciência da Computaçãoo");
        model.add(cursoSI, ResourceFactory.createProperty(ns + "nome"), "Sistemas de Informação");


        RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create()
                .destination("http://localhost:3030/Educacao/")
                // Query only.
                .queryEndpoint("sparql")
                .updateEndpoint("update");

        try ( RDFConnection conn = builder.build() ) {
            conn.load(model);
            System.out.println("Ok!");
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            e.printStackTrace();
        }

    }
}