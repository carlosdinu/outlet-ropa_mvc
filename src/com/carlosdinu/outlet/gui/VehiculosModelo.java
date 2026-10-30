package com.carlosdinu.outlet.gui;

import com.carlosdinu.outlet.base.Camiseta;
import com.carlosdinu.outlet.base.Zapatilla;
import com.carlosdinu.outlet.base.Prenda;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class VehiculosModelo {
    private ArrayList<Prenda> listaVehiculos;

    public VehiculosModelo() {
        listaVehiculos= new ArrayList<>();
    }

    public ArrayList<Prenda> obtenerVehiculos() {
        return listaVehiculos;
    }

    public void altaCoche(String matricula, String marca,
                          String modelo, LocalDate fechaMatriculacion,
                          int numPlazas) {
        Camiseta nuevoCamiseta = new Camiseta(matricula,marca,modelo,fechaMatriculacion,numPlazas);
        listaVehiculos.add(nuevoCamiseta);
    }

    public void altaMoto(String matricula, String marca,
                         String modelo, LocalDate fechaMatriculacion,
                         double kms) {
        Zapatilla nuevoZapatilla = new Zapatilla(matricula,marca,modelo,fechaMatriculacion,kms);
        listaVehiculos.add(nuevoZapatilla);
    }

    public void eliminarVehiculo(Prenda vehiculo) {
        listaVehiculos.remove(vehiculo);
    }

    public boolean existeMatricula(String matricula) {
        for (Prenda unVehiculo:listaVehiculos) {
            if (unVehiculo.getCodigo().equals(matricula)) {
                return true;
            }
        }
        return false;
    }

    public void exportarXML (File fichero) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
        DocumentBuilder builder= factory.newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        Document documento = dom.createDocument(null,"xml",null);

        //añado el nodo raiz
        Element raiz = documento.createElement("vehiculos");
        documento.getDocumentElement().appendChild(raiz);

        Element nodoVehiculo=null;
        Element nodoDatos=null;
        Text texto=null;

        for (Prenda unVehiculo:listaVehiculos) {
            //añadimos dentro de vehiculos, coches o motos
            if (unVehiculo instanceof Camiseta) {
                nodoVehiculo=documento.createElement("Coche");
            } else {
                nodoVehiculo=documento.createElement("Moto");
            }
            raiz.appendChild(nodoVehiculo);

            //dentro de la etiqueta veniculo -> marca, modelo ....
            nodoDatos=documento.createElement("matricula");
            nodoVehiculo.appendChild(nodoDatos);
            texto=documento.createTextNode(unVehiculo.getCodigo());
            nodoDatos.appendChild(texto);

            nodoDatos=documento.createElement("marca");
            nodoVehiculo.appendChild(nodoDatos);
            texto=documento.createTextNode(unVehiculo.getMarca());
            nodoDatos.appendChild(texto);

            nodoDatos=documento.createElement("modelo");
            nodoVehiculo.appendChild(nodoDatos);
            texto=documento.createTextNode(unVehiculo.getNombre());
            nodoDatos.appendChild(texto);

            nodoDatos=documento.createElement("fechamatriculacion");
            nodoVehiculo.appendChild(nodoDatos);
            texto=documento.createTextNode(unVehiculo.getFechaEntrada().toString());
            nodoDatos.appendChild(texto);

            //ponemos el campo diferente
            if (unVehiculo instanceof Camiseta) {
                nodoDatos=documento.createElement("numeroplazas");
                nodoVehiculo.appendChild(nodoDatos);
                texto=documento.createTextNode(String.valueOf(((Camiseta) unVehiculo).getTalla()));
                nodoDatos.appendChild(texto);
            } else {
                nodoDatos=documento.createElement("kms");
                nodoVehiculo.appendChild(nodoDatos);
                texto=documento.createTextNode(String.valueOf(((Zapatilla)unVehiculo).getNumero()));
                nodoDatos.appendChild(texto);
            }
        }
        //guardo en fichero
        Source source = new DOMSource(documento);
        Result resultado = new StreamResult(fichero);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source,resultado);
    }

    public void importarXML (File fichero) throws ParserConfigurationException, IOException, SAXException {
        listaVehiculos= new ArrayList<Prenda>();
        Camiseta nuevoCamiseta = null;
        Zapatilla nuevaZapatilla =null;

        DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
        DocumentBuilder builder= factory.newDocumentBuilder();
        Document documento = builder.parse(fichero);

        NodeList listaElementos = documento.getElementsByTagName("*");

        for (int i=0;i<listaElementos.getLength();i++) {
            Element nodoVehiculo= (Element) listaElementos.item(i);

            if (nodoVehiculo.getTagName().equals("Coche")) {
                nuevoCamiseta = new Camiseta();
                nuevoCamiseta.setCodigo(nodoVehiculo.getChildNodes().item(0).getTextContent());
                nuevoCamiseta.setMarca(nodoVehiculo.getChildNodes().item(1).getTextContent());
                nuevoCamiseta.setNombre(nodoVehiculo.getChildNodes().item(2).getTextContent());
                nuevoCamiseta.setFechaEntrada(LocalDate.parse(nodoVehiculo.getChildNodes().item(3).getTextContent()));
                nuevoCamiseta.setTalla(Integer.parseInt(nodoVehiculo.getChildNodes().item(4).getTextContent()));
                listaVehiculos.add(nuevoCamiseta);
            } else{
                if (nodoVehiculo.getTagName().equals("Moto")) {
                    nuevaZapatilla = new Zapatilla();
                    nuevaZapatilla.setCodigo(nodoVehiculo.getChildNodes().item(0).getTextContent());
                    nuevaZapatilla.setMarca(nodoVehiculo.getChildNodes().item(1).getTextContent());
                    nuevaZapatilla.setNombre(nodoVehiculo.getChildNodes().item(2).getTextContent());
                    nuevaZapatilla.setFechaEntrada(LocalDate.parse(nodoVehiculo.getChildNodes().item(3).getTextContent()));
                    nuevaZapatilla.setNumero(Double.parseDouble(nodoVehiculo.getChildNodes().item(4).getTextContent()));
                    listaVehiculos.add(nuevaZapatilla);
                }
            }

        }
    }
}
