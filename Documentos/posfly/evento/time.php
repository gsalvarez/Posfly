<?php

require 'Evento.php';

timequery();
	$sql = Evento::getAll();
timequery();
	

function timequery() {
	static $querytime_begin;
	list($usec, $sec) = explode(' ',microtime());

	if(!isset($querytime_begin)) {
		$querytime_begin = ((float)$usec + (float)$sec);
	}
	else {
		$querytime = (((float)$usec + (float)$sec)) - $querytime_begin;
		echo sprintf('<br />La consulta tardó %01.5f segundos.- <br />', $querytime);
		echo $querytime;
	}
}
?>
