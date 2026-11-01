package com.carlosdinu.outlet.gui;

import com.carlosdinu.outlet.base.Camiseta;
import com.carlosdinu.outlet.base.Pantalon;
import com.carlosdinu.outlet.base.Prenda;
import com.carlosdinu.outlet.base.Zapatilla;

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

public class OutletModelo {
    private ArrayList<Prenda> listaPrendas;

    public OutletModelo() {
        listaPrendas = new ArrayList<>();
    }

    public ArrayList<Prenda> obtenerPrendas() {
        return listaPrendas;
    }

    public void altaCamiseta(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                             double precio, int descuento, String color, String descripcion,
                             String talla) {
        listaPrendas.add(new Camiseta(codigo, marca, nombre, fechaEntrada,
                precio, descuento, color, descripcion, talla));
    }

    public void altaPantalon(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                             double precio, int descuento, String color, String descripcion,
                             int tallaCintura) {
        listaPrendas.add(new Pantalon(codigo, marca, nombre, fechaEntrada,
                precio, descuento, color, descripcion, tallaCintura));
    }

    public void altaZapatilla(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                              double precio, int descuento, String color, String descripcion,
                              double numero) {
        listaPrendas.add(new Zapatilla(codigo, marca, nombre, fechaEntrada,
                precio, descuento, color, descripcion, numero));
    }

    public void eliminarPrenda(Prenda prenda) {
        listaPrendas.remove(prenda);
    }

    public boolean existeCodigo(String codigo) {
        for (Prenda unaPrenda : listaPrendas) {
            if (unaPrenda.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    public void exportarXML(File fichero) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        Document documento = dom.createDocument(null, "xml", null);

        Element raiz = documento.createElement("prendas");
        documento.getDocumentElement().appendChild(raiz);

        for (Prenda unaPrenda : listaPrendas) {
            Element nodoPrenda;
            if (unaPrenda instanceof Camiseta) {
                nodoPrenda = documento.createElement("Camiseta");
            } else if (unaPrenda instanceof Pantalon) {
                nodoPrenda = documento.createElement("Pantalon");
            } else {
                nodoPrenda = documento.createElement("Zapatilla");
            }
            raiz.appendChild(nodoPrenda);

            crearElemento(documento, nodoPrenda, "codigo", unaPrenda.getCodigo());
            crearElemento(documento, nodoPrenda, "marca", unaPrenda.getMarca());
            crearElemento(documento, nodoPrenda, "nombre", unaPrenda.getNombre());
            crearElemento(documento, nodoPrenda, "fechaentrada", unaPrenda.getFechaEntrada().toString());
            crearElemento(documento, nodoPrenda, "precio", String.valueOf(unaPrenda.getPrecio()));
            crearElemento(documento, nodoPrenda, "descuento", String.valueOf(unaPrenda.getDescuento()));
            crearElemento(documento, nodoPrenda, "color", unaPrenda.getColor());
            crearElemento(documento, nodoPrenda, "descripcion", unaPrenda.getDescripcion());

            if (unaPrenda instanceof Camiseta) {
                crearElemento(documento, nodoPrenda, "talla", ((Camiseta) unaPrenda).getTalla());
            } else if (unaPrenda instanceof Pantalon) {
                crearElemento(documento, nodoPrenda, "tallacintura",
                        String.valueOf(((Pantalon) unaPrenda).getTallaCintura()));
            } else {
                crearElemento(documento, nodoPrenda, "numero",
                        String.valueOf(((Zapatilla) unaPrenda).getNumero()));
            }
        }

        Source source = new DOMSource(documento);
        Result resultado = new StreamResult(fichero);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, resultado);
    }

    private void crearElemento(Document documento, Element padre, String etiqueta, String valor) {
        Element nodoDatos = documento.createElement(etiqueta);
        padre.appendChild(nodoDatos);
        nodoDatos.appendChild(documento.createTextNode(valor));
    }

    public void importarXML(File fichero) throws ParserConfigurationException, IOException, SAXException {
        listaPrendas = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.parse(fichero);

        NodeList listaElementos = documento.getElementsByTagName("*");

        for (int i = 0; i < listaElementos.getLength(); i++) {
            Element nodoPrenda = (Element) listaElementos.item(i);
            String tipo = nodoPrenda.getTagName();

            Prenda nuevaPrenda;
            if (tipo.equals("Camiseta")) {
                Camiseta camiseta = new Camiseta();
                camiseta.setTalla(leer(nodoPrenda, "talla"));
                nuevaPrenda = camiseta;
            } else if (tipo.equals("Pantalon")) {
                Pantalon pantalon = new Pantalon();
                pantalon.setTallaCintura(Integer.parseInt(leer(nodoPrenda, "tallacintura")));
                nuevaPrenda = pantalon;
            } else if (tipo.equals("Zapatilla")) {
                Zapatilla zapatilla = new Zapatilla();
                zapatilla.setNumero(Double.parseDouble(leer(nodoPrenda, "numero")));
                nuevaPrenda = zapatilla;
            } else {
                continue;
            }

            nuevaPrenda.setCodigo(leer(nodoPrenda, "codigo"));
            nuevaPrenda.setMarca(leer(nodoPrenda, "marca"));
            nuevaPrenda.setNombre(leer(nodoPrenda, "nombre"));
            nuevaPrenda.setFechaEntrada(LocalDate.parse(leer(nodoPrenda, "fechaentrada")));
            nuevaPrenda.setPrecio(Double.parseDouble(leer(nodoPrenda, "precio")));
            nuevaPrenda.setDescuento(Integer.parseInt(leer(nodoPrenda, "descuento")));
            nuevaPrenda.setColor(leer(nodoPrenda, "color"));
            nuevaPrenda.setDescripcion(leer(nodoPrenda, "descripcion"));

            listaPrendas.add(nuevaPrenda);
        }
    }

    private String leer(Element nodo, String etiqueta) {
        return nodo.getElementsByTagName(etiqueta).item(0).getTextContent();
    }

    public ArrayList<Prenda> buscarPrendas(String texto) {
        ArrayList<Prenda> resultado = new ArrayList<>();
        String busqueda = texto.toLowerCase();
        for (Prenda unaPrenda : listaPrendas) {
            if (unaPrenda.getCodigo().toLowerCase().contains(busqueda) ||
                    unaPrenda.getMarca().toLowerCase().contains(busqueda) ||
                    unaPrenda.getNombre().toLowerCase().contains(busqueda) ||
                    unaPrenda.getClass().getSimpleName().toLowerCase().contains(busqueda)) {
                resultado.add(unaPrenda);
            }
        }
        return resultado;
    }
}