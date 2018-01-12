package com.stumpner.sql.installer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Stellt Funktionen für das erstellen bzw. updaten von SQL-Datenbanken zur Verfügung.
 * Nötig wenn eine neue Applikation eingerichtet wird und das Datenbank-
 * Schema in der SQL-Datenbank angelegt wer
 * User: franz.stumpner
 * Date: 04.07.2008
 * Time: 13:19:13
 * To change this template use File | Settings | File Templates.
 */
public interface SqlInstaller {
    
    /**
     * SQL-Connection zur Datenbank gegen die das SQL-Script gefahren werden soll
     * @param conn
     */
    public void setConnection(Connection conn);

    /**
     * Hier kann die SQL-Datei angegeben werden, das gegen die SQL-Datenbank
     * gefahren werden soll...
     * @param file
     */
    public void setFile(File file);

    /**
     * Installiert/Aktualisiert das Datenbank-Schema.
     */
    public void run() throws IOException, SQLException;

}
