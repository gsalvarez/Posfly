<?php

require 'Museo.php';

try{
    $nombre = utf8_encode($_POST['nombre']);
    $fecha = utf8_encode($_POST['fecha']);
    $descripcion = utf8_encode($_POST['descripcion']);
    $anonimo = utf8_encode($_POST['anonimo']);
    $idUsuario = utf8_encode($_POST['idUsuario']);

    Museo::insert($nombre, $idUsuario, $descripcion, $fecha, $anonimo);
    
    echo "Anécdota creada con éxito";
}
catch (PDOException $e) {
	echo $e;
}

?>
