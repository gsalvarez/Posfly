<?php

require 'Evento.php';

try{
    $oldNombre = utf8_encode($_POST['oldNombre']);
    $nombre = utf8_encode($_POST['nombre']);
    $lugar = utf8_encode($_POST['lugar']);
    $fecha = utf8_encode($_POST['fecha']);
    $hora = utf8_encode($_POST['hora']);
    $descripcion = utf8_encode($_POST['descripcion']);
    $precio = utf8_encode($_POST['precio']);
    $calificacion = utf8_encode($_POST['calificacion']);
	
Evento::update($oldNombre, $nombre, $lugar, $fecha, $hora, $descripcion, $precio, $calificacion);
echo "Evento modificado con éxito";
}
 catch (PDOException $e) {
	echo $e;
}
catch (Exception $e){
    echo $e;
}

?>
