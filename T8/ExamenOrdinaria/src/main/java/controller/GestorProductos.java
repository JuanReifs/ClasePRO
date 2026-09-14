package controller;

import database.ConexionDB;
import dto.ProductosDTO;
import model.Productos;

public class GestorProductos {

    private Productos productos;
    private ConexionDB conexionDB;
    private ProductosDTO productosDTO;

    public GestorProductos() {

     this.conexionDB = new ConexionDB();
     this.productos = new Productos();
     this.productosDTO = new ProductosDTO();
    }

public void crearPRoducto(String nombre, Double precio, int cantidad){

        productos = new Productos(nombre, precio, cantidad);
        productosDTO.realizarInsercionPrepare();
}

public void borrarProducto(Long id){
        productos = new Productos(id);
        productosDTO.realizarBorradoPrepare();

}

public void buscarPorID(Long id){
    productos = new Productos(id);
    productosDTO.realizarBorradoPrepare();

}

public void mostrarTodos(){
        productosDTO.mostrarALLProductos();
}
}
