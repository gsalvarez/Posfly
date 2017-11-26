<?php

require 'Database.php';

class Museo
{
    function __construct()
    {
    }

    public static function getAll() {

        $consulta = "SELECT * FROM museo";

        try {
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            $comando->execute();
            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
    }

    public static function getById($idMuseo) {
        
        $consulta = "SELECT id_museo, nombre, id_usuario, descripcion, fecha FROM museo WHERE id_museo = ?";

        try {
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            $comando->execute(array($idMuseo));
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;

        } catch (PDOException $e) {
            return -1;
        }
    }

    public static function update($idMuseo, $nombre, $fecha, $descripcion, $anonimo) {
        
        $consulta = "UPDATE museo" .
            " SET nombre=?, descripcion=?, fecha=?, anonimo=? " .
            "WHERE id_museo=?";

        $cmd = Database::getInstance()->getDb()->prepare($consulta);
        $cmd->execute(array($nombre, $descripcion, $fecha, $anonimo, $idMuseo));

        return $cmd;
    }

    public static function insert($nombre, $id_usuario, $descripcion, $fecha, $anonimo) {
    
        $comando = "INSERT INTO museo ( nombre, id_usuario, descripcion, fecha, anonimo)" .
            " VALUES( ?,?,?,?,?)";

        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(array($nombre, $id_usuario, $descripcion, $fecha, $anonimo));
    }

    public static function delete($idMuseo) {
        
        $comando = "DELETE FROM museo WHERE id_museo=?";

        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(array($idMuseo));
    }
}
?>
