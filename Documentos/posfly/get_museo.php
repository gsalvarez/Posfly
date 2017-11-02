<?php
/**
 * Obtiene todas las metas de la base de datos
 */

require 'Museo.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {

    // Manejar petición GET
    $museos = Museo::getAll();

    if ($museos) {

        $datos["estado"] = 1;
        $datos["museos"] = $museos;

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
}