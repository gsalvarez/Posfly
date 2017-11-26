<?php

require 'Usuario.php';

$idUsuario = utf8_encode($_POST['idUsuario']);
	
try{
    $users = Usuario::getProfile($idUsuario);
} catch (PDOException $e){
	echo $e;
}
    if ($users) {
      
        print json_encode($users);
    } else {
	echo $users;
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
?>
