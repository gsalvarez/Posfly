<?php

/**
 * Representa el la estructura de las metas
 * almacenadas en la base de datos
 */
require 'Database.php';

class Evento
{
    function __construct()
    {
    }

    /**
     * Retorna en la fila especificada de la tabla 'meta'
     *
     * @param $idMeta Identificador del registro
     * @return array Datos del registro
     */
    public static function getAll()
    {
        $consulta = "SELECT * FROM evento";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
    }

    /**
     * Obtiene los campos de una meta con un identificador
     * determinado
     *
     * @param $idMeta Identificador de la meta
     * @return mixed
     */
    public static function getById($idEvento)
    {
        // Consulta de la meta
        $consulta = "SELECT id_evento, nombre, lugar, fecha, hora, descripcion, precio, id_usuario, calificacion FROM evento WHERE id_evento = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($idEvento));
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;

        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }

public static function update($oldNombre, $nombre, $lugar, $fecha, $hora, $descripcion, $precio, $calificacion) {
	$consulta = "UPDATE evento SET nombre=?, lugar=?, fecha=?, hora=?, descripcion=?, precio=?, calificacion=? WHERE nombre=?";
	$cmd = Database::getInstance()->getDb()->prepare($consulta);
        $cmd->execute(array($nombre, $lugar, $fecha, $hora, $descripcion, $precio, $calificacion, $oldNombre));
        return $cmd;
    }

    /**
     * Insertar una nueva meta
     *
     * @param $titulo      titulo del nuevo registro
     * @param $descripcion descripción del nuevo registro
     * @param $fechaLim    fecha limite del nuevo registro
     * @param $categoria   categoria del nuevo registro
     * @param $prioridad   prioridad del nuevo registro
     * @return PDOStatement
     */
    public static function insert(
        $nombre,
        $lugar,
        $fecha,
        $hora,
        $descripcion,
        $precio,
        $idUsuario,
        $calificacion
    )
    {
        // Sentencia INSERT
        $comando = "INSERT INTO evento ( " .
            " nombre," .
            " lugar," .
            " fecha," .
            " hora," .
            " descripcion," .
            " precio," .
            " id_usuario," .
            " calificacion)" .
            " VALUES( ?,?,?,?,?,?,?,?)";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(
            array(
                $nombre,
                $lugar,
                $fecha,
                $hora,
                $descripcion,
                $precio,
                $idUsuario,
                $calificacion
            )
        );

    }

    /**
     * Eliminar el registro con el identificador especificado
     *
     * @param $idMeta identificador de la meta
     * @return bool Respuesta de la eliminación
     */
    public static function delete($idEvento)
    {
        // Sentencia DELETE
        $comando = "DELETE FROM evento WHERE nombre=?";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(array($idEvento));
    }
}

?>
