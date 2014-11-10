package servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import dto.*;

/**
 * Very simplistic servlet that generates plain text. Uses the @WebServlet
 * annotation that is supported by Tomcat 7 and other servlet 3.0 containers.
 * 
 * From <a href="http://courses.coreservlets.com/Course-Materials/">the
 * coreservlets.com tutorials on servlets, JSP, Struts, JSF 2.x, Ajax, jQuery,
 * GWT, Spring, Hibernate/JPA, Hadoop, and Java programming</a>.
 */

@WebServlet("/mytransactions")
public class TransactionManager extends HttpServlet {

	private Controller controller;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		controller = new Controller();
		Properties properties = new Properties();
		try {
			properties.load(TransactionManager.class.getClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		controller.startMonitoringHotspot(properties.getProperty("hotspot"),
				properties.getProperty("processing"),
				properties.getProperty("history"));
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Content-Type", "text/xml; charset=UTF-8");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"), true);
		String s = "";
		Table table = controller.getTable();
		out.println("<table>"
				+ "     <tr>"
				+ "         <th>TransactionID</th>"
				+ "         <th>Transaction Date</th>"
				+ "         <th>Amount</th>"
				+ "         <th>Description</th>"
				+ "         <th>StartDate</th>"
				+ "         <th>EndDate</th>"
				+ "         <th>Filename</th>"
                + "     </tr>");
		for (TableRow tr : table.getTableRows()) {
			out.println(tr.asHTMLTableRow());
		}
		out.println("</table>");
	}
}
