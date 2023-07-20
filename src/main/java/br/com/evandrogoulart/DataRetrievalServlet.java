package br.com.evandrogoulart;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.query.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import static org.apache.jena.enhanced.BuiltinPersonalities.model;

@WebServlet("/get")
public class DataRetrievalServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Lang lang = Lang.RDFXML;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fusekiURL = "http://localhost:3030/Educacao/query";


        String sparqlQuery = "SELECT ?curso ?nome WHERE { ?curso <http://example.org/SI> ?Sistemas de Informação }";

        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService(fusekiURL, sparqlQuery)) {
            ResultSet resultSet = queryExecution.execSelect();

            response.setContentType(lang.getContentType().getContentTypeStr());
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            OutputStream out = response.getOutputStream();

            RDFDataMgr.write(out, model, lang);

            PrintWriter out = response.getWriter();

            out.println("<html><body>");
            out.println("<table border='1'>");
            out.println("<tr><th>Curso</th><th>Nome</th></tr>");

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();
                String curso = solution.getResource("curso").toString();
                String nome = solution.getLiteral("nome").getString();

                out.println("<tr><td>" + curso + "</td><td>" + nome + "</td></tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (Exception e) {
            throw new ServletException("Erro ao recuperar os dados: " + e.getMessage(), e);
        }
    }
}

