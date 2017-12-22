package testing

import java.io.File

import scalikejdbc.{ConnectionPool, DBSession, NamedDB, _}

import scala.util.Random

object DBRepo {
  Class.forName("org.sqlite.JDBC")

  def clean(f: File): Unit = {
    using(f) { implicit s: DBSession =>
      sql"""DELETE from FOO"""
        .update()
        .apply()
      }
    }

  def list(f: File): Seq[String] = {
    using(f) { implicit s: DBSession =>
      sql"""SELECT bar FROM foo"""
          .map(_.string("bar"))
          .list()
          .apply()
    }
  }

  private def using[T](f: File)(execution: DBSession => T): T = {
    val symbol = Symbol(Random.alphanumeric.take(32).mkString)
    ConnectionPool.add(symbol, s"jdbc:sqlite:${f.getAbsolutePath}", "", "")
    val result = NamedDB(symbol).localTx(execution)
    ConnectionPool.close(symbol)
    result
  }
}
