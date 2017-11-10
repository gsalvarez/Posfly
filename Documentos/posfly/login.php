<?php

require 'Usuario.php';

try{
    $idUsuario = utf8_encode($_POST['idUsuario']);
    $pass = utf8_encode($_POST['pass']);

    $usuario = Usuario::getById($idUsuario, $pass);

    if($usuario != null){
        echo "Bienvenido";
    }
    else{
        echo "Credenciales incorrectas";
    }
}
catch(Exception $e){
    echo $e;
}
?>