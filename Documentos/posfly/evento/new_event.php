<?php

require 'Evento.php';

try{
    $nombre = utf8_encode($_POST['nombre']);
    $lugar = utf8_encode($_POST['lugar']);
    $fecha = utf8_encode($_POST['fecha']);
    $hora = utf8_encode($_POST['hora']);
    $descripcion = utf8_encode($_POST['descripcion']);
    $precio = utf8_encode($_POST['precio']);
    $idUsuario = utf8_encode($_POST['idUsuario']);
    $calificacion = utf8_encode($_POST['calificacion']);

    Evento::insert($nombre, $lugar, $fecha, $hora, $descripcion, $precio, $idUsuario, $calificacion);
    
    echo "Evento creado con éxito";
}
catch(Exception $e){
    if(strpos( $e, "PRIMARY" ) !== false) {
        echo "Este evento ya existe";
    }
}
?>