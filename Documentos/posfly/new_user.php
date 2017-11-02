<?php

require 'Usuario.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    
    $idUsuario = $_GET['idUsuario'];
    $nombre = $_GET['nombre'];
    $apellido = $_GET['apellido'];
    $correo = $_GET['correo'];
    $pass = $_GET['pass'];
    $rol = "user";

    Usuario::insert($idUsuario, $nombre, $apellido, $correo, $pass, $rol);
}


/*
    require 'datos/Database.php';

    $idUsuario = $_GET['idUsuario'];
    $nombre = $_GET['nombre'];
    $apellido = $_GET['apellido'];
    $correo = $_GET['correo'];
    $pass = $_GET['pass'];
    $rol = "user";
        
    $comando = "INSERT INTO usuario (id_usuario, nombre, apellido, correo, pass, rol) VALUES( ?,?,?,?,?,?)";
    $sentencia = Database::getInstance()->getDb()->prepare($comando);
    return $sentencia->execute(array($idUsuario, $nombre, $apellido, $correo, $pass, $rol));
    echo $sentencia;
*/
?>