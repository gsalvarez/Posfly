<?php

use PHPMailer\PHPmailer\Exception;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\PHPmailer;

require 'PHPMailer/src/PHPMailer.php';
require 'PHPMailer/src/SMTP.php';
require 'PHPMailer/src/Exception.php';
require 'Usuario.php';

try {
	$correo = "gabo05640@hotmail.com";
	//$correo = utf8_encode($_POST['correo']);
	$code = generateCode();
	$user = Usuario::getByEmail($correo);
	if($user != null){
		Usuario::insertCode($correo, $code);
		enviarMail($correo, $code);
	}
}
catch(PDOException $e){
	echo $e;
}

function enviarMail($correo, $code){
	$mail = new PHPMailer();
	$mail->IsSMTP();
	$mail->SMTPDebug = 2;
	$mail->SMTPAuth = true;
	//$mail->SMTPSecure = "tls";
	$mail->Host = gethostbyname('ssl://smtp.gmail.com');
	$mail->Port = 465; // or 587 465
	$mail->Username = "posflyadm@gmail.com";
	$mail->Password = "Ingesoft";
	$mail->From = "posflyadm@gmail.com";
	$mail->AddAddress($correo);
	$mail->AddReplyTo("posflyadm@gmail.com");
	$mail->WordWrap = 50;
	$mail->IsHTML = true;
	$mail->Subject = "Codigo de Posfly";
	$mail->Body = "Tu codigo es: " . $code;
	$mail->SMTPOptions = array('ssl' => array('verify_peer' => false,'verify_peer_name' => false, 'allow_self_signed' => true));
	if($mail->Send()){
		echo "true";
	}
	else {
		echo $mail->ErrorInfo;
		echo "false";
	}
	$mail->ClearAddresses();
}

function generateCode(){
	return substr(str_shuffle("0123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ"), 0, 6);
}
?>
