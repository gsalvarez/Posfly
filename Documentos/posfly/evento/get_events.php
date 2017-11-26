<?php

require 'Evento.php';
    try {
    $users = Evento::getAll();
	}
catch(PDOException $e){
	echo $e;
}
    if ($users) {

        print json_encode($users);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
?>
