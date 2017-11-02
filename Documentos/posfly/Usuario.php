<?php

/**
 * Representa el la estructura de las metas
 * almacenadas en la base de datos
 */
require 'datos/Database.php';

class Usuario
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
        $consulta = "SELECT * FROM usuario";
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
    public static function getById($idUsuario)
    {
        // Consulta de la meta
        $consulta = "SELECT id_usuario, nombre, apellido, correo, rol FROM usuario WHERE id_usuario = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($idUsuario));
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
        $idUsuario,
        $nombre,
        $apellido,
        $correo,
        $rol,
        $pass
    )
    {
        // Creando consulta UPDATE
        $consulta = "UPDATE usuario" .
            " SET nombre=?, apellido=?, correo=?, rol=?, pass=? " .
            "WHERE id_usuario=?";

        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($nombre, $apellido, $correo, $rol, $pass, $idUsuario));

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
        $id_usuario,
        $nombre,
        $apellido,
        $correo,
        $pass,
        $rol
    )
    {
        // Sentencia INSERT
        $comando = "INSERT INTO usuario ( " .
            "id_usuario," .
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