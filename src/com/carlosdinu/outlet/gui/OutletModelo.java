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

    /*
        OutletModelo, este metodo se encarga de crear la lista vacia donde se guardan todas las prendas
     */
    public OutletModelo() {
        listaPrendas = new ArrayList<>();
    }

    /*
        obtenerPrendas, devuelve la lista completa de prendas
     */
    public ArrayList<Prenda> obtenerPrendas() {
        return listaPrendas;
    }

    /*
        altaCamiseta, se encarga de crear una Camiseta con los datos recibidos y añadirla a la lista
     */
    public void altaCamiseta(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                             double precio, int descuento, String color, String descripcion,
                             String talla) {
        listaPrendas.add(new Camiseta(codigo, marca, nombre, fechaEntrada,
                precio, descuento, color, descripcion, talla));
    }

    /*
        altaPantalon, se encarga de crear un Pantalon con los datos recibidos y añadirlo a la lista
     */
    public void altaPantalon(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                             double precio, int descuento, String color, String descripcion,
                             int tallaCintura) {
        listaPrendas.add(new Pantalon(codigo, marca, nombre, fechaEntrada,
                precio, descuento, color, descripcion, tallaCintura));
    }

    /*
        altaZapatilla, se encarga de crear una Zapatilla con los datos recibidos y añadirla a la lista
     */
    public void altaZapatilla(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                              double precio, int descuento, String color, String descripcion,
                              double numero) {
        listaPrendas.add(new Zapatilla(codigo, marca, nombre, fechaEntrada,
                precio, descuento, color, descripcion, numero));
    }

    /*
        eliminarPrenda, se encarga de borrar de la lista la prenda recibida
     */
    public void eliminarPrenda(Prenda prenda) {
        listaPrendas.remove(prenda);
    }

    /*
        existeCodigo, recorre la lista y devuelve true si alguna prenda ya tiene ese codigo,
        sirve para que no se repitan codigos
     */
    public boolean existeCodigo(String codigo) {
        for (Prenda unaPrenda : listaPrendas) {
            if (unaPrenda.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    /*
        buscarPrendas, devuelve una lista nueva solo con las prendas cuyo codigo, marca, nombre o tipo contienen
        el texto buscado. Paso todo a minusculas para que encuentre igual en mayusculas o minusculas,
        si el texto esta vacio devuelve todas.
     */
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

    /*
        exportarXML, se encarga de guardar todas las prendas de la lista en un fichero .xml,
        crea el documento, añade el nodo raiz prendas y dentro una etiqueta por cada prenda con todos sus datos
     */
    public void exportarXML(File fichero) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        Document documento = dom.createDocument(null, "xml", null);

        //añado el nodo raiz
        Element raiz = documento.createElement("prendas");
        documento.getDocumentElement().appendChild(raiz);

        for (Prenda unaPrenda : listaPrendas) {
            //segun el tipo de prenda creo su etiqueta
            Element nodoPrenda;
            if (unaPrenda instanceof Camiseta) {
                nodoPrenda = documento.createElement("Camiseta");
            } else if (unaPrenda instanceof Pantalon) {
                nodoPrenda = documento.createElement("Pantalon");
            } else {
                nodoPrenda = documento.createElement("Zapatilla");
            }
            raiz.appendChild(nodoPrenda);

            //campos comunes a todas las prendas
            crearElemento(documento, nodoPrenda, "codigo", unaPrenda.getCodigo());
            crearElemento(documento, nodoPrenda, "marca", unaPrenda.getMarca());
            crearElemento(documento, nodoPrenda, "nombre", unaPrenda.getNombre());
            crearElemento(documento, nodoPrenda, "fechaentrada", unaPrenda.getFechaEntrada().toString());
            crearElemento(documento, nodoPrenda, "precio", String.valueOf(unaPrenda.getPrecio()));
            crearElemento(documento, nodoPrenda, "descuento", String.valueOf(unaPrenda.getDescuento()));
            crearElemento(documento, nodoPrenda, "color", unaPrenda.getColor());
            crearElemento(documento, nodoPrenda, "descripcion", unaPrenda.getDescripcion());

            //campo propio de cada prenda, hago casting para usar su get
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

        //guardo en fichero
        Source source = new DOMSource(documento);
        Result resultado = new StreamResult(fichero);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, resultado);
    }

    /*
        crearElemento, metodo auxiliar que crea una etiqueta <etiqueta>valor</etiqueta> dentro del nodo padre,
        lo uso para no repetir las mismas lineas en cada campo
     */
    private void crearElemento(Document documento, Element padre, String etiqueta, String valor) {
        Element nodoDatos = documento.createElement(etiqueta);
        padre.appendChild(nodoDatos);
        nodoDatos.appendChild(documento.createTextNode(valor));
    }

    /*
        importarXML, se encarga de leer un fichero .xml y cargar sus prendas en la lista,
        recorre todas las etiquetas y cuando encuentra Camiseta, Pantalon o Zapatilla crea ese objeto y lo rellena
     */
    public void importarXML(File fichero) throws ParserConfigurationException, IOException, SAXException {
        listaPrendas = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.parse(fichero);

        NodeList listaElementos = documento.getElementsByTagName("*");

        for (int i = 0; i < listaElementos.getLength(); i++) {
            Element nodoPrenda = (Element) listaElementos.item(i);
            String tipo = nodoPrenda.getTagName();

            //creo la prenda segun su etiqueta y relleno su campo propio
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
                //si no es una prenda (raiz, codigo, marca...) la salto
                continue;
            }

            //campos comunes, valen para las 3 gracias a la herencia
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

    /*
        leer, metodo auxiliar que devuelve el texto de una etiqueta dentro del nodo,
        busca por nombre de etiqueta asi no falla aunque el xml tenga sangrias
     */
    private String leer(Element nodo, String etiqueta) {
        return nodo.getElementsByTagName(etiqueta).item(0).getTextContent();
    }
}