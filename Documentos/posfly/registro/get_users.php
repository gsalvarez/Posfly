<?php

require 'Usuario.php';

try{
    $users = Usuario::getAll();
} catch (PDOException $e){
	echo $e;
}
    if ($users) {

        $datos["estado"] = 1;
        $datos["users"] = $users;

        print json_encode($datos);
    } else {
	echo $users;
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
?>
