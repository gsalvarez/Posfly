<?php

require 'Usuario.php';

try{
    $idUsuario = utf8_encode($_POST['idUsuario']);
    $nombre = utf8_encode($_POST['nombre']);
    $apellido = utf8_encode($_POST['apellido']);
    $correo = utf8_encode($_POST['correo']);

    Usuario::modifyProfile($idUsuario, $nombre, $apellido, $correo);
    
    echo "Perfil modificado con éxito";
}
catch(Exception $e){
    echo $e;
    if(strpos( $e, "PRIMARY" ) !== false) {
        echo "Este usuario ya está en uso";
    }
    else if(strpos( $e, "correo" ) !== false) {
        echo "Este correo ya está en uso";
    }
}
?>
