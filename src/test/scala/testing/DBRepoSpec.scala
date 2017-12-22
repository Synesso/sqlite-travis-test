package testing

import java.io.File
import java.nio.file.Files

import org.specs2.mutable.Specification

class DBRepoSpec extends Specification {

  "mbtile repo" should {
    "do things with the sqlite db" >> {
      val db = new File("do-things.db")
      Files.copy(new File("src/test/resources/db").toPath, db.toPath)
      db.deleteOnExit()

      DBRepo.clean(db)
      DBRepo.list(db) must beEmpty
    }
  }
}
