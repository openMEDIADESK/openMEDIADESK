package com.stumpner.sql.installer;

import com.stumpner.mediadesk.core.Config;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 04.07.2008
 * Time: 13:22:13
 * To change this template use File | Settings | File Templates.
 */
public class SqlInstallerBase implements SqlInstaller {

    private File file = null;
    private Connection connection = null;

    public SqlInstallerBase(File file, Connection connection) {
        this.file = file;
        this.connection = connection;
    }

    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void run() throws IOException, SQLException {

        StringBuffer sqlCommandBuilder = new StringBuffer();
        ArrayList sqlCommandList = new ArrayList();
        FileReader reader = new FileReader(file);
        LineNumberReader lrReader = new LineNumberReader(reader);
        String lineString = "";
        while ((lineString = lrReader.readLine())!=null) {
            sqlCommandBuilder.append(lineString);
            if (lineString.endsWith(";")) {
                sqlCommandList.add(sqlCommandBuilder.toString());
                sqlCommandBuilder = new StringBuffer();
            }
        }
        if (sqlCommandList.size()>0) {
            Statement stmt = connection.createStatement();
            //Sql-Commands abarbeiten:
            Iterator sqlCommands = sqlCommandList.iterator();
            while (sqlCommands.hasNext()) {
                String sqlCommand = (String)sqlCommands.next();
                System.out.println("Processing SQL-Command: "+sqlCommand);
                stmt.addBatch(sqlCommand);
            }
            //Execute Sql
            System.out.println("["+ Config.instanceName+"]: Processing Batch...");
            stmt.executeBatch();
            System.out.println("["+ Config.instanceName+"]: ...OK");
            stmt.close();
        }

    }
}
