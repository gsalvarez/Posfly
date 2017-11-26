<?php

require 'Database.php';

class Usuario
{
    function __construct()
    {
    }

    public static function getToken($idUsuario, $token) {
	$consulta = "SELECT id_usuario, token FROM forgot WHERE id_usuario=? AND token=?";
	$comando = Database::getInstance()->getDb()->prepare($consulta);
	$comando->execute(array($idUsuario, $token));
	$row = $comando->FETCH(PDO::FETCH_ASSOC);
	return $row;	
    }

    public static function getAll()
    {
        $consulta = "SELECT * FROM usuario";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
	    echo $e;
            return false;
        }
    }

    public static function getByEmail($correo) {
	$consulta = "SELECT id_usuario FROM usuario WHERE correo = ?";
	$comando = Database::getInstance()->getDb()->prepare($consulta);
	$comando->execute(array($correo));
	$row = $comando->FETCH(PDO::FETCH_ASSOC);
	return $row;
}

    public static function getProfile($idUsuario) {

	$consulta = "SELECT nombre, apellido, correo FROM usuario WHERE id_usuario = ?";
	
	$comando = Database::getInstance()->getDb()->prepare($consulta);

	$comando->execute(array($idUsuario));
	
	$row = $comando->fetch(PDO::FETCH_ASSOC);
	return $row;
	
    }

    public static function getById($idUsuario, $pass) {

        $consulta = "SELECT * FROM usuario WHERE id_usuario = ? and pass = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($idUsuario, $pass));
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;

        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }

    public static function modifyProfile($idUsuario, $nombre, $apellido, $correo) {
        
	$consulta = "UPDATE usuario SET nombre=?, apellido=?, correo=? WHERE id_usuario=?";

        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        $cmd->execute(array($nombre, $apellido, $correo, $idUsuario));

        return $cmd;
    }

    public static function modifyPass($idUsuario, $pass, $newPass) {
	$consulta = "UPDATE usuario SET pass = ? WHERE id_usuario = ? and pass = ?";
	
	$cmd = Database::getInstance()->getDb()->prepare($consulta);

	$cmd->execute(array($newPass, $idUsuario, $pass));

	return $cmd;
    }

    public static function insertCode($correo, $code) {
	$consulta = "INSERT INTO forgot (correo, codigo) VALUES (?,?)";
	$cmd = Database::getInstance()->getDb()->prepare($consulta);
	$cmd->execute(array($correo, $code));
	return $cmd;
    }

    public static function insert(
        $id_usuario,
        $nombre,
        $apellido,
        $correo,
        $pass,
        $rol
    )
    {
        // Sentencia INSERT
        $comando = "INSERT INTO usuario ( id_usuario," .
            " nombre," .
            " apellido," .
            " correo," .
            " pass," .
            " rol)" .
            " VALUES( ?,?,?,?,?,?)";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(
            array(
                $id_usuario,
                $nombre,
                $apellido,
                $correo,
                $pass,
                $rol
            )
        );

    }

    /**
     * Eliminar el registro con el identificador especificado
     *
     * @param $idMeta identificador de la meta
     * @return bool Respuesta de la eliminación
     */
    public static function delete($idUsuario)
    {
        // Sentencia DELETE
        $comando = "DELETE FROM usuario WHERE id_usuario=?";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(array($idUsuario));
    }
}

?>
