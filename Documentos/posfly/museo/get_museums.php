<?php

require 'Museo.php';
    try {
    $museums = Museo::getAll();
	}
catch(PDOException $e){
	echo $e;
}
    if ($museums) {

        print json_encode($museums);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
?>
