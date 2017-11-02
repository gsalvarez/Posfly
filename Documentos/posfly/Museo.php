<?php

/**
 * Representa el la estructura de las metas
 * almacenadas en la base de datos
 */
require 'datos/Database.php';

class Museo
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
        $consulta = "SELECT * FROM museo";
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
    public static function getById($idMuseo)
    {
        // Consulta de la meta
        $consulta = "SELECT id_museo, nombre, id_usuario, descripcion, fecha FROM museo WHERE id_museo = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($idMuseo));
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;

        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }

    /**
     * Actualiza un registro de la bases de datos basado
     * en los nuevos valores relacionados con un identificador
     *
     * @param $idMeta      identificador
     * @param $titulo      nuevo titulo
     * @param $descripcion nueva descripcion
     * @param $fechaLim    nueva fecha limite de cumplimiento
     * @param $categoria   nueva categoria
     * @param $prioridad   nueva prioridad
     */
    public static function update(
        $idMuseo,
        $nombre,
        $descripcion,
        $fecha
    )
    {
        // Creando consulta UPDATE
        $consulta = "UPDATE museo" .
            " SET nombre=?, descripcion=?, fecha=? " .
            "WHERE id_museo=?";

        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($nombre, $descripcion, $fecha, $idMuseo));

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
        $id_museo,
        $nombre,
        $id_usuario,
        $descripcion,
        $fecha
    )
    {
        // Sentencia INSERT
        $comando = "INSERT INTO museo ( " .
            "id_museo," .
            " nombre," .
            " id_usuario," .
            " descripcion," .
            " fecha)" .
            " VALUES( ?,?,?,?,?)";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(
            array(
                $id_museo,
                $nombre,
                $id_usuario,
                $descripcion,
                $fecha
            )
        );

    }

    /**
     * Eliminar el registro con el identificador especificado
     *
     * @param $idMeta identificador de la meta
     * @return bool Respuesta de la eliminación
     */
    public static function delete($idMuseo)
    {
        // Sentencia DELETE
        $comando = "DELETE FROM museo WHERE id_museo=?";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(array($idMuseo));
    }
}

?>