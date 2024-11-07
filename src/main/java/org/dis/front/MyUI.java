package org.dis.front;

import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.dis.back.BRException;
import org.dis.back.EmpleadoBR;

/**
 * This UI is the application entry point.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private TextField creatLabel(String texto) {
        TextField etiqueta = new TextField();
        etiqueta.setCaption(texto);
        return etiqueta;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout salarioBruto = new HorizontalLayout();
        final HorizontalLayout salarioNeto = new HorizontalLayout();
        final VerticalLayout salarioBrutoContenedor = new VerticalLayout();
        final VerticalLayout salarioNetoContenedor = new VerticalLayout();

        TextField tipo = creatLabel("Tipo de empleado");
        TextField ventasMes = creatLabel("Venta del mes");
        TextField horasExtra = creatLabel("Horas extra");

        salarioBruto.addComponents(tipo, ventasMes, horasExtra);

        Button botonSalarioBruto = new Button("Calcula Salario Bruto");
        botonSalarioBruto.addClickListener(e -> {
            String tipoEmpleadoIn = tipo.getValue();
            double ventasMesIn = Double.parseDouble(ventasMes.getValue());
            double horasExtrasIn = Double.parseDouble(horasExtra.getValue());

            EmpleadoBR empleado = new EmpleadoBR();

            try {
                double resultado = empleado.calculaSalarioBruto(tipoEmpleadoIn, ventasMesIn, horasExtrasIn);
                Label labelSalarioBruto = new Label("El salario bruto obtenido es: " + resultado + "€");
                salarioBrutoContenedor.addComponent(labelSalarioBruto);
            } catch (BRException ex) {
                Label labelSalarioBruto = new Label(ex.getMessage());
                salarioBrutoContenedor.addComponent(labelSalarioBruto);
            }
        });

        Button botonSalarioNeto = new Button("Calcula Salario Neto");
        botonSalarioNeto.addClickListener(e -> {
            double salarioBrutoIn = Double.parseDouble(tipo.getValue());

            EmpleadoBR empleado = new EmpleadoBR();

            try {
                double resultado = empleado.calculaSalarioNeto(salarioBrutoIn);
                Label labelSalarioNeto = new Label("El salario neto obtenido es: " + resultado + "€");
                salarioNetoContenedor.addComponent(labelSalarioNeto);
            } catch (BRException ex) {
                Label labelSalarioNeto = new Label(ex.getMessage());
                salarioNetoContenedor.addComponent(labelSalarioNeto);
            }
        });

        salarioBrutoContenedor.addComponents(salarioBruto, botonSalarioBruto);
        salarioNetoContenedor.addComponents(salarioNeto, botonSalarioNeto);

        TabSheet tabs = new TabSheet();
        tabs.addTab(salarioBrutoContenedor).setCaption("Calcula Salario Bruto");
        tabs.addTab(salarioNetoContenedor).setCaption("Calcula Salario Neto");

        layout.addComponents(tabs);
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
