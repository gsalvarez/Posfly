<?php

require 'Usuario.php';

$idUsuario = utf8_encode($_GET['idUsuario']);
$token = utf8_encode($_GET['token']);

$forgot = Usuario::getToken($idUsuario, $token);

if($forgot != null){
	echo "hola";	
}
?>
