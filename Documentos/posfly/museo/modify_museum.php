<?php

require 'Museo.php';

try{
    $idMuseo = utf8_encode($_POST['idMuseo']);
    $nombre = utf8_encode($_POST['nombre']);
    $fecha = utf8_encode($_POST['fecha']);
    $descripcion = utf8_encode($_POST['descripcion']);
    $anonimo = utf8_encode($_POST['anonimo']);
	
Museo::update($idMuseo, $nombre, $fecha, $descripcion, $anonimo);
echo "Anécdota modificada con éxito";
}
 catch (PDOException $e) {
	echo $e;
}
catch (Exception $e){
    echo $e;
}

?>
