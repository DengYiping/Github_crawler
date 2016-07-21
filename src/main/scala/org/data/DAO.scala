package org.data
import java.sql.{PreparedStatement, DriverManager}

import scala.util.Try

/**
  * Created by Scott on 6/24/16.
  */
object DAO {
  // JDBC driver name and database URL
  private val DB_URL = "jdbc:mysql://" + Config.config.getString("db.address") + "/" + Config.config.getString("db.db_name")
  //  Database credentials
  private val USER = "root"
  private val PASS = "123456"
  private val connection  = DriverManager.getConnection(DB_URL,USER,PASS)
  def createStandardTable(table_name:String):Unit = {
      val temp_stmt = connection.createStatement()
      val sql = "CREATE TABLE IF NOT EXISTS " + table_name + " (id INT NOT NULL AUTO_INCREMENT, URL VARCHAR(255) NOT NULL, PRIMARY KEY (id));"
      temp_stmt.executeUpdate(sql)
      temp_stmt.close()
  }
  def insert(table_name:String, url:String):Unit = {
    val temp_stmt = connection.createStatement()
    val sql  = "INSERT INTO " + table_name + " (URL) VALUES ('" + url + "')"
    temp_stmt.executeUpdate(sql)
    temp_stmt.close()
  }
  def drop_table(table_name:String):Unit = {
    val temp_stmt = connection.createStatement()
    val sql = "DROP TABLE " + table_name
    temp_stmt.executeUpdate(sql)
    temp_stmt.close()
  }
  def main(args:Array[String]):Unit = {
    insert("python","asdfasdfqwer")
  }
}

