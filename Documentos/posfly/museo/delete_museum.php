<?php

require 'Museo.php';

	try{
		$idMuseo = utf8_encode($_POST['idMuseo']);
		Museo::delete($idMuseo);
		echo "Anécdota eliminada";
	}
 catch (PDOException $e) {
	echo $e;
}
catch (Exception $e){
    echo $e;
}

?>
