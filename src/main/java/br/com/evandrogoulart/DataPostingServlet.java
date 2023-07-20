package br.com.evandrogoulart;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

@WebServlet("/post")
public class DataPostingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fusekiURL = "http://localhost:3030/Educacao/update";

        Model model = ModelFactory.createDefaultModel();

        String ns = "http://example.org/";
        String prefix = "ex";

        String cursoId = request.getParameter("cursoId");
        String cursoName = request.getParameter("cursoName");

        Resource cursoResource = model.createResource(ns + cursoId);
        cursoResource.addProperty(ResourceFactory.createProperty(ns + "nome"), cursoName);

        try (RDFConnection conn = RDFConnectionFactory.connectFuseki(fusekiURL)) {
            conn.update("INSERT DATA { " + model.toString() + " }");
            System.out.println("Dados RDF salvos com sucesso.");
        } catch (Exception e) {
            throw new ServletException("Erro ao persistir os dados: " + e.getMessage(), e);
        }

        response.sendRedirect("form.html");
    }
}
