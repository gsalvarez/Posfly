<?php

require 'Evento.php';

	try{
		$nombre = utf8_encode($_POST['nombre']);
		Evento::delete($nombre);
		echo "Evento eliminado";
	}
 catch (PDOException $e) {
	echo $e;
}
catch (Exception $e){
    echo $e;
}

?>
