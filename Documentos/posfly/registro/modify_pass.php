<?php

require 'Usuario.php';

try{
    $idUsuario = utf8_encode($_POST['idUsuario']);
    $pass = utf8_encode($_POST['pass']);
    $newPass = utf8_encode($_POST['newPass']);

    Usuario::modifyPass($idUsuario, $pass, $newPass);
    
    echo "Contraseña modificada con éxito";
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
